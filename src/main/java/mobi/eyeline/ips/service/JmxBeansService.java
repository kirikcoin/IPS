package mobi.eyeline.ips.service;

import com.j256.simplejmx.server.JmxServer;
import mobi.eyeline.ips.properties.Config;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MXBean;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
            jmxServer.register(services.getEsdpService());

        } catch (JMException e) {
            logger.error("JMX initialization failed", e);
        }

        try {
            initStatistics(Services.instance().getDb().getSessionFactory());
        } catch (InstanceAlreadyExistsException |
                MBeanRegistrationException |
                MalformedObjectNameException |
                NotCompliantMBeanException e) {
            logger.error("Hibernate JMX error", e);
        }
    }

    @MXBean
    public static interface StatisticsMXBean extends Statistics {
    }

    public void initStatistics(SessionFactory sessionFactory)
            throws NotCompliantMBeanException, InstanceAlreadyExistsException,
            MBeanRegistrationException, MalformedObjectNameException {

        final ObjectName statsName = new ObjectName("org.hibernate:type=statistics");
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

        final Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);

        final InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(statistics, args);
            }
        };

        final Object statisticsMBean = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] { StatisticsMXBean.class },
                handler);

        mbeanServer.registerMBean(statisticsMBean, statsName);
    }
}
