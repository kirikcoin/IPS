package mobi.eyeline.ips.util;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class StringUtils {

  public static List<String> split(String data, String separators) {
    if (data == null) {
      return null;
    }

    final ArrayList<String> parts = new ArrayList<String>();
    for (String part : data.split(separators)) {
      if (org.apache.commons.lang3.StringUtils.isNotBlank(part)) {
        parts.add(part.trim());
      }
    }

    return unmodifiableList(parts);
  }

  public static List<String> split(String data) {
    return split(data, ",|(\\s+)");
  }

}
