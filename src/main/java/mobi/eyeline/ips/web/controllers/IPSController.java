package mobi.eyeline.ips.web.controllers;

import mobi.eyeline.ips.util.RequestParam;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.Principal;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public abstract class IPSController implements Serializable
{
  /**
   * Возвращает текущую сессию пользователя
   *
   * @param createIfNeeded создать, если сессии не существует
   * @return сессия
   */
  protected HttpSession getHttpSession(boolean createIfNeeded) {
    return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(createIfNeeded);
  }

  /**
   * Возвращает залогиненного пользвателя
   *
   * @return залогиненный пользователь
   */
  protected Principal getUserPrincipal() {
    return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
  }

  protected RequestParam getParam() {
    return new RequestParam(getRequest());
  }

  public RequestParam.Value getParamValue(String name) {
    return getParam().get(name);
  }

  public HttpServletRequest getRequest() {
    return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
  }
}
