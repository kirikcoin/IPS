package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyInvitation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import java.util.List;

public class SurveyInvitationRepository extends BaseRepository<SurveyInvitation, Integer> {

    public SurveyInvitationRepository(DB db) {
        super(db);
    }

    public List<SurveyInvitation> list(String orderColumn,
                                       boolean orderAsc,
                                       int limit,
                                       int offset) {
        final Session session = getSessionFactory().openSession();
        final Criteria criteria = session.createCriteria(SurveyInvitation.class);

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

        return (List<SurveyInvitation>) criteria.list();
    }

    public int count() {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(SurveyInvitation.class);

        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.uniqueResult()).intValue();
    }

    public int count(Survey survey) {
        final Session session = getSessionFactory().getCurrentSession();

        final Number count = (Number) session.createQuery(
                "select count(i.value)" +
                " from SurveyInvitation i" +
                " where i.survey = :survey")
                .setEntity("survey", survey)
                .uniqueResult();
        return count.intValue();
    }
}
