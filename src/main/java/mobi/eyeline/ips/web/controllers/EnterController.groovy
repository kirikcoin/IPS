package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services

import javax.faces.context.FacesContext
import javax.servlet.http.HttpSession


@CompileStatic
@Slf4j('logger')
class EnterController extends BaseController {

    Object getRunOnLogin() {
        init()
        return null
    }

    private void init() {
        User user = getCurrentUser()
        new LocaleController().changeLocale(user)

        if (request.isUserInRole('manager') || request.isUserInRole('client')) {
            redirect '/pages/surveys/index.faces'

        } else if (request.isUserInRole('admin')) {
            logger.error("There no admin role in ips")
            throw new AssertionError()

        } else {
            redirect '/login.faces'
        }
    }

    private void redirect(String target) {
        FacesContext.currentInstance.externalContext.redirect target
    }
}
