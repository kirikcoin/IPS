package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.model.RespondentSource;
import mobi.eyeline.ips.util.RequestParseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static mobi.eyeline.ips.model.RespondentSource.RespondentSourceType.C2S;
import static mobi.eyeline.ips.model.RespondentSource.RespondentSourceType.TELEGRAM;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

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

  public RespondentSource getStoredSource() {
    final HttpSession session = request.getSession();
    if (session == null) {
      return null;
    }

    return (RespondentSource) session.getAttribute(PARAM_STORED_SOURCE);
  }

  public void setStoredSource(RespondentSource value) {
    final HttpSession session = request.getSession();
    if (session != null) {
      session.removeAttribute(PARAM_STORED_SOURCE);

      if (value != null) {
        session.setAttribute(PARAM_STORED_SOURCE, value);
      }
    }
  }

  public RespondentSource getSource() {
    // Set by Mobilizer.
    final String connector = request.getHeader("X-Connector");
    final String description = request.getHeader("X-Connector-Description");

    if ("sip".equalsIgnoreCase(trimToEmpty(connector)) && isNotBlank(description)) {
      final RespondentSource source = new RespondentSource();
      source.setSource(description.trim());
      source.setSourceType(C2S);
      return source;
    }

    if ("telegram".equalsIgnoreCase(trimToEmpty(connector)) && isNotBlank(description)) {
      final RespondentSource source = new RespondentSource();
      source.setSource(description.trim());
      source.setSourceType(TELEGRAM);
      return source;
    }

    return getStoredSource();
  }

}
