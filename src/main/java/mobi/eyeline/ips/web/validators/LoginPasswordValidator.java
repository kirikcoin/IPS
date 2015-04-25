package mobi.eyeline.ips.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator("loginPasswordValidator")
public class LoginPasswordValidator implements Validator {

  public static final String LOGIN_PASSWORD_REGEXP = "^[A-Za-z0-9\\.\\-_]+$";

  private final Pattern pattern = Pattern.compile(LOGIN_PASSWORD_REGEXP);

  @Override
  public void validate(FacesContext context, UIComponent component, Object value)
      throws ValidatorException {

    if (value == null) {
      throw new ValidatorException(new FacesMessage());
    }

    final Matcher m = pattern.matcher(value.toString());
    if (!m.matches()) {
      throw new ValidatorException(new FacesMessage());
    }
  }
}

