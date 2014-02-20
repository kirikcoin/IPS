package mobi.eyeline.ips.util;

import mobi.eyeline.ips.messages.MissingParameterException;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Utilities for {@code String -> String[]} maps handing.
 */
public class RequestParseUtils {

    public static int getInt(Map<String, String[]> map, String key)
            throws MissingParameterException {

        try {
            return Integer.parseInt(map.get(key)[0]);
        } catch (Exception e) {
            throw new MissingParameterException(key);
        }
    }

    /**
     * @return {@code false} by default.
     */
    public static boolean getBoolean(Map<String, String[]> map, String key)
            throws MissingParameterException {

        try {
            return BooleanUtils.toBoolean(map.get(key)[0]);
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
