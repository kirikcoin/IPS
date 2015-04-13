package mobi.eyeline.ips.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@FacesValidator("email")
public class EmailValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value)
      throws ValidatorException {

    if (!isValid((value == null) ? null : value.toString())) {
      throw new ValidatorException(new FacesMessage());
    }
  }

  public static boolean isValid(String email) {
    final org.hibernate.validator.internal.constraintvalidators.EmailValidator delegate =
        new org.hibernate.validator.internal.constraintvalidators.EmailValidator();

    return isNotEmpty(email) && delegate.isValid(email, null);
  }
}
