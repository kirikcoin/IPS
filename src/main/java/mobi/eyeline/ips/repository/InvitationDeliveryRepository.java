package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliveryAbonent;
import mobi.eyeline.ips.model.DeliveryAbonentStatus;
import mobi.eyeline.ips.model.InvitationDelivery;

import mobi.eyeline.ips.model.Survey;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

        criteria.add(Restrictions.eq("survey", survey));
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
        return (List<InvitationDelivery>) criteria.list();
    }

    public int count(Survey survey) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(InvitationDelivery.class);

        criteria.add(Restrictions.eq("survey", survey));
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
                DeliveryAbonent abonent= new DeliveryAbonent();
                abonent.setMsisdn(msisdn);
                abonent.setInvitationDelivery(delivery);
                abonent.setStatus(DeliveryAbonentStatus.NEW);

                session.save(delivery);
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

}
