package mobi.eyeline.ips.web.controllers.login

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class UserSessionController extends BaseController {
    private final UserRepository userRepository = Services.instance().userRepository

    public String logout() {
        getHttpSession(false)?.invalidate()

        return "LOGIN"
    }

    //
    //  Authorization routines.
    //


    public boolean isSurveyModificationAllowed() { !isClientRole() }
    public boolean isSurveyViewAllowed() { isClientRole() || isManagerRole() }

}
