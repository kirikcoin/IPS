package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.User

import javax.faces.bean.ManagedBean
import javax.faces.context.FacesContext

@CompileStatic
@Slf4j('logger')
@ManagedBean(name = "enterController")
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
