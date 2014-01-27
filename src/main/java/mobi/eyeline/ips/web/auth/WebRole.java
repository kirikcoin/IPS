package mobi.eyeline.ips.web.auth;

import java.security.Principal;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 *
 * Класс роли для JAAS-аутентификации
 */
public class WebRole implements Principal
{
  private final String name;


  WebRole(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

