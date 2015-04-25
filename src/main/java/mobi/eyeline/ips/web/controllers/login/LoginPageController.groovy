package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import mobi.eyeline.ips.web.controllers.BaseController

import javax.enterprise.context.RequestScoped
import javax.faces.context.FacesContext
import javax.inject.Named

@CompileStatic
@Named("login")
@RequestScoped
class LoginPageController extends BaseController {
  private boolean error

  LoginPageController() {
    def params =
        FacesContext.currentInstance.externalContext.requestParameterMap
    this.error = (params["loginError"] != null)

  }

  boolean isError() {
    return error
  }

}
