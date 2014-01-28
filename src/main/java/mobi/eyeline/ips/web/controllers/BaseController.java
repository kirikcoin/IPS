package mobi.eyeline.ips.web.controllers;

import mobi.eyeline.ips.model.Role;
import mobi.eyeline.ips.util.RequestParam;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.Principal;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 */
public abstract class BaseController implements Serializable
{
    /**
     * Возвращает текущую сессию пользователя
     *
     * @param createIfNeeded создать, если сессии не существует
     * @return сессия
     */
    protected HttpSession getHttpSession(boolean createIfNeeded) {
        final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        return (HttpSession) context.getSession(createIfNeeded);
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
        final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        return (HttpServletRequest) context.getRequest();
    }

    public boolean isAuthenticated() {
        return getUserPrincipal() != null;
    }

    private boolean inRole(Role role) {
        final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        return context.isUserInRole(role.getName());
    }

    public String getUserName() {
        final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        return context.getRemoteUser();
    }

    public boolean isClientRole() {
        return inRole(Role.CLIENT);
    }

    public boolean isManagerRole() {
        return inRole(Role.MANAGER);
    }

    public boolean isAdminRole() {
        return inRole(Role.ADMIN);
    }
}
