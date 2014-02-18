package mobi.eyeline.ips.web.controllers.login

import mobi.eyeline.ips.web.controllers.BaseController

import javax.faces.context.FacesContext

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class LoginPageController extends BaseController {
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
