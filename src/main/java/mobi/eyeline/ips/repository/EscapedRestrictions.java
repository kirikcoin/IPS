package mobi.eyeline.ips.repository;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;

import static mobi.eyeline.ips.repository.DB.LIKE_ESCAPE_CHARACTER;


public class EscapedRestrictions {

  public static Criterion ilike(String propertyName, String value) {
    return new EscapedILikeExpression(propertyName, value);
  }

  public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
    return new EscapedILikeExpression(propertyName, value, matchMode);
  }


  private static class EscapedILikeExpression extends LikeExpression {

    public EscapedILikeExpression(String propertyName, String value) {
      super(propertyName, replaceAll(value), null, true);
    }

    public EscapedILikeExpression(String propertyName, String value, MatchMode matchMode) {
      super(propertyName, replaceAll(value), matchMode, null, true);
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
        throws HibernateException {
      final String escapeExpression = DB.getEscapeExpression(criteriaQuery.getFactory());
      return super.toSqlString(criteria, criteriaQuery) + escapeExpression;
    }

    private static String replaceAll(String value) {
      return value
          .replace("\\", LIKE_ESCAPE_CHARACTER + "\\")
          .replace("_", LIKE_ESCAPE_CHARACTER + "_")
          .replace("%", LIKE_ESCAPE_CHARACTER + "%");
    }
  }
}
