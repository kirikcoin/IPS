package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliveryAbonent;
import mobi.eyeline.ips.model.DeliveryAbonentStatus;
import mobi.eyeline.ips.model.InvitationDelivery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliveryAbonentRepository extends BaseRepository<DeliveryAbonent, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryAbonentRepository.class);

    public DeliveryAbonentRepository(DB db) {
        super(db);
    }

    public void updateState(int id, DeliveryAbonentStatus state) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery(
                    "UPDATE DeliveryAbonent" +
                    " SET status = :newState" +
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
                    "UPDATE DeliveryAbonent" +
                    " SET status = :newState" +
                    " WHERE status = :oldState and invitationDelivery = :invitationDelivery")
                    .setParameter("newState", DeliveryAbonentStatus.NEW)
                    .setParameter("oldState", DeliveryAbonentStatus.QUEUED)
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
                    "UPDATE DeliveryAbonent" +
                    " SET status = :newState" +
                    " WHERE status = :oldState")
                    .setParameter("newState", DeliveryAbonentStatus.NEW)
                    .setParameter("oldState", DeliveryAbonentStatus.QUEUED)
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
