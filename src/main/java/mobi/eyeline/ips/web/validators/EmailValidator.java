package mobi.eyeline.ips.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: dizan
 * Date: 30.01.14
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class EmailValidator implements Validator {
    private Pattern emailPattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,4}$");
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Matcher m = emailPattern.matcher(String.valueOf(value));
        if(!m.matches()) throw new ValidatorException(new FacesMessage());
    }
}
