package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

class ProfilePageController extends BaseController {
    private User user;
    private String userName;
    def String userFio;
    def String userEmail;
    private UserRepository userRepository = Services.instance().userRepository;

    ProfilePageController() {
        userName = getUserName();
        user = userRepository.getByLogin(userName);
        userFio = user.fullName;
        userEmail = user.email;
    }

    public void saveProfile() {
        user.email = userEmail;
        user.fullName = userFio;
        userRepository.update(user);
    }

}
