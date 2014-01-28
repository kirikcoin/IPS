package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.util.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


import static mobi.eyeline.ips.exceptions.LoginException.LoginErrorKind.NotFoundUser;
import static mobi.eyeline.ips.exceptions.LoginException.LoginErrorKind.WrongPassword;
import static org.hibernate.criterion.Restrictions.eq;

public class UserRepository extends BaseRepository<User, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(DB db) {
        super(db);
    }

    private String hash(String value)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        // Note: this should correspond to DB contents.
        return HashUtils.hash(value, "SHA-256", "UTF-8");
    }

    /**
     * @throws LoginException if password is incorrect or any error occurred.
     */
    private void checkPassword(User user,
                               String password) throws LoginException {

        final String providedHash;
        try {
            providedHash = hash(password);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            throw new LoginException(NotFoundUser);
        }

        if (!StringUtils.equalsIgnoreCase(user.getPassword(), providedHash)) {
            throw new LoginException(WrongPassword);
        }
    }

    public User getUser(String login, String password) throws LoginException {

        final Session session = getSessionFactory().openSession();
        try {
            final User user = (User) session
                    .createCriteria(User.class)
                    .add(eq("login", login))
                    .uniqueResult();

            if (user == null) {
                throw new LoginException(NotFoundUser);
            }

            checkPassword(user, password);

            return user;

        } finally {
            session.close();
        }
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