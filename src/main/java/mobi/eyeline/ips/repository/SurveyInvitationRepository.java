package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyInvitation;
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

public class SurveyInvitationRepository extends BaseRepository<SurveyInvitation, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(SurveyInvitationRepository.class);

    public SurveyInvitationRepository(DB db) {
        super(db);
    }

    public List<SurveyInvitation> list(Survey survey,
                                       String orderColumn,
                                       boolean orderAsc,
                                       int limit,
                                       int offset) {
        final Session session = getSessionFactory().openSession();
        final Criteria criteria = session.createCriteria(SurveyInvitation.class);

        criteria.add(Restrictions.eq("survey", survey));
        criteria.setFirstResult(offset).setMaxResults(limit);
        if(orderColumn != null) {
            final String property;
            switch (orderColumn) {
                case "date":         property = "date";        break;
                case "number":       property = "value";         break;
                default:
                    throw new RuntimeException("Unexpected sort column: " + orderColumn);
            }

            criteria.addOrder(orderAsc ? Order.asc(property) : Order.desc(property));
        }

        //noinspection unchecked
        return (List<SurveyInvitation>) criteria.list();
    }

    public int count(Survey survey) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(SurveyInvitation.class);

        criteria.add(Restrictions.eq("survey", survey));
        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.uniqueResult()).intValue();
    }
}