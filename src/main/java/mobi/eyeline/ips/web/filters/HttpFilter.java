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

public abstract class HttpFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException { /* Nothing here. */ }

  @Override
  public void doFilter(ServletRequest request,
                       ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {

    doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }

  protected abstract void doFilter(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain chain) throws IOException, ServletException;

  @Override
  public void destroy() { /* Nothing here. */ }
}
