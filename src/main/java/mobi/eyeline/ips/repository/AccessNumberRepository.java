package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.AccessNumber;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.sql.JoinType.LEFT_OUTER_JOIN;

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

    criteria.createAlias("surveyStats", "surveyStats", LEFT_OUTER_JOIN);
    criteria.createAlias("surveyStats.survey", "survey", LEFT_OUTER_JOIN);
    criteria.createAlias("survey.details", "details", LEFT_OUTER_JOIN);

    if (isNotBlank(filter)) {
      filter = filter.trim();

      final Criterion filters = or(
          EscapedRestrictions.ilike("number", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("details.title", filter, MatchMode.ANYWHERE)
      );
      criteria.add(filters);
    }

    criteria.setFirstResult(offset).setMaxResults(limit);

    if (orderColumn != null) {
      final String orderProperty;
      switch (orderColumn) {
        case "number":
          orderProperty = "number";
          break;
        case "survey":
          orderProperty = "details.title";
          break;
        default:
          throw new RuntimeException("Unexpected sort column " + orderColumn);
      }

      criteria.addOrder(orderAsc ? Order.asc(orderProperty) : Order.desc(orderProperty));
    }

    //noinspection unchecked
    return (List<AccessNumber>) criteria.list();
  }

  public int count(String filter) {
    final Session session = getSessionFactory().getCurrentSession();
    final Criteria criteria = session.createCriteria(AccessNumber.class);

    criteria.createAlias("surveyStats", "surveyStats", LEFT_OUTER_JOIN);
    criteria.createAlias("surveyStats.survey", "survey", LEFT_OUTER_JOIN);
    criteria.createAlias("survey.details", "details", LEFT_OUTER_JOIN);

    if (isNotBlank(filter)) {
      filter = filter.trim();

      final Criterion filters = or(
          EscapedRestrictions.ilike("number", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("details.title", filter, MatchMode.ANYWHERE)
      );
      criteria.add(filters);
    }

    criteria.setProjection(Projections.rowCount());

    return ((Number) criteria.uniqueResult()).intValue();
  }

  public AccessNumber find(String number) {
    final Session session = getSessionFactory().getCurrentSession();

    final Criteria criteria = session.createCriteria(AccessNumber.class);
    return (AccessNumber) criteria.add(eq("number", number)).uniqueResult();
  }

  public List<AccessNumber> list(Survey survey) {
    final Session session = getSessionFactory().openSession();
    try {
      final Criteria criteria = session.createCriteria(AccessNumber.class);
      //noinspection unchecked
      return (List<AccessNumber>) criteria.add(eq("surveyStats", survey.getStatistics())).list();

    } finally {
      session.close();
    }
  }
}
