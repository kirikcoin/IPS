package mobi.eyeline.ips.util;

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

/**
 * User: user
 * Date: 17.01.14
 */
public class LessFilter implements Filter {

  private final Pattern FILTER_PATTERN = Pattern.compile(".*\\.less\\.faces|.*\\.less");

  private LessParser parser;
  private boolean devMode;
  private ServletContext servletContext;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    try {
      parser = new LessParser();
    } catch (LessException e) {
      throw new ServletException("Can't init LessFilter.", e);
    }

    servletContext = filterConfig.getServletContext();

    String projectStage = servletContext.getInitParameter("facelets.DEVELOPMENT");
    devMode = projectStage != null && projectStage.equalsIgnoreCase("true");

    servletContext.log("LESS Filter: initialized successfully. devMode = " + devMode) ;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    String url = httpRequest.getRequestURI();
    if (url == null || !FILTER_PATTERN.matcher(url).matches()) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    RespWrapper httpResponse = new RespWrapper((HttpServletResponse) servletResponse);
    filterChain.doFilter(httpRequest, httpResponse);

    byte[] result;
    if (httpResponse.status == 200 || httpResponse.status == 0) {
      result = lessToCss(httpRequest, httpResponse);
      if (devMode)
        ((HttpServletResponse) servletResponse).addHeader("Cache-Control", "no-store");
    } else {
      result = httpResponse.getBytes();
    }

    servletResponse.setContentLength(result.length);

    if (servletResponse.getContentType() == null || servletResponse.getContentType().isEmpty())
      servletResponse.setContentType("text/css; encoding=utf-8");

    servletResponse.getOutputStream().write(result);

    servletResponse.flushBuffer();
  }

  private byte[] lessToCss(HttpServletRequest request, RespWrapper response) throws ServletException, UnsupportedEncodingException {
    long start = System.currentTimeMillis();
    byte[] css;
    try {
      String encoding = request.getCharacterEncoding();
      Charset charset = encoding == null ? Charset.defaultCharset() : Charset.forName(encoding);

      String less = new String(response.getBytes(), charset);
      css = parser.parse(less, getPath(request), !devMode).getBytes();

    } catch (LessException e) {
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

  @Override
  public void destroy() {
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
