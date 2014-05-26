package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.exceptions.LoginException
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.EmailValidator

@CompileStatic
@Slf4j('logger')
public class PasswordResetController extends BaseController {

    private final UserService userService = Services.instance().userService

    String email

    String resetPassword() {
        if (!EmailValidator.isValid(email)) {
            addErrorMessage(strings['passwrecovery.form.incorrectemail'], 'email')
            return null
        }

        try {
            userService.resetPassword((String) email)
            return 'DONE_RECOVERY'

        } catch (LoginException e) {
            logger.warn(e.getMessage(), e)
            return 'RETRY_RECOVERY'
        }
    }

}
