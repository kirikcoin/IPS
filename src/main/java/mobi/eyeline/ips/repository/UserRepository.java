package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.exceptions.LoginException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ilike;
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
        final Session session = getSessionFactory().getCurrentSession();
        return (User) session
                .createCriteria(User.class)
                .add(eq("login", login))
                .uniqueResult();
    }

    public List<User> listByRole(Role role) {
        final Session session = getSessionFactory().getCurrentSession();
        //noinspection unchecked
        return (List<User>) session
                .createCriteria(User.class)
                .add(eq("role", role))
                .list();
    }


    public User getByEmail(String email) {
        final Session session = getSessionFactory().getCurrentSession();
        return (User) session
                .createCriteria(User.class)
                .add(eq("email", email))
                .uniqueResult();
    }

    public List<User> list(String filter,
                           String orderColumn,
                           boolean orderAsc,
                           int limit,
                           int offset) {
        final Session session = getSessionFactory().openSession();
        final Criteria criteria = session.createCriteria(User.class);

        if (isNotBlank(filter)) {
            filter = filter.trim();
            final List<Criterion> filters = new ArrayList<>();

            filters.add(ilike("fullName", filter, MatchMode.ANYWHERE));
            filters.add(ilike("company", filter, MatchMode.ANYWHERE));
            filters.add(ilike("login", filter, MatchMode.ANYWHERE));
            filters.add(ilike("email", filter, MatchMode.ANYWHERE));

            criteria.add(or(filters.toArray(new Criterion[filters.size()])));

        }

        criteria.setFirstResult(offset).setMaxResults(limit);

        // TODO: may be in controller too
        if(orderColumn != null) {
            final String property;
            switch (orderColumn) {
                case "fullname":     property = "fullName";        break;
                case "company":      property = "company";         break;
                case "login":        property = "login";           break;
                case "email":        property = "email";           break;
                case "status":       property = "blocked";         break;
                default:
                    throw new RuntimeException("Unexpected sort column: " + orderColumn);
            }

            criteria.addOrder(orderAsc ? Order.asc(property) : Order.desc(property));
        }

        return (List<User>) criteria.list();
    }

    public int count(String filter) {
        final Session session = getSessionFactory().getCurrentSession();
        final Criteria criteria = session.createCriteria(User.class);

        if(isNotBlank(filter)){
            filter = filter.trim();
            final List<Criterion> filters = new ArrayList<>();

            filters.add(ilike("fullName", filter, MatchMode.ANYWHERE));
            filters.add(ilike("company", filter, MatchMode.ANYWHERE));
            filters.add(ilike("login", filter, MatchMode.ANYWHERE));
            filters.add(ilike("email", filter, MatchMode.ANYWHERE));

            criteria.add(or(filters.toArray(new Criterion[filters.size()])));

        }

        criteria.setProjection(Projections.rowCount());

        return ((Number) criteria.uniqueResult()).intValue();
    }


    // TODO: do we need this?
    public void insertOneTimePass(int userId, String onePass)
            throws SQLException, IOException {

        // Initial query:
        // insertOnePass= UPDATE users SET one_time_pw = ? WHERE uid = ? LIMIT 1
        throw new AssertionError("Not implemented");
    }

}
