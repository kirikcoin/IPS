package mobi.eyeline.ips.web;

import mobi.eyeline.ips.properties.Config;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.Properties;


public class InitListener implements ServletContextListener
{
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false"); //Если закомментировать эту строку, то inputText при пустом значении начнет передавать в сеттеры 0, а не NULL (http://stackoverflow.com/questions/5225013/coerce-to-zero-at-runtime)

        File configDir = getConfigDir();
        if(!configDir.exists()) {
            throw new RuntimeException("Config directory '" + configDir.getAbsolutePath() + "' does not exists");
        }

        initLog4j(configDir);

        initJaasAuthorization(servletContextEvent);
    }

    private void initJaasAuthorization(ServletContextEvent servletContextEvent) {
        if(System.getProperty("java.security.auth.login.config") == null) {
            System.setProperty("java.security.auth.login.config", servletContextEvent.getServletContext().getRealPath("WEB-INF/jaas.config"));
        }
    }

    private File getConfigDir() {
        String configDir = System.getProperty("ips.config.dir");
        if(configDir == null) {
            configDir = "conf";
            System.err.println("System property 'ips.config.dir' is not set. Use default value: " + configDir);
        }
        return new File(configDir);
    }

    private void initLog4j(File configDir) {
        final Properties properties =
                Config.ConfigImpl.instance().loadProperties("log4j.properties");
        PropertyConfigurator.configure(properties);

        File log4jprops = new File(configDir, "log4j.properties");
        System.out.println("Log4j conf file: " + log4jprops.getAbsolutePath() + " " + log4jprops.exists());
        PropertyConfigurator.configureAndWatch(log4jprops.getAbsolutePath(), 60000L);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
