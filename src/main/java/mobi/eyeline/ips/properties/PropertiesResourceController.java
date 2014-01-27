package mobi.eyeline.ips.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesResourceController extends PropertiesController {

    public PropertiesResourceController(String propertyFile) {
        super(propertyFile);
    }

    protected Properties tryLoadProperties(String propertyFile) throws IOException {
        final InputStream inputStream =
                PropertiesController.class.getResourceAsStream("/" + propertyFile);
        if (inputStream == null) {
            throw new IOException("Resource " + propertyFile + " cannot be found");
        }

        try {
            final Properties props = new Properties();
            props.load(inputStream);
            return props;

        } finally {
            inputStream.close();
        }
    }

}
