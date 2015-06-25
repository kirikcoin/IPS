package mobi.eyeline.ips.util;

import mobi.eyeline.ips.messages.MissingParameterException;
import org.apache.commons.lang3.BooleanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestParseUtils {

  public static int getInt(Map<String, String[]> map, String key)
      throws MissingParameterException {

    try {
      return Integer.parseInt(map.get(key)[0]);
    } catch (Exception e) {
      throw new MissingParameterException(key);
    }
  }

  public static int getInt(Map<String, String[]> map, String key, Integer defaultValue) {

    try {
      return getInt(map, key);
    } catch (MissingParameterException e) {
      return defaultValue;
    }
  }

  /**
   * Treats {@code key} as a required parameter.
   *
   * @see #getBoolean(java.util.Map, String, boolean)
   */
  public static boolean getBoolean(Map<String, String[]> map, String key)
      throws MissingParameterException {

    try {
      return BooleanUtils.toBoolean(map.get(key)[0]);
    } catch (Exception e) {
      throw new MissingParameterException(key);
    }
  }

  /**
   * @return {@code defaultValue} if {@code key} is missing.
   */
  public static boolean getBoolean(Map<String, String[]> map,
                                   String key,
                                   boolean defaultValue) {

    try {
      return getBoolean(map, key);
    } catch (MissingParameterException e) {
      return defaultValue;
    }
  }

  public static String getString(Map<String, String[]> map,
                                 String key,
                                 String defaultValue) {
    try {
      return getString(map, key);
    } catch (MissingParameterException e) {
      return defaultValue;
    }
  }

  public static String getString(Map<String, String[]> map, String key)
      throws MissingParameterException {

    final String[] values = map.get(key);
    if ((values != null) && (values.length > 0) && (values[0] != null)) {
      return values[0];
    }

    throw new MissingParameterException(key);
  }

  public static String toString(Map<String, String[]> map) {
    final StringBuilder result = new StringBuilder();

    result.append("{");
    for (Iterator<Map.Entry<String, String[]>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
      final Map.Entry<String, String[]> entry = iterator.next();
      result
          .append("\"")
          .append(entry.getKey())
          .append("\"=")
          .append(Arrays.toString(entry.getValue()));
      if (iterator.hasNext()) {
        result.append(", ");
      }
    }
    result.append("}");

    return result.toString();
  }

  public static String toQueryString(Map<String, String[]> map) {
    if (map.isEmpty()) {
      return "";
    }

    final StringBuilder buf = new StringBuilder();

    buf.append("?");

    for (Iterator<Map.Entry<String, String[]>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
      final Map.Entry<String, String[]> entry = iterator.next();

      final String[] value = entry.getValue();
      for (int i = 0; i < value.length; i++) {
        final String val = value[i];
        buf.append(entry.getKey()).append("=").append(val);
        if (i != value.length - 1) {
          buf.append("&");
        }
      }

      if (iterator.hasNext()) {
        buf.append("&");
      }
    }

    return buf.toString();
  }

  public static Map<String, String> getHeaders(final HttpServletRequest request) {
    return new LinkedHashMap<String, String>() {{
      final Enumeration<String> names = request.getHeaderNames();
      while (names.hasMoreElements()) {
        final String name = names.nextElement();
        put(name, request.getHeader(name));
      }
    }};
  }

  public static Map<String, Object> getSessionAttributes(final HttpSession session) {
    return new LinkedHashMap<String, Object>() {{
      final Enumeration<String> names = session.getAttributeNames();
      while (names.hasMoreElements()) {
        final String name = names.nextElement();
        put(name, session.getAttribute(name));
      }
    }};
  }
}
