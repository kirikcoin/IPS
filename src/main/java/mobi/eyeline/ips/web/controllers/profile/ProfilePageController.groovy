package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.util.HashUtils
import mobi.eyeline.ips.web.controllers.BaseController
import org.apache.commons.lang3.StringUtils

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase

class ProfilePageController extends BaseController {
    
    private final UserRepository userRepository = Services.instance().userRepository
    
    User user

    String currentPassword
    String newPassword
    String passwordForConfirm
    boolean userDataValidationError
    Boolean error


    ProfilePageController() {
        user = userRepository.getByLogin(this.userName)
    }

    String saveProfile() {


        if(currentPassword==null && newPassword==null && passwordForConfirm==null){
            userDataValidationError =
                    renderViolationMessage(validator.validate(user))               // ?
            if(userDataValidationError){
                return null
            }
            userRepository.update(user)
            error=false
        } else {
            if(currentPassword!= null && newPassword!=null && passwordForConfirm!=null){
                String hashedCurrentPassword=HashUtils.hashPassword(currentPassword)

                if (equalsIgnoreCase(hashedCurrentPassword, user.password)) {
                    if(newPassword==passwordForConfirm){
                        user.password=HashUtils.hashPassword(newPassword)
                        userDataValidationError =
                                renderViolationMessage(validator.validate(user))               // ?
                        if(userDataValidationError){
                            return null
                        }
                        userRepository.update(user)
                        error=false
                    } else {
                        error=true
                    }
                } else {
                    error=true
                }
            } else {
                error=true
            }
        }
    }

}
