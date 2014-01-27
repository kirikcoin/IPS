package mobi.eyeline.ips.web.auth;

import java.security.Principal;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 *
 * Класс пользователя для JAAS-аутентификации
 */
public class WebUser implements Principal
{
  private final String name;


  WebUser(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

