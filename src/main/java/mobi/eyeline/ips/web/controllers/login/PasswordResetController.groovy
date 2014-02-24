package mobi.eyeline.ips.web.controllers.login


import mobi.eyeline.ips.exceptions.LoginException
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.EmailValidator

public class PasswordResetController extends BaseController {

    private final UserService userService = Services.instance().userService

    String email
    boolean retry

    String resetPassword() {
       if(!EmailValidator.validate(email)){
           addErrorMessage(
                   resourceBundle.getString("passwrecovery.form.incorrectemail"),
                   "email")
           return null
       }
        try {
            userService.resetPassword((String)email)
            return "DONE_RECOVERY"
        } catch (LoginException e) {
            return "RETRY_RECOVERY"
        }
    }

}
