package mobi.eyeline.ips.repository;

import com.google.common.primitives.Ints;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.sql.JoinType.LEFT_OUTER_JOIN;


public class SurveyRepository extends BaseRepository<Survey, Integer> {

  public SurveyRepository(DB db) {
    super(db);
  }

  public List<Survey> list(User user,
                           User owner,
                           String filter,
                           Boolean active,
                           String orderProperty,
                           boolean asc,
                           int limit,
                           int offset) {

    final Session session = getSessionFactory().getCurrentSession();

    final Criteria criteria = createQuery(user, owner, filter, active, session);

    // XXX: this JOIN is probably for searching by access number, which we don't do now.
    // criteria.createAlias("statistics", "statistics");

    criteria.setResultTransformer(DISTINCT_ROOT_ENTITY);
    criteria.setFirstResult(offset).setMaxResults(limit);

    // TODO: Order property to object graph path conversion should
    //       better be performed on controller level.
    if (orderProperty != null) {
      final String property;
      switch (orderProperty) {
        case "id":
          property = "id";
          break;
        case "title":
          property = "details.title";
          break;
        case "client":
          property = "client.fullName";
          break;
        case "state":
          property = "state";
          break;
        case "period":
          property = "startDate";
          break;
        default:
          throw new RuntimeException("Unexpected sort column: " + orderProperty);
      }

      criteria.addOrder(asc ? Order.asc(property) : Order.desc(property));
    }

    //noinspection unchecked
    return (List<Survey>) criteria.list();
  }

  private Criteria createQuery(User user, User owner, String filter, Boolean active, Session session) {
    final Criteria criteria = session.createCriteria(Survey.class).setCacheable(true);

    criteria.createAlias("details", "details");
    criteria.createAlias("client", "client");
    criteria.createAlias("owner", "owner", LEFT_OUTER_JOIN);

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

      if (Ints.tryParse(filter) != null) {
        filters.add(eq("id", Integer.parseInt(filter)));
      }
      filters.add(EscapedRestrictions.ilike("details.title", filter, MatchMode.ANYWHERE));
      if (user == null) {
        filters.add(EscapedRestrictions.ilike("client.fullName", filter, MatchMode.ANYWHERE));
      }

      criteria.add(or(
          filters.toArray(new Criterion[filters.size()])
      ));
    }
    return criteria;
  }

  public int count(User user,
                   User owner,
                   String filter,
                   Boolean active) {

    final Session session = getSessionFactory().getCurrentSession();
    final Criteria criteria = createQuery(user, owner, filter, active, session);

    criteria.setProjection(Projections.countDistinct("id"));

    //noinspection unchecked
    return ((Number) criteria.uniqueResult()).intValue();
  }

}
