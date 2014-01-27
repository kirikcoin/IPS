package mobi.eyeline.ips.util;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 11.04.11
 * Time: 15:58
 */
public class StringUtils {

    static final String HEXES = "0123456789ABCDEF";

    public static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    public static Integer integer(String str) {
        Integer result = null;
        try {
            if (str != null)
                result = new Integer(str);
        } catch (Exception ex) {
            result = -1;
        }
        return result;
    }

  public static String toString(List<Integer> list, String delim){
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      if (i > 0)
          result.append(delim);
       result.append(list.get(i));
    }
    return result.toString();
  }

  public static boolean isInteger(String value){
    boolean result = true;
    try {
      Integer.parseInt(value);
    } catch (NumberFormatException e) {
      result = false;
    }
    return result;
  }
}
