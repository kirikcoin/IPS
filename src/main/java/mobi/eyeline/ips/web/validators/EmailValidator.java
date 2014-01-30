package mobi.eyeline.ips.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: dizan
 * Date: 30.01.14
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {

        final org.hibernate.validator.internal.constraintvalidators.EmailValidator delegate =
                new org.hibernate.validator.internal.constraintvalidators.EmailValidator();

        final String stringValue = String.valueOf(value);
        if (isEmpty(stringValue) || !delegate.isValid(stringValue, null)) {
            throw new ValidatorException(new FacesMessage());
        }
    }
}
