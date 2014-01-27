package mobi.eyeline.ips.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 18.04.11
 * Time: 13:13
 */
public interface Config {

    public Properties loadProperties(String fileName);

    public Properties getAppProperties();

    public String limeSurveyHome() throws IOException;

    public String ussdPushUrl() throws IOException;

    public boolean isProduction() throws IOException;

    public String ussdPushService() throws IOException;

    public int threadCountPostPush() throws IOException;

    public String loadProperty(String name);

    public int loadInteger(String name);

    public static class ConfigImpl implements Config {
        private static Config _instance = null;

        protected PropertiesController props;

        private static final String PROPERTIES_FILE_NAME    = "config.properties";
        private static final String PROPERTIES_PARAM_NAME   = "ips.config.dir";

        protected ConfigImpl() {
            props = loadPropertiesController(PROPERTIES_FILE_NAME);
        }

        private PropertiesController loadPropertiesController(String fileName) {
            final String configDirName = System.getProperty(PROPERTIES_PARAM_NAME);
            if (configDirName != null) {
                try {
                    final PropertiesController props =
                            new PropertiesPathController(configDirName, fileName);
                    props.getProperties();
                    return props;

                } catch (IOException ignored) {}
            }

            return new PropertiesResourceController(fileName);
        }

        public Properties loadProperties(String fileName) {
            try {
                return loadPropertiesController(fileName).getProperties();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Properties getAppProperties() {
            return loadProperties(PROPERTIES_FILE_NAME);
        }

        public static Config instance() {
            if (_instance == null)
                _instance = new ConfigImpl();
            return _instance;
        }

        public String limeSurveyHome() throws IOException {
            if (isProduction())
                return loadProperty("limeSurveyHome");
            else
                return loadProperty("limeSurveyHome.debug");
        }

        public String ussdPushUrl() throws IOException {
            if (isProduction())
                return loadProperty("ussdPushUrl");
            else
                return loadProperty("ussdPushUrl.debug");
        }

        public boolean isProduction() throws IOException {
            return "production".equalsIgnoreCase(loadProperty("isProduction"));
        }

        public String ussdPushService() throws IOException {
            return loadProperty("ussdPushService");
        }

        public int threadCountPostPush() throws IOException {
            return Integer.parseInt(loadProperty("threadCountPostPush"));
        }

        public String loadProperty(String name) {
            try {
                return props.loadProperty(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int loadInteger(String name) {
            try {
                return Integer.valueOf(props.loadProperty(name));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
