package mobi.eyeline.ips.service;

import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;

import static mobi.eyeline.ips.util.HashUtils.hashPassword;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;


public class UserService {
    private final UserRepository userRepository;
    private final MailService mailService;

    public UserService(UserRepository userRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public void resetPassword(String email) throws LoginException {
      //  if (isEmpty(email)) throw new LoginException(LoginException.LoginErrorKind.NotFoundUser);
        final User user = userRepository.getByEmail(email);
        if (user == null || user.isBlocked()) {
            throw new LoginException(LoginException.LoginErrorKind.NotFoundUser);
        }

        resetPassword(user);
    }

    public void resetPassword(User user) {
        final String newPassword = generatePassword();
        user.setPassword(hashPassword(newPassword));

        userRepository.update(user);
        mailService.sendPasswordRestore(user, newPassword);
    }

    public void deActivate(User user) {
        user.setBlocked(true);
        userRepository.update(user);
        mailService.sendUserDeactivation(user);
    }

    public void activate(User user) {
        user.setBlocked(false);
        userRepository.update(user);
        mailService.sendUserActivation(user);
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public boolean isLoginAllowed(User user) {
        final User existing = userRepository.getByLogin(user.getLogin());
        // XXX: Explicitly compare IDs if don't want to rely on equals() using PK comparison.
        return existing == null || user.equals(existing);
    }

    public boolean isEmailAllowed(User user) {
        final User existing = userRepository.getByEmail(user.getEmail());
        // XXX: Explicitly compare IDs if don't want to rely on equals() using PK comparison.
        return existing == null || user.equals(existing);
    }

    /**
     * @return {@code true} iff {@code rawPassword} is a valid password for {@code user}.
     */
    public boolean checkPassword(User user, String rawPassword) {
        if (rawPassword == null) {
            return false;
        }
        final String hash = hashPassword(rawPassword);
        return equalsIgnoreCase(hash, user.getPassword());
    }
}
