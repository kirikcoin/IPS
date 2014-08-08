package mobi.eyeline.ips.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseRepository<E, K extends Serializable> {

    private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    private final DB db;
    protected Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public BaseRepository(DB db) {
        this.db = db;
        this.entityClass = getEntityClass();
    }

    protected Class<E> getEntityClass() {
        final ParameterizedType genericSuperclass =
                (ParameterizedType) getClass().getGenericSuperclass();
        //noinspection unchecked
        return (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

    protected SessionFactory getSessionFactory() {
        return db.getSessionFactory();
    }

    /**
     * @return {@code null} if nothing found.
     */
    public E get(K id) {
        final Session session = getSessionFactory().openSession();
        try {
            //noinspection unchecked
            return (E) session.get(entityClass, id);

        } finally {
            session.close();
        }
    }

    /**
     * Fails if nothing found.
     */
    public E load(K id) {
        final Session session = getSessionFactory().openSession();
        try {
            //noinspection unchecked
            return (E) session.load(entityClass, id);

        } finally {
            session.close();
        }
    }

    public List<E> load(List<K> ids) {
        final Session session = getSessionFactory().openSession();
        try {
            final List<E> results = new ArrayList<>(ids.size());
            for (K id : ids) {
                //noinspection unchecked
                results.add((E) session.load(entityClass, id));
            }

            return results;

        } finally {
            session.close();
        }
    }

    public K save(E entity) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            //noinspection unchecked
            K id = (K) session.save(entity);

            transaction.commit();
            return id;

        } catch (HibernateException e) {
            if ((transaction != null) && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (HibernateException ee) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public void update(E entity) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.update(entity);

            transaction.commit();

        } catch (HibernateException e) {

            if ((transaction != null) && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (HibernateException ee) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public void saveOrUpdate(E entity) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();

        } catch (HibernateException e) {
            if ((transaction != null) && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (HibernateException ee) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public List<E> list() {
        final Session session = getSessionFactory().openSession();
        try {
            //noinspection unchecked
            return session.createCriteria(entityClass).list();

        } finally {
            session.close();
        }
    }

    public void refresh(E entity) {
        final Session session = getSessionFactory().openSession();
        try {
            session.refresh(entity);

        } finally {
            session.close();
        }
    }

    public void delete(E entity) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();

        } catch (HibernateException e) {
            if ((transaction != null) && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (HibernateException ee) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw e;

        } finally {
            session.close();
        }
    }

}
