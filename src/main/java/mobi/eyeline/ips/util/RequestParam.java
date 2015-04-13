package mobi.eyeline.ips.util;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: Denis Enenko
 * date: 21.01.2014
 */
public class RequestParam {
  private final HttpServletRequest request;

  public static final DateFormat inDF = new SimpleDateFormat("dd-MM-yyyy");

  public static final DateFormat inDFFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
  public static final String CHECKED = "checked=\"checked\"";

  public RequestParam(HttpServletRequest request) {
    this.request = request;
  }

  public static class Value {
    private String param;
    private String val;
    private HttpServletRequest request;

    Value(String param, String val, HttpServletRequest request) {
      this.param = param;
      this.val = val;
      this.request = request;
    }

    public String asString() {
      String result;
      if (val == null)
        result = null;
      else
        result = val;
      return result;
    }

    public Date asDate() {
      Date result;
      if (val == null)
        result = null;
      else {
        try {
          long time = Long.parseLong(val);
          result = new Date(time);
        } catch (NumberFormatException e1) {
          try {
            result = inDFFull.parse(val);
          } catch (ParseException e) {
            result = null;
          }
        }
      }
      return result;
    }

    public Timestamp asTimestamp() {
      Timestamp result;
      if (val == null)
        result = null;
      else
        result = new Timestamp(asDate().getTime());
      return result;
    }

    public List<String> asList() {
      List<String> result = new ArrayList<String>();
      String[] res = request.getParameterValues(param);
      if (res == null)
        res = request.getParameterValues(param.toLowerCase());
      if (res != null) {
        for (String val : res) {
          result.add(val);
        }
      }
      return result;
    }

    public <T extends Enum> List<T> asEnumList(Class<T> clazz) {
      List<T> result = new ArrayList<T>();
      for (String val : asList()) {
        result.add((T) Enum.valueOf(clazz, val));
      }
      return result;
    }

    public boolean asBool() {
      return asBool(false);
    }

    public boolean asBool(boolean def) {
      if (val == null)
        return def;
      else
        return ("TRUE".equalsIgnoreCase(val) || "YES".equalsIgnoreCase(val) || "T".equalsIgnoreCase(val) || "Y".equalsIgnoreCase(val) || "ON".equalsIgnoreCase(val));
    }

    public String asChecked() {
      String result;
      if (asBool())
        result = CHECKED;
      else
        result = "";
      return result;
    }

    public <T extends Enum> T asEnum(T def) {
      T result;
      if (val == null)
        result = def;
      else
        result = (T) Enum.valueOf(def.getClass(), val);
      return result;
    }

    public <T extends Enum> T asEnum(Class<T> clazz) {
      T result;
      if (val == null)
        result = null;
      else
        result = (T) Enum.valueOf(clazz, val);
      return result;
    }

    public boolean isNull() {
      return val == null;
    }

    public Integer asInteger() {
      try {
        return Integer.valueOf(val);
      } catch (NumberFormatException e) {
        return null;
      }
    }

    public Integer asInteger(Integer def) {
      Integer result = asInteger();
      if (result == null)
        result = def;
      return result;
    }

    public boolean exists() {
      return !isNull();
    }

    public Timestamp asTimestamp(Timestamp def) {
      if (isNull())
        return def;
      else
        return asTimestamp();
    }
  }

  public Value get(String param) {
    String result = request.getParameter(param);
    if (result == null)
      result = request.getParameter(param.toLowerCase());
    if (result == null)
      result = request.getParameter(camelizeName(param));
    if (result == null)
      result = request.getParameter("content:" + param);
    return new Value(param, result, request);
  }

  protected static String camelizeName(String name) {
    String res = name.toLowerCase();
    while (res.contains("_")) {
      String tmp = res;
      int idx = tmp.indexOf("_");
      res = tmp.substring(0, idx);
      if (tmp.length() > idx + 1) {
        res = res + tmp.substring(idx + 1, idx + 2).toUpperCase();
      }
      if (tmp.length() > idx + 2) {
        res = res + tmp.substring(idx + 2);
      }
    }
    return res;
  }
}
