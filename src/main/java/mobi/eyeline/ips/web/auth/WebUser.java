package mobi.eyeline.ips.web.auth;

import java.security.Principal;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 * <p/>
 * Класс пользователя для JAAS-аутентификации
 */
public class WebUser implements Principal {
  private final String name;
  private final Integer id;

  WebUser(String name, Integer id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Integer getId() {
    return id;
  }
}
