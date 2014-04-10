package mobi.eyeline.ips.repository;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;


public class EscapedRestrictions {

    public static Criterion ilike(String propertyName, String value) {
        return new EscapedILikeExpression(propertyName, value);
    }

    public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
        return new EscapedILikeExpression(propertyName, value, matchMode);
    }


    private static class EscapedILikeExpression extends LikeExpression {
        private static final String HIBERNATE_ESCAPE_CHAR = "\\";

        public EscapedILikeExpression(String propertyName, String value) {
            super(propertyName, replaceAll(value), null, true);
        }

        public EscapedILikeExpression(String propertyName, String value, MatchMode matchMode) {
            super(propertyName, replaceAll(value), matchMode, null, true);
        }

        @Override
        public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
            return super.toSqlString(criteria, criteriaQuery) + " ESCAPE '\\" + HIBERNATE_ESCAPE_CHAR + "'";
        }

        private static String replaceAll(String value) {
            return value
                    .replace("\\", HIBERNATE_ESCAPE_CHAR + "\\")
                    .replace("_", HIBERNATE_ESCAPE_CHAR + "_")
                    .replace("%", HIBERNATE_ESCAPE_CHAR + "%");
        }
    }
}
