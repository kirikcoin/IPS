package mobi.eyeline.ips.web.auth;

import javax.security.auth.login.LoginException;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public class IPSAuthenticator
{
  public String getUserRole(String login, String password) throws LoginException {
    if(login == null || password == null || !"admin".equals(login) || !"password".equals(password)) {
      return null;
    }

    return "admin";
  }
}
