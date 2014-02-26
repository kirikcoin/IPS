package mobi.eyeline.ips.web.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator{

    public static final String PHONE_REGEXP = "^[1-9]\\d{10,}+$";

    private final Pattern pattern = Pattern.compile(PHONE_REGEXP);

    /**
     * @return {@code true} iff considered valid.
     */
    public boolean validate(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }

        final Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}