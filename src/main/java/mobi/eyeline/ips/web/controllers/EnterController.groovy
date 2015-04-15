package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.User

import javax.enterprise.inject.Model
import javax.faces.context.ExternalContext
import javax.faces.context.FacesContext
import javax.inject.Inject

@CompileStatic
@Slf4j('logger')
@Model
class EnterController extends BaseController {

  @Inject private ExternalContext externalContext

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
    externalContext.redirect target
  }
}
