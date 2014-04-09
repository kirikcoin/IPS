package mobi.eyeline.ips.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HashUtils {

    /**
     * Same as {@link #hash(String, String, String)} using
     * default charset and hashing algorithm used for passwords storage.
     *
     * @throws RuntimeException on hashing/encoding issues.
     */
    static String hashPassword(String value) {
        try {
            return hash(value, "SHA-256", "UTF-8")
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException  e) {
            throw new RuntimeException(e)
        }
    }

    static String hash(String value,
                       String algorithm,
                       String charset)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest.getInstance(algorithm).with {
            update(value.getBytes(charset))
            return digest().encodeHex().toString().toUpperCase()
        }
    }
}
