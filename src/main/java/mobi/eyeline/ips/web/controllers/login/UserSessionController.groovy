package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
@CompileStatic
class UserSessionController extends BaseController {

    String logout() {
        getHttpSession(false)?.invalidate()
        Services.instance().locationService.skin = UiProfile.Skin.ARAKS
        return 'LOGIN'
    }

    //
    //  Authorization routines.
    //


    @Memoized boolean isSurveyModificationAllowed() { !isClientRole() }
    @Memoized boolean isSurveyViewAllowed() { isClientRole() || isManagerRole() }
    @Memoized boolean isInvitationDeliveryAllowed() { getCurrentUser().canSendInvitations }
    @Memoized boolean isC2sAllowed() { getCurrentUser().showC2s }
}
