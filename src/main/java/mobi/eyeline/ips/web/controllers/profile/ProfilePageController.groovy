package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

class ProfilePageController extends BaseController {
    private User user;
    private String userName;
    private UserRepository userRepository = Services.instance().userRepository;

    public String getUserFio() {
        userRepository
        return null;

    }

    public String getUserEmail() {

    }
}
