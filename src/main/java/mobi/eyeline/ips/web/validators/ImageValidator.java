package mobi.eyeline.ips.web.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator {

    private static final String IMAGE_REGEXP = "[^\\s]+(\\.(?i)(jpg|jpeg|png))$";

    private final Pattern pattern = Pattern.compile(IMAGE_REGEXP);

    /**
     * @return {@code true} iff considered valid.
     */
    public boolean validate(String imageName) {
        if (imageName == null) {
            return false;
        }

        final Matcher matcher = pattern.matcher(imageName);
        return matcher.matches();
    }
}
