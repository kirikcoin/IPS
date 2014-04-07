package mobi.eyeline.ips.repository;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.IlikeExpression;
import org.hibernate.criterion.MatchMode;


public class EscapedRestrictions {
    public static Criterion ilike(String propertyName, String value) {
        return new EscapedILikeExpression(propertyName, value);
    }

    public static Criterion ilike(String propertyName, String value, MatchMode matchMode) {
        return new EscapedILikeExpression(propertyName, value, matchMode);
    }
}

class EscapedILikeExpression extends IlikeExpression {
    private static final String HIBERNATE_ESCAPE_CHAR = "\\";

    public EscapedILikeExpression(String propertyName, Object value) {
        super(propertyName, value);
    }


    public EscapedILikeExpression(String propertyName, String value, MatchMode matchMode) {
        super(propertyName, replaceAll(value), matchMode);
    }

    private static String replaceAll(String value) {
        return value
                .replace("\\", HIBERNATE_ESCAPE_CHAR + "\\")
                .replace("_", HIBERNATE_ESCAPE_CHAR + "_")
                .replace("%", HIBERNATE_ESCAPE_CHAR + "%");

    }
}


