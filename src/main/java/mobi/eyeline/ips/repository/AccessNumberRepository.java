package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.AccessNumber;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.or;

public class AccessNumberRepository extends BaseRepository<AccessNumber, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AccessNumberRepository.class);

    public AccessNumberRepository(DB db) {
        super(db);
    }

    public List<AccessNumber> list(String filter,
                                   String orderColumn,
                                   boolean orderAsc,
                                   int limit,
                                   int offset) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(AccessNumber.class);

        if (isNotBlank(filter)) {
            filter = filter.trim();
            final List<Criterion> filters = new ArrayList<>();

            filters.add(EscapedRestrictions.ilike("number", filter, MatchMode.ANYWHERE));
            filters.add(EscapedRestrictions.ilike("survey.survey.details.title", filter, MatchMode.ANYWHERE));

            criteria.add(or(filters.toArray(new Criterion[filters.size()])));
        }

        criteria.setFirstResult(offset).setMaxResults(limit);

        if(orderColumn != null) {
            final String orderProperty;
            switch (orderColumn) {
                case "number":      orderProperty = "number";        break;
                case "survey":      orderProperty = "survey.title";  break;
                default:
                    throw new RuntimeException("Unexpected sort column " + orderColumn);
            }

            criteria.addOrder(orderAsc ? Order.asc(orderProperty) : Order.desc(orderProperty));
        }

        return (List<AccessNumber>) criteria.list();
    }

    public int count(String filter) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(AccessNumber.class);

        if (isNotBlank(filter)) {
            filter = filter.trim();
            final List<Criterion> filters = new ArrayList<>();

            filters.add(EscapedRestrictions.ilike("number", filter, MatchMode.ANYWHERE));
            filters.add(EscapedRestrictions.ilike("survey.survey.details.title", filter, MatchMode.ANYWHERE));

            criteria.add(or(filters.toArray(new Criterion[filters.size()])));
        }

        criteria.setProjection(Projections.rowCount());

        return ((Number) criteria.uniqueResult()).intValue();
    }
}
