package mobi.eyeline.ips.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LatinValidator implements Validator  {
    private Pattern pattern = Pattern.compile("^[A-Za-z0-9\\.\\-\\_]+$");
//    public static void main(String[] args) {
//        Pattern emailPattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,4}$");
//        Pattern emailPattern2 = Pattern.compile("\\p{L}*+\\.*+(\\p{L}+)*+\\d*");
//        Pattern loginPattern = Pattern.compile("^[A-Za-z0-9\\.\\-\\_]+$");
//        Matcher m = loginPattern.matcher(String.valueOf("sdfdsd.fg23423f asd."));
//        if(!m.matches()) throw new ValidatorException(new FacesMessage());
//    }

//    public boolean validate(String value) throws ValidatorException {
//        Matcher m = pattern.matcher(String.valueOf(value));
//        if(!m.matches()) return false;
//        return true;
//    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Matcher m = pattern.matcher(String.valueOf(value));
        if(!m.matches())  throw new ValidatorException(new FacesMessage());
    }
}

