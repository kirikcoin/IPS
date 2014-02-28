package mobi.eyeline.ips.util;

import mobi.eyeline.ips.messages.MissingParameterException;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Arrays;
import java.util.Iterator;
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
    public static boolean getBoolean(Map<String, String[]> map, String key, boolean defaultValue)
            throws MissingParameterException {

        try {
            return getBoolean(map, key);
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
}
