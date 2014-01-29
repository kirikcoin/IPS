package mobi.eyeline.ips.web.auth;

import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.service.Services;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class IPSAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(IPSAuthenticator.class);

    public String getUserRole(String login, String password) throws LoginException {
        final User user;
        try {
            user = Services.instance().getUserRepository().getUser(login, password);

        } catch (HibernateException e) {
            throw new LoginException(e.getMessage());
        }

        if (user == null) {
            logger.info("User not found for login = [" + login + "]," +
                    " password = [" + password + "]");
            return null;
        }

        if (user.isBlocked()) {
            logger.info("Account is blocked for login = [" + login + "]");
            return null;
        }

        return user.getRole().getName();
    }
}
