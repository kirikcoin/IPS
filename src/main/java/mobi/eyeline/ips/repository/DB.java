package mobi.eyeline.ips.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

import java.sql.Connection;
import java.util.Properties;

public class DB {
    private final SessionFactory sessionFactory;

    public DB(Properties properties) {
        final Configuration configuration = new Configuration()
                .configure("/hibernate-model.cfg.xml")
                .configure()
                .addProperties(properties);
        sessionFactory = configuration.buildSessionFactory();
    }

    public Connection getConnection() {
        return ((SessionImpl) sessionFactory.openSession()).connection();
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
