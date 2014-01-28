package mobi.eyeline.ips.web.controllers.login;

import mobi.eyeline.ips.model.Role;
import mobi.eyeline.ips.web.controllers.BaseController;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class UserSessionController extends BaseController {

    public String logout() {
        final HttpSession session = getHttpSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "LOGIN";
    }

}
