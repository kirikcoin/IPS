package mobi.eyeline.ips.web.controllers.login;

import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.web.controllers.BaseController;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class LoginPageController extends BaseController {
  private boolean error;

  public LoginPageController() {
    Map<String, String> request = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String error = request.get("loginError");
    this.error = error != null;

   }

  public boolean isError() {
    return error;
  }


}
