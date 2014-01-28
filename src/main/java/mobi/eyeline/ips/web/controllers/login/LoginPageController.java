package mobi.eyeline.ips.web.controllers.login;

import mobi.eyeline.ips.exceptions.LoginException;
import mobi.eyeline.ips.service.Services;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class LoginPageController
{
  private boolean error;


  public LoginPageController() {
    Map<String, String> request = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String error = request.get("loginError");
    this.error = error != null;

//    String username = request.get("j_username");
//    String password = request.get("j_password");
//      try {
//          Services.instance().getUserRepository().getUser(username,password);
//      } catch (LoginException e) {
//          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//      }


  }

  public boolean isError() {
    return error;
  }
}
