package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.util.RequestParseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

public class OuterRequest {

  private static final String PARAM_STORED_SOURCE = "X-IPS-Source";

  private final HttpServletRequest request;

  public OuterRequest(HttpServletRequest request) {
    this.request = request;
  }

  public Map<String, String[]> getUrlParams() {
    return request.getParameterMap();
  }

  @Override
  public String toString() {
    final StringBuilder buf = new StringBuilder();

    buf.append("URI params: ").append(RequestParseUtils.toString(getUrlParams())).append(", ");
    buf.append("Headers: ").append(RequestParseUtils.getHeaders(request)).append(", ");
    if (request.getSession() != null) {
      buf.append("Session: ").append(RequestParseUtils.getSessionAttributes(request.getSession())).append(", ");
    }

    buf.append("Determined: source = [").append(getSource()).append("],")
        .append(" stored source = [").append(getStoredSource()).append("]");

    return buf.toString();
  }

  public String getString(String key) throws MissingParameterException {
    return RequestParseUtils.getString(getUrlParams(), key);
  }

  public String getString(String key, String defaultValue) {
    return RequestParseUtils.getString(getUrlParams(), key, defaultValue);
  }

  public int getInt(String key) throws MissingParameterException {
    return RequestParseUtils.getInt(getUrlParams(), key);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return RequestParseUtils.getBoolean(getUrlParams(), key, defaultValue);
  }

  public String getStoredSource() {
    final HttpSession session = request.getSession();
    if (session == null) {
      return null;
    }

    final Object attribute = session.getAttribute(PARAM_STORED_SOURCE);
    return (attribute == null) ? null : attribute.toString().trim();
  }

  public void setStoredSource(String value) {
    final HttpSession session = request.getSession();
    if (session != null) {
      session.removeAttribute(PARAM_STORED_SOURCE);

      if (value != null) {
        session.setAttribute(PARAM_STORED_SOURCE, value);
      }
    }
  }

  public String getSource() {
    // Set by Mobilizer.
    final String connector = request.getHeader("X-Connector");
    final String description = request.getHeader("X-Connector-Description");

    if ("sip".equalsIgnoreCase(trimToEmpty(connector)) && isNotBlank(description)) {
      return description.trim();
    }

    return getStoredSource();
  }

}
