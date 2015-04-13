package mobi.eyeline.ips.web.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Triggers eager request parts parsing, absorbing any exceptions occurred. The rationale for this
 * hack is to handle the following case:
 * <ol>
 *     <li>
 *         POST request of {@literal multipart/form-data} type is performed.
 *         It contains attachment of the size exceeding the limit declared in the
 *         {@literal multipart-config} section.
 *     </li>
 *     <li>
 *         JSF servlet handles request. During this, it tries to get some request parameters etc
 *         (which is totally expected).
 *     </li>
 *     <li>
 *         Tomcat's request implementation tries parsing all the request on any parameter fetches,
 *         and throws an exception in case part size limit is exceeded.
 *     </li>
 *     <li>
 *         Faces servlet doesn't know anything about file size constraints, so
 *         just considers the request malformed.
 *     </li>
 *     <li>
 *         Error page gets served. The problem here is that we don't get anywhere near FileUpload
 *         handler and have no way to provide any meaningful message to that strange guy who
 *         actually tried to upload something.
 *     </li>
 * </ol>
 */
public class RequestEagerParsingFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req,
                            HttpServletResponse resp,
                            FilterChain chain) throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");

        try {
            tryParsingIfApplicable(req);

        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/error.faces?id=400&type=FILE_SIZE");
            return;
        }

        chain.doFilter(req, resp);
    }

    private void tryParsingIfApplicable(HttpServletRequest httpRequest)
            throws IOException, ServletException {
        final String contentType = httpRequest.getContentType();
        if ((contentType != null) &&
                contentType.toLowerCase().contains("multipart/form-data")) {
            httpRequest.getParts();
        }
    }
}
