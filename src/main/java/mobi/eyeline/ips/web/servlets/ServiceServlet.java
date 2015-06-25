package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.util.RequestParseUtils;
import mobi.eyeline.ips.util.StringUtils;
import mobi.eyeline.util.jsf.components.utils.NumberUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mobi.eyeline.ips.util.RequestParseUtils.toQueryString;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

public class ServiceServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req,
                       HttpServletResponse resp) throws ServletException, IOException {

    final List<String> pathParts = StringUtils.split(req.getRequestURI(), "/");
    if ((pathParts == null) || pathParts.isEmpty()) {
      resp.setStatus(SC_BAD_REQUEST);
      resp.setContentLength(0);
      return;
    }

    final String lastPart = pathParts.get(pathParts.size() - 1);
    final Integer surveyId = NumberUtils.parseOrNull(lastPart);
    if (surveyId == null) {
      resp.setStatus(SC_BAD_REQUEST);
      resp.setContentLength(0);
      return;
    }

    // Ensure all parameters are passed through.
    final Map<String, String[]> parameters = new HashMap<>();
    parameters.putAll(req.getParameterMap());
    parameters.put("survey_id", new String[] { String.valueOf(surveyId) });

    // Forward to the handler JSP.
    final String jsp = "/ussd/index.jsp" + toQueryString(parameters);
    final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
    dispatcher.forward(req, resp);
  }
}
