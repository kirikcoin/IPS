package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.gt;

public class InvitationDeliveryRepository extends BaseRepository<InvitationDelivery, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(InvitationDeliveryRepository.class);

    public InvitationDeliveryRepository(DB db) {
        super(db);
    }

    public List<InvitationDelivery> list(Survey survey,
                                         String orderColumn,
                                         boolean orderAsc,
                                         int limit,
                                         int offset) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(InvitationDelivery.class);

        criteria.add(eq("survey", survey));
        criteria.setFirstResult(offset).setMaxResults(limit);
        if (orderColumn != null) {
            final String property;
            switch (orderColumn) {
                case "date":         property = "date";          break;
                case "type":         property = "type";          break;
                case "speed":        property = "speed";         break;
                case "errorsCount":  property = "errorsCount";   break;
                default:
                    throw new RuntimeException("Unexpected sort column: " + orderColumn);
            }

            criteria.addOrder(orderAsc ? Order.asc(property) : Order.desc(property));
        }
        //noinspection unchecked
        return (List<InvitationDelivery>) criteria.list();
    }

    public int count(Survey survey) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(InvitationDelivery.class);

        criteria.add(eq("survey", survey));
        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.uniqueResult()).intValue();
    }

    public void save(InvitationDelivery delivery, List<String> msisdns){
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.save(delivery);
            for(String msisdn:msisdns){
                final DeliverySubscriber subscriber = new DeliverySubscriber();
                subscriber.setMsisdn(msisdn);
                subscriber.setInvitationDelivery(delivery);

                session.save(subscriber);
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

        } finally {
            session.close();
        }
    }

    public List<DeliverySubscriber> fetchNext(InvitationDelivery delivery,
                                              int limit) {

        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Update current position as passed in entity might not be attached or out of date.
            final int currentPos = ((Number) session.createQuery(
                    "SELECT d.currentPosition" +
                    " FROM InvitationDelivery d" +
                    " WHERE d = :delivery")
                    .setEntity("delivery", delivery)
                    .uniqueResult()).intValue();

            // Get next chunk of messages.
            final List<DeliverySubscriber> results;
            {
                final Criteria criteria = session.createCriteria(DeliverySubscriber.class);
                criteria.add(
                        and(
                                eq("invitationDelivery", delivery),
                                eq("state", DeliverySubscriber.State.NEW),
                                gt("id", currentPos)));
                criteria.setMaxResults(limit);

                //noinspection unchecked
                results = (List<DeliverySubscriber>) criteria.list();
            }

            if (!results.isEmpty()) {
                // Set current position to the maximal ID
                final int maxId = Collections.max(results, DeliverySubscriber.ID_COMPARATOR).getId();
                session.createQuery(
                        "UPDATE InvitationDelivery" +
                        " SET currentPosition = :maxId" +
                        " WHERE id = :id")
                        .setParameter("maxId", maxId)
                        .setParameter("id", delivery.getId())
                        .executeUpdate();
            }

            transaction.commit();

            return results;

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

    public List<InvitationDelivery> list(InvitationDelivery.State state) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            final Criteria criteria = session.createCriteria(InvitationDelivery.class);
            criteria.add(eq("state", state));

            //noinspection unchecked
            return (List<InvitationDelivery>) criteria.list();

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