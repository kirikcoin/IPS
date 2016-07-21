package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import mobi.eyeline.ips.web.controllers.BaseController

import javax.enterprise.context.RequestScoped
import javax.inject.Named

@CompileStatic
@Named("userSession")
@RequestScoped
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
  boolean isGlobalStatisticsAllowed() { surveyViewAllowed && globalStatsAllowed }
}
