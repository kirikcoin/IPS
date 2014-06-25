package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliverySubscriber;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeliverySubscriberRepository extends BaseRepository<DeliverySubscriber, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DeliverySubscriberRepository.class);

    public DeliverySubscriberRepository(DB db) {
        super(db);
    }

    public void updateState(List<Pair<Integer, DeliverySubscriber.State>> idsAndStates,
                            DeliverySubscriber.State oldState) {

        final Session session = getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            for (Pair<Integer, DeliverySubscriber.State> idAndState : idsAndStates) {
                session.createQuery(
                        "UPDATE DeliverySubscriber" +
                        " SET state = :newState" +
                        " WHERE id = :id AND state = :oldState")
                        .setParameter("newState", idAndState.getValue())
                        .setParameter("oldState", oldState)
                        .setParameter("id", idAndState.getKey())
                        .executeUpdate();
            }

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

        }
    }

    public void updateState(List<Pair<Integer, DeliverySubscriber.State>> idsAndStates) {

        final Session session = getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            for (Pair<Integer, DeliverySubscriber.State> idAndState : idsAndStates) {
                session.createQuery(
                        "UPDATE DeliverySubscriber" +
                        " SET state = :newState" +
                        " WHERE id = :id")
                        .setParameter("newState", idAndState.getValue())
                        .setParameter("id", idAndState.getKey())
                        .executeUpdate();
            }

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

        }
    }

    // TODO: this is untested due to missing `time_to_sec' and `timediff' routines in HSQL.
    public int expire(long expirationDelaySeconds) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            final int count = session.createSQLQuery(
                    "update delivery_subscribers ds" +
                    " set state = 'UNDELIVERED'" +
                    " where" +
                    " ds.state = 'SENT' and time_to_sec(timediff(now(), ds.last_update)) > :diff")
                    .setParameter("diff", expirationDelaySeconds)
                    .executeUpdate();

            transaction.commit();

            getSessionFactory().getCache().evictEntityRegion(DeliverySubscriber.class);

            return count;

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
