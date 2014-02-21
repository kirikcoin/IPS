package mobi.eyeline.ips.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements Validator {

    public static final String PHONE_REGEXP = "^[1-9]\\d{10,}+$";

    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEXP);

    /**
     * @return {@code true} iff considered valid.
     */
    public boolean validate(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }

        final Matcher matcher = PHONE_PATTERN.matcher(phoneNumber);
        return matcher.matches();
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            throw new ValidatorException(new FacesMessage());
        }

        Matcher matcher = PHONE_PATTERN.matcher(String.valueOf(value));
        if (!matcher.matches())
            throw new ValidatorException(new FacesMessage());
    }
}
