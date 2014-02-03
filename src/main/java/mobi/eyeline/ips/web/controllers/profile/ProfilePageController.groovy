package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.util.HashUtils
import mobi.eyeline.ips.web.controllers.BaseController
import org.apache.commons.lang3.StringUtils

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase

class ProfilePageController extends BaseController {
    private final UserRepository userRepository = Services.instance().userRepository;
    private User user;
    private String userName;
    def String userFio;
    def String userEmail;

    def String currentPassword;
    def String newPassword;
    def String passwordForConfirm;
    def boolean error;
    def boolean success;

    ProfilePageController() {
        userName = getUserName();
        user = userRepository.getByLogin(userName);
        userFio = user.fullName;
        userEmail = user.email;
    }

    public String saveProfile() {
        if(currentPassword==null && newPassword==null && passwordForConfirm==null){
            user.email = userEmail;
            user.fullName = userFio;
            userRepository.update(user);
            success=true;
        } else {
            if(currentPassword!= null && newPassword!=null && passwordForConfirm!=null){
                String hashedCurrentPassword=HashUtils.hashPassword(currentPassword);

                if (equalsIgnoreCase(hashedCurrentPassword, user.password)) {
                    if(newPassword==passwordForConfirm){
                        user.password=HashUtils.hashPassword(newPassword);
                        userRepository.update(user);
                        success=true;
                    } else {
                        error=true;
                    }
                } else {
                    error=true;
                }
            } else {
                error=true;
            }

        }

    }

}
