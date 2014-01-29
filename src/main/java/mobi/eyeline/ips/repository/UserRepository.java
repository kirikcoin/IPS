package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.util.HashUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static org.hibernate.criterion.Restrictions.eq;

public class UserRepository extends BaseRepository<User, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(DB db) {
        super(db);
    }

    public User getUser(String login, String password) {
        final Session session = getSessionFactory().getCurrentSession();

        final String providedHash = HashUtils.hashPassword(password);
        return (User) session
                .createCriteria(User.class)
                .add(eq("login", login))
                .add(eq("password", providedHash).ignoreCase())
                .uniqueResult();
    }

    public User getByLogin(String login) {
        final Session session = getSessionFactory().getCurrentSession();
        return (User) session
                .createCriteria(User.class)
                .add(eq("login", login))
                .uniqueResult();
    }

    public User getUserByEmail(String email) {
        final Session session = getSessionFactory().openSession();
        try{
            final  User user = (User) session
                    .createCriteria(User.class)
                    .add(eq("email",email))
                    .uniqueResult();

            return user;
        } finally {
            session.close();
        }
    }

    // TODO: do we need this?
    public void insertOneTimePass(int userId, String onePass)
            throws SQLException, IOException {

        // Initial query:
        // insertOnePass= UPDATE users SET one_time_pw = ? WHERE uid = ? LIMIT 1
        throw new AssertionError("Not implemented");
    }

}