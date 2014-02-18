package mobi.eyeline.ips.properties;

import com.eyeline.utils.config.ConfigException;
import com.eyeline.utils.config.xml.XmlConfig;
import com.eyeline.utils.config.xml.XmlConfigSection;

import java.util.Properties;

public interface Config {

    public String getSmtpHost();
    public int getSmtpPort();
    public String getSmtpUsername();
    public String getSmtpPassword();
    public String getMailFrom();
    public String getLoginUrl();

    public Properties getDatabaseProperties();

    public boolean isSmaqUpdateEnabled();
    public int getSmaqUpdateDelayMinutes();

    public static class XmlConfigImpl implements Config {

        private final String smtpHost;
        private final int smtpPort;

        private final String smtpUsername;
        private final String smtpPassword;

        private final String mailFrom;
        private final String loginUrl;

        private final Properties databaseProperties;

        private final boolean smaqUpdateEnabled;
        private final int smaqUpdateDelayMinutes;

        public XmlConfigImpl(XmlConfig xmlConfig) throws ConfigException {

            final XmlConfigSection mail = xmlConfig.getSection("mail");
            {
                smtpHost = mail.getString("smtp.host");
                smtpPort = mail.getInt("smtp.port");
                smtpUsername = mail.getString("smtp.username");
                smtpPassword = mail.getString("smtp.password");
                mailFrom = mail.getString("from");

                loginUrl = mail.getString("login.url");
            }

            final XmlConfigSection database = xmlConfig.getSection("database");
            databaseProperties = database.toProperties(null);

            final XmlConfigSection smaq = xmlConfig.getSection("smaq");
            smaqUpdateEnabled = smaq.getBool("update.enabled", true);
            smaqUpdateDelayMinutes = smaq.getInt("update.delay.minutes");
            // TODO: add auth info.
        }

        public String getSmtpHost() {
            return smtpHost;
        }

        public int getSmtpPort() {
            return smtpPort;
        }

        public String getSmtpUsername() {
            return smtpUsername;
        }

        public String getSmtpPassword() {
            return smtpPassword;
        }

        public String getMailFrom() {
            return mailFrom;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        @Override
        public Properties getDatabaseProperties() {
            return databaseProperties;
        }

        public boolean isSmaqUpdateEnabled() {
            return smaqUpdateEnabled;
        }

        public int getSmaqUpdateDelayMinutes() {
            return smaqUpdateDelayMinutes;
        }
    }
}
