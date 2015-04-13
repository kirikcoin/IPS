package mobi.eyeline.ips.web.filters;

import mobi.eyeline.ips.util.ResourcePool;
import mobi.eyeline.lessparser.LessException;
import mobi.eyeline.lessparser.LessParser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class LessFilter extends HttpFilter {

    private static final int LESS_PARSER_INSTANCES = 8;

    private final Pattern FILTER_PATTERN = Pattern.compile(".*\\.less\\.faces|.*\\.less");

    private ResourcePool<LessParser> parserPool;
    private boolean devMode;
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            parserPool = new ResourcePool<LessParser>(LESS_PARSER_INSTANCES) {
                @Override
                protected LessParser init() throws LessException {
                    return new LessParser();
                }
            };

        } catch (Exception e) {
            throw new ServletException("Can't init LessParser.", e);
        }

        servletContext = filterConfig.getServletContext();

        String projectStage = servletContext.getInitParameter("facelets.DEVELOPMENT");
        devMode = projectStage != null && projectStage.equalsIgnoreCase("true");

        servletContext.log("LESS Filter: initialized successfully. devMode = " + devMode) ;
    }

    @Override
    protected void doFilter(HttpServletRequest req,
                            HttpServletResponse resp,
                            FilterChain filterChain) throws IOException, ServletException {

        final String url = req.getRequestURI();
        if (url == null || !FILTER_PATTERN.matcher(url).matches()) {
            filterChain.doFilter(req, resp);
            return;
        }

        final RespWrapper httpResponse = new RespWrapper(resp);
        filterChain.doFilter(req, httpResponse);

        byte[] result;
        if (httpResponse.status == 200 || httpResponse.status == 0) {
            result = lessToCss(req, httpResponse);
            if (devMode)
                resp.addHeader("Cache-Control", "no-store");
        } else {
            result = httpResponse.getBytes();
        }

        resp.setContentLength(result.length);

        if (resp.getContentType() == null || resp.getContentType().isEmpty())
            resp.setContentType("text/css; encoding=utf-8");

        resp.getOutputStream().write(result);

        resp.flushBuffer();
    }

    private byte[] lessToCss(final HttpServletRequest request,
                             RespWrapper response) throws ServletException, UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        byte[] css;
        try {
            String encoding = request.getCharacterEncoding();
            Charset charset = encoding == null ? Charset.defaultCharset() : Charset.forName(encoding);

            final String less = new String(response.getBytes(), charset);

            css = parserPool.execute(new ResourcePool.ResourceCallable<byte[], LessParser>() {
                @Override
                public byte[] call(LessParser parser) throws LessException {
                    return parser.parse(less, getPath(request), !devMode).getBytes();
                }
            });

        } catch (Exception e) {
            throw new ServletException(e);
        }

        servletContext.log("LESS Filter: '" + request.getRequestURI() + "' was parsed in " + (System.currentTimeMillis() - start) + " ms.");
        return css;
    }

    private String getPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("javax.faces.resource")) {
            String libraryName = request.getParameter("ln");
            if (libraryName != null)
                uri = uri.replace("javax.faces.resource", "resources/" + libraryName);
        }

        return servletContext.getRealPath(uri);
    }

    private class RespWrapper extends HttpServletResponseWrapper {

        private final ByteArrayServletOutputStream os;
        private int status;

        public RespWrapper(HttpServletResponse response) {
            super(response);
            os = new ByteArrayServletOutputStream();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return os;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(os);
        }

        @Override
        public void setStatus(int sc) {
            this.status = sc;
            super.setStatus(sc);
        }

        public byte[] getBytes() {
            return os.os.toByteArray();
        }
    }


    private class ByteArrayServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream os;

        private ByteArrayServletOutputStream() {
            this.os = new ByteArrayOutputStream();
        }

        @Override
        public void write(int b) throws IOException {
            os.write(b);
        }
    }
}
