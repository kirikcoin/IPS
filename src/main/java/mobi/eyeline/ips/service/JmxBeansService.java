package mobi.eyeline.ips.service;

import com.j256.simplejmx.server.JmxServer;
import mobi.eyeline.ips.properties.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMException;

public class JmxBeansService {

    private static final Logger logger = LoggerFactory.getLogger(JmxBeansService.class);

    private static JmxBeansService instance;

    @SuppressWarnings("FieldCanBeLocal")
    private JmxServer jmxServer;


    public static synchronized void initialize(Config config) {
        if (instance != null) {
            throw new AssertionError("Instance is already initialized");
        }

        instance = new JmxBeansService(config);
    }

    private JmxBeansService(Config config) {
        if (!config.getExposeJmxBeans()) {
            logger.info("JMX disabled");
            return;
        }

        jmxServer = new JmxServer(true);

        final Services services = Services.instance();
        try {
            jmxServer.register(services.getDeliveryService());
            jmxServer.register(services.getNotificationService());

        } catch (JMException e) {
            logger.error("JMX initialization failed", e);
        }
    }
}
