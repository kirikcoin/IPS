package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliverySubscriberRepository extends BaseRepository<DeliverySubscriber, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DeliverySubscriberRepository.class);

    public DeliverySubscriberRepository(DB db) {
        super(db);
    }

    public void updateState(int id, DeliverySubscriber.State state) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery(
                    "UPDATE DeliverySubscriber" +
                    " SET state = :newState" +
                    " WHERE id = :id")
                    .setParameter("newState", state)
                    .setParameter("id", id)
                    .executeUpdate();

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

    public void clearQueued(InvitationDelivery invitationDelivery) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery(
                    "UPDATE DeliverySubscriber" +
                    " SET state = :newState" +
                    " WHERE state = :oldState and invitationDelivery = :invitationDelivery")
                    .setParameter("newState", DeliverySubscriber.State.NEW)
                    .setParameter("oldState", DeliverySubscriber.State.QUEUED)
                    .setParameter("invitationDelivery", invitationDelivery)
                    .executeUpdate();

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

    public void clearQueued() {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery(
                    "UPDATE DeliverySubscriber" +
                    " SET state = :newState" +
                    " WHERE state = :oldState")
                    .setParameter("newState", DeliverySubscriber.State.NEW)
                    .setParameter("oldState", DeliverySubscriber.State.QUEUED)
                    .executeUpdate();

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
