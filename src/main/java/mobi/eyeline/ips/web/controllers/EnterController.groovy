package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services

import javax.faces.context.FacesContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession


@CompileStatic
class EnterController extends BaseController {
    private final UserRepository userRepository = Services.instance().userRepository
    private final  HttpSession session = request.session
    private final SkinController skinController = new SkinController();
    Object getRunOnLogin() {
        init()
        return null
    }

    private void init() {
        User user = getCurrentUser()
        new LocaleController().changeLocale(user)
        skinController.skin = user.uiProfile.skin
        skinController.logo = user.uiProfile.icon
        Services.instance().locationService.skin = user.uiProfile.skin
//        session.setAttribute("currentUser",user)
//        SkinController skinController= new SkinController()

        if (request.isUserInRole('manager') ) {
//            skinController.logo = user.uiProfile.icon
            redirect '/pages/surveys/index.faces'

        } else if (request.isUserInRole('client')) {

            redirect '/pages/surveys/index.faces'
        } else if (request.isUserInRole('admin')) {
            redirect '/pages/admin/accessNumbers.faces'

        } else {
            redirect '/login.faces'
        }


    }

    private void redirect(String target) {
        FacesContext.currentInstance.externalContext.redirect target
    }
}
