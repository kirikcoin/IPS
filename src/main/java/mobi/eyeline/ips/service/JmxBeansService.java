package mobi.eyeline.ips.service;

import com.j256.simplejmx.server.JmxServer;
import mobi.eyeline.ips.properties.Config;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
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
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class JmxBeansService {

  private static final Logger logger = LoggerFactory.getLogger(JmxBeansService.class);

  private static JmxBeansService instance;

  private JmxServer jmxServer;


  public static synchronized void initialize(Config config, Services services) {
    if (instance != null) {
      throw new AssertionError("Instance is already initialized");
    }

    instance = new JmxBeansService(config, services);
  }

  private JmxBeansService(Config config, Services services) {
    if (!config.isJmxEnabled()) {
      logger.info("JMX disabled");
      return;
    }

    // This effectively uses the same port for RMI registry and RMI server.
    jmxServer = new JmxServer(config.getJmxPort());

    // Handle callbacks for NATs, host parameter is optional.
    if (config.getJmxHost() != null) {
      try {
        jmxServer.setInetAddress(Inet4Address.getByName(config.getJmxHost()));
      } catch (UnknownHostException e) {
        throw new RuntimeException(e);
      }
    }

    try {
      // Start own MBean server bypassing the one (possibly) created by JVM.
      jmxServer.start();
      jmxServer.register(services.getDeliveryService());
      jmxServer.register(services.getNotificationService());
      jmxServer.register(services.getEsdpService());

    } catch (JMException e) {
      logger.error("JMX initialization failed", e);
    }

    try {
      initHibernate(services.getDb().getSessionFactory());
    } catch (InstanceAlreadyExistsException |
        MBeanRegistrationException |
        MalformedObjectNameException |
        NotCompliantMBeanException e) {
      logger.error("Hibernate JMX error", e);
    }

    initEhcache();
  }

  public static JmxBeansService getInstance() {
    return instance;
  }

  public void stop() {
    if (jmxServer != null) {
      jmxServer.stop();
    }
  }

  private void initHibernate(SessionFactory sessionFactory)
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
        new Class<?>[]{StatisticsMXBean.class},
        handler);

    mbeanServer.registerMBean(statisticsMBean, statsName);
  }

  private void initEhcache() {
    final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

    final CacheManager manager = CacheManager.getInstance();
    ManagementService.registerMBeans(manager, mbeanServer, true, true, false, true);
  }

  @MXBean
  public interface StatisticsMXBean extends Statistics {
  }
}
