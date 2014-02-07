package mobi.eyeline.ips.messages;

import java.util.Arrays;
import java.util.Map;

public class ParseUtils {

    public static int getInt(Map<String, String[]> map, String key)
            throws MissingParameterException {

        try {
            return Integer.parseInt(map.get(key)[0]);
        } catch (Exception e) {
            throw new MissingParameterException(key);
        }
    }

    public static String getString(Map<String, String[]> map, String key)
            throws MissingParameterException {

        final String value = map.get(key)[0];
        if (value == null) {
            throw new MissingParameterException(key);
        }

        return value;
    }

    public static String toString(Map<String, String[]> map) {
        final StringBuilder result = new StringBuilder();

        result.append("{");
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            result
                    .append("\"")
                    .append(entry.getKey())
                    .append("\"=")
                    .append(Arrays.toString(entry.getValue()))
                    .append(", ");
        }
        result.append("}");

        return result.toString();
    }
}
