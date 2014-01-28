package mobi.eyeline.ips.web.auth;

import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.service.Services;
import org.hibernate.HibernateException;

import javax.security.auth.login.LoginException;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class IPSAuthenticator
{
  public String getUserRole(String login, String password) throws LoginException {
      User user;
      try {
         user = Services.instance().getUserRepository().getUser(login,password);
      } catch (mobi.eyeline.ips.exceptions.LoginException e) {
          return null;
      } catch (HibernateException e){
          throw new LoginException(e.getMessage());
      }

      return user.getRole().getName();

  }
}
