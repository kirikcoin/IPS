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
    
    private User user
    private String userName
    String userFio
    String userEmail

    String currentPassword
    String newPassword
    String passwordForConfirm
    boolean userDataValidationError
    boolean error
    boolean success

    ProfilePageController() {
        userName = getUserName()
        user = userRepository.getByLogin(userName)
        userFio = user.fullName
        userEmail = user.email
    }

    String saveProfile() {
        if(currentPassword==null && newPassword==null && passwordForConfirm==null){
            user.email = userEmail
            user.fullName = userFio
            userDataValidationError= renderViolationMessage(
                    validator.validate(user),
                    [
                        'fullName':'profileEditFullName',
                        'email':'profileEditEmail',
                    ])
            if(userDataValidationError){
                return null
            }

            userRepository.update(user)
            success=true
        } else {
            if(currentPassword!= null && newPassword!=null && passwordForConfirm!=null){
                String hashedCurrentPassword=HashUtils.hashPassword(currentPassword)

                if (equalsIgnoreCase(hashedCurrentPassword, user.password)) {
                    if(newPassword==passwordForConfirm){
                        user.password=HashUtils.hashPassword(newPassword)
                        userRepository.update(user)
                        success=true
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
