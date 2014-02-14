package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.util.HashUtils
import mobi.eyeline.ips.web.controllers.BaseController

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase

class ProfilePageController extends BaseController {
    
    private final UserRepository userRepository = Services.instance().userRepository
    private final UserService userService = Services.instance().userService

    User user

    String currentPassword
    String newPassword
    String passwordForConfirm

    Boolean error

    ProfilePageController() {
        user = userRepository.getByLogin(this.userName)
    }

    String saveProfile() {
        boolean userDataValidationError
        boolean emailExists = !userService.isEmailAllowed(user)
        if(currentPassword==null && newPassword==null && passwordForConfirm==null) {
            return saveFullNameAndEmail(emailExists)
        } else {
            if(currentPassword!= null && newPassword!=null && passwordForConfirm!=null) {
                String hashedCurrentPassword = HashUtils.hashPassword(currentPassword)
                boolean errorHere;

                if (equalsIgnoreCase(hashedCurrentPassword, user.password)) {
                    if(newPassword == passwordForConfirm) {
                        user.password=HashUtils.hashPassword(newPassword)
                        userDataValidationError =
                                renderViolationMessage(validator.validate(user))
                        if(userDataValidationError) {
                            return null
                        }

                        if(emailExists) {
                            addErrorMessage(getResourceBundle().getString("client.dialog.validation.email.exists"),
                                    "email")
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

    private boolean saveFullNameAndEmail(boolean emailExists) {
        boolean userDataValidationError
        userDataValidationError =
                renderViolationMessage(validator.validate(user))
        if (userDataValidationError) {
            return null
        }
        if (emailExists) {
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.email.exists"),
                    "email")
            return null
        }
        userRepository.update(user)
        error = false
    }

}
