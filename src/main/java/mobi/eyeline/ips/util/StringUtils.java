package mobi.eyeline.ips.util;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 11.04.11
 * Time: 15:58
 */
public class StringUtils {

    static final String HEXES = "0123456789ABCDEF";

    public static String asHex(byte[] raw) {
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

    public static boolean isInteger(String value) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
