package mobi.eyeline.ips.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionFactoryImpl;

import java.util.Properties;

public class DB {
    private final SessionFactory sessionFactory;

    static final String LIKE_ESCAPE_CHARACTER = "\\";

    public DB(Properties properties) {
        final Configuration configuration = new Configuration()
                .configure("/hibernate-model.cfg.xml")
                .configure()
                .addProperties(properties);
        //noinspection deprecation
        sessionFactory = configuration.buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    static String getEscapeExpression(SessionFactoryImplementor factory) {
        final Dialect dialect = factory.getDialect();
        if (dialect instanceof HSQLDialect) {
            return "ESCAPE '\\'";
        } else {
            return "ESCAPE '\\\\'";
        }
    }
}
