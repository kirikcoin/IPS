package mobi.eyeline.ips.service;

import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.repository.UserRepository;
import mobi.eyeline.ips.util.HashUtils;
import org.apache.commons.lang3.RandomStringUtils;



public class UserService {
    private final UserRepository userRepository;
    private final MailService mailService;

    public UserService(UserRepository userRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
    }
    // TODO: in one transaction
    public void restorePassword(String email) throws LoginException {
        User user = userRepository.getByEmail(email);
        if(user == null || user.isBlocked()) {
            throw new LoginException(LoginException.LoginErrorKind.NotFoundUser);
        }

        String password = generatePassword();
        String hashedPassword = HashUtils.hashPassword(password);
        user.setPassword(hashedPassword);
        userRepository.update(user);
        mailService.sendPasswordRestore(user, password);
    }

    public void blockUser(String login) {
        User user = userRepository.getByLogin(login);

        user.setBlocked(true);
        userRepository.update(user);
        mailService.sendUserDeactivation(user);

    }

    public void unblockUser(String login) {
        User user = userRepository.getByLogin(login);

        user.setBlocked(false);
        userRepository.update(user);
        mailService.sendUserActivation(user);
    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public boolean isLoginExists(String login) {
       return userRepository.getByLogin(login) != null;
    }

    public boolean isEmailExists(String email) {
        return userRepository.getByEmail(email) != null;
    }

    public boolean isLoginAllowed(User user) {
        final User existing = userRepository.getByLogin(user.getLogin());
        // XXX: Explicitly compare IDs if don't want to rely on equals() using PK comparison.
        return existing == null || user.equals(existing);
    }

    public boolean isEmailAllowed(User user) {
        final User existing = userRepository.getByEmail(user.getLogin());
        // XXX: Explicitly compare IDs if don't want to rely on equals() using PK comparison.
        return existing == null || user.equals(existing);
    }

}
