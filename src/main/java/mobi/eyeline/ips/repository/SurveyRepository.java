package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import org.apache.commons.lang3.StringUtils;
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
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.eqOrIsNull;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.sql.JoinType.LEFT_OUTER_JOIN;


public class SurveyRepository extends BaseRepository<Survey, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SurveyRepository.class);

    public SurveyRepository(DB db) {
        super(db);
    }

    /*
    static DetachedCriteria getCriteriaSurvey(User user) {
        final DetachedCriteria criteria = DetachedCriteria
                .forClass(Survey.class)
                .createAlias("details", "details")
                .createAlias("client", "client");

        if (user.getRole() == Role.CLIENT) {
            criteria.add(Restrictions.eq("client.id", user.getId()));
        }
        return criteria;
    } */

    // TODO: Extract complex `list' methods to static `DetachedQuery` properties
    //       for direct use in controllers. This should result in code reuse, reduce
    //       duplication and clean up method signatures.
    public List<Survey> list(User user,
                             User owner,
                             String filter,
                             Boolean active,
                             String orderProperty,
                             boolean asc,
                             int limit,
                             int offset) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = session.createCriteria(Survey.class).setCacheable(true);

        criteria.createAlias("details", "details");
        criteria.createAlias("statistics", "statistics");
        criteria.createAlias("client", "client");
        criteria.createAlias("owner", "owner", LEFT_OUTER_JOIN);
        criteria.createAlias("statistics.accessNumber", "accessNumber", LEFT_OUTER_JOIN);

        if (active != null) {
            criteria.add(eq("active", active));
        }

        if (user != null) {
            criteria.add(eq("client.id", user.getId()));
        }

        if (owner != null) {
            criteria.add(or(
                    isNull("owner.id"),
                    eq("owner.id", owner.getId())
            ));
        }

        if (isNotBlank(filter)) {
            filter = filter.trim();

            final List<Criterion> filters = new ArrayList<>();

            if (StringUtils.isNumeric(filter)) {
                filters.add(eq("id", Integer.parseInt(filter)));
            }
            filters.add(EscapedRestrictions.ilike("details.title", filter, MatchMode.ANYWHERE));
            filters.add(EscapedRestrictions.ilike("accessNumber.number", filter, MatchMode.ANYWHERE));
            if (user == null) {
                filters.add(EscapedRestrictions.ilike("client.fullName", filter, MatchMode.ANYWHERE));
            }

            criteria.add(or(
                    filters.toArray(new Criterion[filters.size()])
            ));
        }

        criteria.setFirstResult(offset).setMaxResults(limit);

        // TODO: Order property to object graph path conversion should
        //       better be performed on controller level.
        if (orderProperty != null) {

            final String property;
            switch (orderProperty) {
                case "id":              property = "id";                        break;
                case "title":           property = "details.title";             break;
                case "client":          property = "client.fullName";           break;
                case "state":           property = "state";                     break;
                case "period":          property = "startDate";                 break;
                case "accessNumber":    property = "statistics.accessNumber";   break;
                default:
                    throw new RuntimeException("Unexpected sort column: " + orderProperty);
            }

            criteria.addOrder(asc ? Order.asc(property) : Order.desc(property));
        }

        //noinspection unchecked
        return (List<Survey>) criteria.list();
    }

    public int count(User user,
                     User owner,
                     String filter,
                     Boolean active) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = session.createCriteria(Survey.class);

        criteria.createAlias("details", "details");
        criteria.createAlias("statistics", "statistics");
        criteria.createAlias("client", "client", LEFT_OUTER_JOIN);
        criteria.createAlias("statistics.accessNumber", "accessNumber", LEFT_OUTER_JOIN);

        if (active != null) {
            criteria.add(eq("active", active));
        }

        if (user != null) {
            criteria.add(eq("client.id", user.getId()));
        }

        if (owner != null) {
            criteria.add(or(
                    isNull("owner.id"),
                    eq("owner.id", owner.getId())
            ));
        }

        if (isNotBlank(filter)) {
            filter = filter.trim();

            final List<Criterion> filters = new ArrayList<>();

            if (StringUtils.isNumeric(filter)) {
                filters.add(eq("id", Integer.parseInt(filter)));
            }

            filters.add(EscapedRestrictions.ilike("details.title", filter, MatchMode.ANYWHERE));
            filters.add(EscapedRestrictions.ilike("accessNumber.number", filter, MatchMode.ANYWHERE));
            if (user == null) {
                filters.add(EscapedRestrictions.ilike("client.fullName", filter, MatchMode.ANYWHERE));
            }

            criteria.add(or(
                    filters.toArray(new Criterion[filters.size()])
            ));
        }

        criteria.setProjection(Projections.rowCount());

        //noinspection unchecked
        return ((Number) criteria.uniqueResult()).intValue();
    }

}
