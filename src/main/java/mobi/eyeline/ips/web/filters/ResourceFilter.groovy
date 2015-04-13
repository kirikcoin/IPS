package mobi.eyeline.ips.web.filters

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.regex.Pattern

import static mobi.eyeline.ips.web.BuildVersion.BUILD_VERSION

class ResourceFilter extends HttpFilter {

  private final Pattern FILTER_PATTERN_CSS =
      Pattern.compile(".*\\.css$BUILD_VERSION|.*\\.css")

  @Override
  protected void doFilter(HttpServletRequest req,
                          HttpServletResponse resp,
                          FilterChain filterChain) {

    if (FILTER_PATTERN_CSS.matcher(req.requestURI).matches()) {
      resp.setHeader('Content-Type', 'text/css')
    }

    filterChain.doFilter req, resp
  }
}
