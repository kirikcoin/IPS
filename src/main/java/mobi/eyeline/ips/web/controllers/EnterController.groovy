package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services

import javax.faces.context.FacesContext


class EnterController extends BaseController {
    private final UserRepository userRepository = Services.instance().userRepository

    Object getRunOnLogin() {
        init()
        return null
    }

    private void init() {
        User user = userRepository.getByLogin(this.userName)
        new LocaleController().changeLocale(user)

        if (request.isUserInRole('manager') || request.isUserInRole('client')) {
            FacesContext.currentInstance.externalContext.redirect('/pages/surveys/index.faces')

        } else {
            FacesContext.currentInstance.externalContext.redirect('/login.faces')
        }
    }
}
