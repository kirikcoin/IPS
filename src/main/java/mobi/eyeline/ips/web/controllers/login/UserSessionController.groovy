package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import mobi.eyeline.ips.web.controllers.BaseController

import javax.faces.bean.ManagedBean

@CompileStatic
@ManagedBean(name = "userSession")
class UserSessionController extends BaseController {

  String logout() {
    getHttpSession(false)?.invalidate()
    return 'LOGIN'
  }

  //
  //  Authorization routines.
  //


  @Memoized
  boolean isSurveyModificationAllowed() { !isClientRole() }

  @Memoized
  boolean isSurveyViewAllowed() { isClientRole() || isManagerRole() }

  @Memoized
  boolean isInvitationDeliveryAllowed() { getCurrentUser().canSendInvitations }

  @Memoized
  boolean isC2sAllowed() { getCurrentUser().showC2s }
}
