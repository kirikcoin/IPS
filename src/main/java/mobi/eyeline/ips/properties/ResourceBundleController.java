package mobi.eyeline.ips.properties;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 26.05.11
 * Time: 10:58
  */
public class ResourceBundleController {
    private String propertyFile = "";

    public ResourceBundleController(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String loadProperty(String propName, Locale locale) throws IOException {
        String value;
        value = new String(getBundle(locale).getString(propName).getBytes("ISO-8859-1"), "UTF-8");
        return value;
    }

    public ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(propertyFile, locale);
    }

}
