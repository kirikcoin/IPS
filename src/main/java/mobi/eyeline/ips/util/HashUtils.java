package mobi.eyeline.ips.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static mobi.eyeline.ips.util.StringUtils.getHex;

public class HashUtils {

    /**
     * Same as {@link #hash(String, String, String)} using
     * default charset and hashing algorithm used for passwords storage.
     *
     * @throws RuntimeException on hashing/encoding issues.
     */
    public static String hashPassword(String value) {
        try {
            return hash(value, "SHA-256", "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException  e) {
            throw new RuntimeException(e);
        }
    }

    public static String hash(String value,
                              String algorithm,
                              String charset)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        final MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(value.getBytes(charset));
        return getHex(md.digest());
    }
}
