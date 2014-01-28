package mobi.eyeline.ips.web.controllers.login;

import mobi.eyeline.ips.model.Role;
import mobi.eyeline.ips.web.controllers.IPSController;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class UserSessionController extends IPSController
{
  public String logout() {
    HttpSession session = getHttpSession(false);
    if(session != null) {
      session.invalidate();
    }
    return "LOGIN";
  }

  public boolean isLogined() {
    return getUserPrincipal() != null;
  }

    public boolean getUserInClientRole() {
        return FacesContext.getCurrentInstance().getExternalContext().isUserInRole(Role.CLIENT.getName());
    }

}
