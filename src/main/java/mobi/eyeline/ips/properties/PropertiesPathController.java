package mobi.eyeline.ips.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesPathController extends PropertiesController {

    private final String path;

    public PropertiesPathController(String path, String propertyFile) {
        super(propertyFile);
        this.path = path;
    }

    protected Properties tryLoadProperties(String propertyFile) throws IOException {
        final InputStream inputStream =
                new FileInputStream(new File(path, propertyFile));

        try {
            final Properties props = new Properties();
            props.load(inputStream);
            return props;

        } finally {
            inputStream.close();
        }
    }

}
