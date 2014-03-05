package mobi.eyeline.ips.web

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.regex.Pattern

import static mobi.eyeline.ips.web.BuildVersion.BUILD_VERSION

class ResourceFilter implements Filter {

    private final Pattern FILTER_PATTERN_CSS =
            Pattern.compile(".*\\.css$BUILD_VERSION|.*\\.css")

    @Override
    void init(FilterConfig filterConfig) {
        // Nothing here.
    }

    @Override
    void doFilter(ServletRequest servletRequest,
                  ServletResponse servletResponse,
                  FilterChain filterChain) {

        HttpServletRequest request = (HttpServletRequest) servletRequest
        HttpServletResponse response = (HttpServletResponse) servletResponse

        if (FILTER_PATTERN_CSS.matcher(request.requestURI).matches()) {
            response.setHeader('Content-Type', 'text/css')
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }

    @Override
    void destroy() {
        // Nothing here.
    }
}
