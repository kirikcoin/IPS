package mobi.eyeline.ips.web;

import com.eyeline.utils.config.ConfigException;
import com.eyeline.utils.config.xml.XmlConfig;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.service.JmxBeansService;
import mobi.eyeline.ips.service.Services;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.concurrent.TimeUnit;


public class InitListener implements ServletContextListener {

    private static final String PROPERTY_CONFIG_DIR             = "ips.config.dir";
    private static final String DEFAULT_CONFIG_DIR              = "conf";
    private static final String PROPERTIES_FILE_NAME            = "config.xml";

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        // XXX: Если закомментировать эту строку, то inputText при пустом значении
        // начнет передавать в сеттеры 0, а не NULL.
        // (http://stackoverflow.com/questions/5225013/coerce-to-zero-at-runtime)
        System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");

        final File configDir = getConfigDir();
        if (!configDir.exists()) {
            throw new RuntimeException(
                    "Config directory '" + configDir.getAbsolutePath() + "' does not exist");
        } else {
            System.out.println("Using properties directory '" + configDir.getAbsolutePath() + "'");
        }

        initLog4j(configDir);

        final Config config = initProperties(configDir);
        Services.initialize(config);

        initJaasAuthorization(servletContextEvent);

        Services.instance().getMadvUpdateService().start();
        Services.instance().getDeliveryService().start();
        Services.instance().getNotificationService().start();

        JmxBeansService.initialize(config);
    }

    private Config initProperties(File configDir) {
        final File cfgFile = new File(configDir, PROPERTIES_FILE_NAME);

        try {
            final XmlConfig xmlConfig = new XmlConfig();
            xmlConfig.load(cfgFile);
            return new Config.XmlConfigImpl(xmlConfig);

        } catch (ConfigException e) {
            throw new RuntimeException("Error reading properties from " +
                    cfgFile.getAbsolutePath(), e);
        }
    }

    private void initJaasAuthorization(ServletContextEvent servletContextEvent) {
        if (System.getProperty("java.security.auth.login.config") == null) {
            System.setProperty("java.security.auth.login.config",
                    servletContextEvent.getServletContext().getRealPath("WEB-INF/jaas.config"));
        }
    }

    private File getConfigDir() {
        String configDir = System.getProperty(PROPERTY_CONFIG_DIR);
        if (configDir == null) {
            configDir = DEFAULT_CONFIG_DIR;
            System.err.println("System property '" + PROPERTY_CONFIG_DIR + "' is not set." +
                    " Using default value: " + configDir);
        }
        return new File(configDir);
    }

    private void initLog4j(File configDir) {
        final File log4jprops = new File(configDir, "log4j.properties");
        System.out.println("Log4j conf file: " + log4jprops.getAbsolutePath() +
                ", exists: " + log4jprops.exists());

        PropertyConfigurator.configureAndWatch(
                log4jprops.getAbsolutePath(),
                TimeUnit.MINUTES.toMillis(1));
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            Services.instance().getDeliveryService().stop();
            Services.instance().getNotificationService().stop();
        } catch (InterruptedException ignored) {}
    }
}
