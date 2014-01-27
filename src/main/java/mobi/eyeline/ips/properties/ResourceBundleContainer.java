package mobi.eyeline.ips.properties;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 5/28/11
 * Time: 5:50 PM
 */
public class ResourceBundleContainer {
    private ResourceBundle _resourceBundle;

    public ResourceBundleContainer(String name, Locale locale) {
        _resourceBundle = ResourceBundle.getBundle(name, locale);
    }

    public String getString(String key) throws UnsupportedEncodingException {
        String value;
        value = new String(_resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        return value;
    }

    public String getCleanString(String key)  {
      try {
        return getString(key);
      } catch (UnsupportedEncodingException e) {
        return "Unsupported encoding " + e.getMessage();
      }
    }
}
