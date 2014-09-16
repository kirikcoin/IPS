package mobi.eyeline.ips.web.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator {
    public static final String IMAGE_REGEXP = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    /**
     * @return {@code true} iff considered valid.
     */
    public static boolean validate(String imageName) {
        if (imageName == null) {
            return false;
        }
        final Pattern pattern = Pattern.compile(IMAGE_REGEXP);
        final Matcher matcher = pattern.matcher(imageName);
        return matcher.matches();
    }
}
