package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.util.RequestParseUtils;

import java.util.Map;

public class OuterRequest {

  private final Map<String, String[]> urlParams;

  public OuterRequest(Map<String, String[]> urlParams) {
    this.urlParams = urlParams;
  }

  public Map<String, String[]> getUrlParams() {
    return urlParams;
  }

  @Override
  public String toString() {
    return RequestParseUtils.toString(urlParams);
  }

  public String getString(String key) throws MissingParameterException {
    return RequestParseUtils.getString(urlParams, key);
  }

  public String getString(String key, String defaultValue) {
    return RequestParseUtils.getString(urlParams, key, defaultValue);
  }

  public int getInt(String key) throws MissingParameterException {
    return RequestParseUtils.getInt(urlParams, key);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return RequestParseUtils.getBoolean(urlParams, key, defaultValue);
  }
}
