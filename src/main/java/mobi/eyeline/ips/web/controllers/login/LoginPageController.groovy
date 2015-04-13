package mobi.eyeline.ips.web.controllers.login

import groovy.transform.CompileStatic
import mobi.eyeline.ips.web.controllers.BaseController

import javax.faces.bean.ManagedBean
import javax.faces.context.FacesContext

@CompileStatic
@ManagedBean(name = "login")
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
