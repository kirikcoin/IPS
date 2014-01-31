package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

class ProfilePageController extends BaseController {
    private User;
    private UserRepository userRepository = Services.instance().userRepository;

    public String getUserFio() {
        return null;

    }

    public String getUserEmail() {

    }
}
