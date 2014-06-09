package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
@CompileStatic
class UserSessionController extends BaseController {
    private final UserRepository userRepository = Services.instance().userRepository

    String logout() {
        getHttpSession(false)?.invalidate()

        return "LOGIN"
    }

    //
    //  Authorization routines.
    //


    boolean isSurveyModificationAllowed() { !isClientRole() }
    boolean isSurveyViewAllowed() { isClientRole() || isManagerRole() }
    boolean isInvitationDeliveryAllowed() { getCurrentUser().canSendInvitations }
}
