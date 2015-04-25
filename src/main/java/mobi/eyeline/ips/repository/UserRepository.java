package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Role;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.util.HashUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

public class UserRepository extends BaseRepository<User, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

  public UserRepository(DB db) {
    super(db);
  }

  public User getUser(String login, String password) {
    final Session session = getSessionFactory().getCurrentSession();
    final Transaction tx = session.beginTransaction();

    try {
      final String providedHash = HashUtils.hashPassword(password);
      final User user = (User) session
          .createCriteria(User.class)
          .setCacheable(true)
          .add(eq("login", login))
          .add(eq("password", providedHash).ignoreCase())
          .uniqueResult();

      tx.commit();

      return user;

    } catch (HibernateException e) {
      try {
        tx.rollback();
      } catch (HibernateException ex) {
        logger.error(e.getMessage(), ex);
      }

      throw e;
    }
  }

  public User getByLogin(String login) {
    final Session session = getSessionFactory().openSession();

    try {
      return (User) session
          .createCriteria(User.class)
          .add(eq("login", login))
          .uniqueResult();
    } finally {
      session.close();
    }
  }

  public User getByEmail(String email) {
    final Session session = getSessionFactory().openSession();
    try {
      return (User) session
          .createCriteria(User.class)
          .add(eq("email", email))
          .uniqueResult();
    } finally {
      session.close();
    }

  }

  public List<User> listByRole(Role role) {
    final Session session = getSessionFactory().getCurrentSession();
    //noinspection unchecked
    return (List<User>) session
        .createCriteria(User.class)
        .setCacheable(true)
        .add(eq("role", role))
        .list();
  }

  public List<User> listClients(User manager) {
    final Session session = getSessionFactory().getCurrentSession();
    if (manager == null) {
      return listByRole(Role.CLIENT);

    } else {
      //noinspection unchecked
      return (List<User>) session
          .createCriteria(User.class)
          .setCacheable(true)
          .add(eq("role", Role.CLIENT))
          .add(eq("manager", manager))
          .list();
    }
  }

  public List<User> list(User manager,
                         String filter,
                         String orderColumn,
                         boolean orderAsc,
                         int limit,
                         int offset) {
    final Session session = getSessionFactory().getCurrentSession();
    final Criteria criteria = session.createCriteria(User.class).setCacheable(true);

    if (isNotBlank(filter)) {
      filter = filter.trim();

      final Criterion filters = or(
          EscapedRestrictions.ilike("fullName", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("company", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("login", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("email", filter, MatchMode.ANYWHERE)
      );

      criteria.add(filters);
    }

    if (manager != null) {
      criteria.add(Restrictions.eq("manager", manager));
    }

    criteria.add(Restrictions.eq("role", Role.CLIENT));
    criteria.setFirstResult(offset).setMaxResults(limit);

    // TODO: may be in controller too
    if (orderColumn != null) {
      final String property;
      switch (orderColumn) {
        case "fullName":
          property = "fullName";
          break;
        case "company":
          property = "company";
          break;
        case "login":
          property = "login";
          break;
        case "email":
          property = "email";
          break;
        case "status":
          property = "blocked";
          break;
        default:
          throw new RuntimeException("Unexpected sort column: " + orderColumn);
      }

      criteria.addOrder(orderAsc ? Order.asc(property) : Order.desc(property));
    }

    //noinspection unchecked
    return (List<User>) criteria.list();
  }

  public int count(User manager, String filter) {
    final Session session = getSessionFactory().getCurrentSession();
    final Criteria criteria = session.createCriteria(User.class).setCacheable(true);

    if (isNotBlank(filter)) {
      filter = filter.trim();

      final Criterion filters = or(
          EscapedRestrictions.ilike("fullName", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("company", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("login", filter, MatchMode.ANYWHERE),
          EscapedRestrictions.ilike("email", filter, MatchMode.ANYWHERE)
      );

      criteria.add(filters);
    }

    if (manager != null) {
      criteria.add(Restrictions.eq("manager", manager));
    }

    criteria.add(Restrictions.eq("role", Role.CLIENT));
    criteria.setProjection(Projections.rowCount());

    return ((Number) criteria.uniqueResult()).intValue();
  }

}
