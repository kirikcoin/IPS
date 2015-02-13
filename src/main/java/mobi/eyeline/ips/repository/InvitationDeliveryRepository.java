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
import org.hibernate.jdbc.AbstractWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.FETCHED;
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

    public void saveWithSubscribers(final InvitationDelivery delivery,
                                    final List<String> msisdns) {

        if (logger.isDebugEnabled()) {
            logger.debug("InvitationDeliveryRepository.saveWithSubscribers:" +
                    " deliveryId = [" + delivery.getId() + "]," +
                    " size = [" + msisdns.size() + "]");
        }

        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.save(delivery);

            session.doWork(new AbstractWork() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    final PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO delivery_subscribers (delivery_id, msisdn)" +
                            " VALUES (?, ?)");

                    int i = 0;
                    for (String msisdn : msisdns) {
                        i++;

                        stmt.setInt(1, delivery.getId());
                        stmt.setString(2, msisdn);
                        stmt.addBatch();

                        if (i % 100 == 0) {
                            stmt.executeBatch();
                        }
                    }
                    stmt.executeBatch();
                }
            });

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

            // Get next chunk of messages.
            final List<DeliverySubscriber> results;
            {
                final Criteria criteria = session.createCriteria(DeliverySubscriber.class);
                criteria.add(
                        and(
                                eq("invitationDelivery", delivery),
                                eq("state", DeliverySubscriber.State.NEW)));
                criteria.setMaxResults(limit);

                //noinspection unchecked
                results = (List<DeliverySubscriber>) criteria.list();
            }

            // change state to FETCHED for all results
            if (!results.isEmpty()) {
                for(DeliverySubscriber subscriber:results){
                    subscriber.setState(FETCHED);
                    session.update(subscriber);
                }
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

        }  finally {
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

    public int countSent(Survey survey) {
        final Session session = getSessionFactory().getCurrentSession();

        return ((Number) session.createQuery(
                "select count(d)" +
                " from DeliverySubscriber d" +
                " where (d.state = :state) and (d.invitationDelivery.survey = :survey)")
                .setParameter("state", DeliverySubscriber.State.DELIVERED)
                .setEntity("survey", survey)
                .uniqueResult()).intValue();
    }
}