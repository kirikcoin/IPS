package mobi.eyeline.ips.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 12.04.11
 * Time: 10:51
 */
public abstract class PropertiesController {
    private String propertyFile = "";
    private Properties props = null;

    public PropertiesController(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public String loadProperty(String propName) throws IOException {
        String value;
        value = getProperties().getProperty(propName);
        return value;
    }

    public Properties getProperties() throws IOException {
        if (props == null) {
            props = tryLoadProperties(propertyFile);
        }
        return props;
    }

    protected abstract Properties tryLoadProperties(String propertyFile) throws IOException;
}
