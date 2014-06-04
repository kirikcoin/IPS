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

    public void updateState(int id,
                            DeliverySubscriber.State newState,
                            DeliverySubscriber.State oldState) {

        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery(
                    "UPDATE DeliverySubscriber" +
                            " SET state = :newState" +
                            " WHERE id = :id AND state = :oldState")
                    .setParameter("newState", newState)
                    .setParameter("id", id)
                    .setParameter("oldState", oldState)
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
