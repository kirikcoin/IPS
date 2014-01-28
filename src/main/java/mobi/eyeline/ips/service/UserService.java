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
        User user = userRepository.getUserByEmail(email);
        if(user == null || user.isBlocked()) {
            throw new LoginException(LoginException.LoginErrorKind.NotFoundUser);
        }

        String password = RandomStringUtils.randomAlphabetic(8);
        String hashedPassword = HashUtils.hashPassword(password);
        user.setPassword(hashedPassword);
        userRepository.update(user);
        mailService.sendPasswordRestore(user, password);
    }
}
