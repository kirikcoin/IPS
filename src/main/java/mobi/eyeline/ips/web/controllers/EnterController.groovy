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
    private final UserRepository userRepository = Services.instance().userRepository
    private final  HttpSession session = request.session
    private final LogoBean logoBean = new LogoBean();

    Object getRunOnLogin() {
        init()
        return null
    }

    private void init() {
        User user = getCurrentUser()
        new LocaleController().changeLocale(user)

        if (request.isUserInRole('manager') ) {

            if(user.uiProfile !=null){
                Services.instance().locationService.skin = user.uiProfile.skin
                logoBean.logo = user.uiProfile.icon
            }
            redirect '/pages/surveys/index.faces'

        } else if (request.isUserInRole('client')) {

            if(user.manager.uiProfile != null) {
                logoBean.logo = user.manager.uiProfile.icon
                Services.instance().locationService.skin = user.manager.uiProfile.skin
            }

            redirect '/pages/surveys/index.faces'

        } else if (request.isUserInRole('admin')) {
            throw new AssertionError()
            logger.error("There no admin role in ips")

        } else {
            redirect '/login.faces'
        }


    }

    private void redirect(String target) {
        FacesContext.currentInstance.externalContext.redirect target
    }
}
