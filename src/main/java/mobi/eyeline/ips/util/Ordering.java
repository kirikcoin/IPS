package mobi.eyeline.ips.util;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class Ordering {

  /**
   * Completely useless ordering, does nothing.
   */
  public static final Order NoOrder = new Order(null, true) {

    @Override
    public String toSqlString(Criteria criteria,
                              CriteriaQuery criteriaQuery) throws HibernateException {
      return "";
    }
  };

}
