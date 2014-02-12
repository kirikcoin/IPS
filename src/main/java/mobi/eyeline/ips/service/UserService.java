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

    public void blockUser(String login) throws LoginException {
        User user = userRepository.getByLogin(login);
        if(user == null || user.isBlocked()) {
            throw new LoginException(LoginException.LoginErrorKind.NotFoundUser);
        }
        user.setBlocked(true);
        userRepository.update(user);
        mailService.sendUserDeactivation(user);

    }

    public void unblockUser(String login) throws LoginException {
        User user = userRepository.getByLogin(login);
        if(user == null || !user.isBlocked()) {
            throw new LoginException(LoginException.LoginErrorKind.NotFoundUser);
        }
        user.setBlocked(false);
        userRepository.update(user);
        mailService.sendUserActivation(user);
    }

    public String generatePassword() {
        String password = RandomStringUtils.randomAlphabetic(8);

        return password;
    }

    public boolean isLoginExists(String login) {
        User user = userRepository.getByLogin(login);

        if(user == null) {
            return false;
        }

        return true;
    }

    public boolean isEmailExists(String email) {
        User user = userRepository.getByEmail(email);

        if(user == null) {
            return false;
        }


        return true;
    }

    public boolean isLoginExists(String login, int id) {
        User user = userRepository.getByLogin(login);

        if(user == null) {
            return false;
        }

        if (id == user.getId()){
            return false;
        }

        return true;
    }

    public boolean isEmailExists(String email, int id) {
        User user = userRepository.getByEmail(email);

        if(user == null) {
            return false;
        }

        if (id == user.getId()){
            return false;
        }

        return true;
    }

}
