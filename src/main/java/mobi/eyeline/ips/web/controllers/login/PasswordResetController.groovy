package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.exceptions.LoginException
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.EmailValidator

import javax.faces.context.FacesContext

@CompileStatic
@Slf4j('logger')
public class PasswordResetController extends BaseController {

    private final UserService userService = Services.instance().userService

    String email

    void resetPassword() {
        if (!EmailValidator.isValid(email)) {
            addErrorMessage(strings['passwrecovery.form.incorrectemail'], 'email')
        }

        try {
            userService.resetPassword(email)
            FacesContext.currentInstance.externalContext.redirect '/login.faces?recovery=true'

        } catch (LoginException e) {
            logger.warn(e.message, e)
            FacesContext.currentInstance.externalContext.redirect '/passwordreset.faces?recovery=false'
        }
    }

}
