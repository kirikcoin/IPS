package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.util.RequestParam

import javax.faces.application.FacesMessage
import javax.faces.context.ExternalContext
import javax.faces.context.FacesContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import javax.validation.ConstraintViolation
import java.security.Principal

import static java.util.Collections.emptyMap

public abstract class BaseController implements Serializable {

    //
    //  Auth routines.
    //

    /**
     * Returns principal of the current logged in user, possibly {@code null}.
     */
    protected Principal getUserPrincipal() {
        return FacesContext.currentInstance.externalContext.userPrincipal
    }

    boolean isAuthenticated() { userPrincipal != null }

    private boolean inRole(Role role) {
        final ExternalContext context = FacesContext.currentInstance.externalContext
        return context.isUserInRole(role.name)
    }

    public String getUserName() { FacesContext.currentInstance.externalContext.remoteUser }

    public boolean isClientRole() {
        return inRole(Role.CLIENT)
    }

    public boolean isManagerRole() {
        return inRole(Role.MANAGER)
    }

    public boolean isAdminRole() {
        return inRole(Role.ADMIN)
    }

    //
    //  Request/session routines.
    //

    /**
     * Returns the current HTTP session.
     *
     * @param createIfNeeded Specifies if the session should be created if absent.
     */
    protected HttpSession getHttpSession(boolean createIfNeeded) {
        final ExternalContext context = FacesContext.currentInstance.externalContext
        return context.getSession(createIfNeeded) as HttpSession
    }

    protected RequestParam getParam() {
        return new RequestParam(request)
    }

    RequestParam.Value getParamValue(String name) {
        return getParam().get(name)
    }

    HttpServletRequest getRequest() {
        final ExternalContext context = FacesContext.currentInstance.externalContext
        return context.request as HttpServletRequest
    }

    private StringBuilder getBaseUrl() {
        StringBuilder sb = new StringBuilder()
        sb.append(getRequest().getScheme()).append("://")
                .append(getRequest().getServerName())
        if (getRequest().getServerPort() != 80) {
            sb.append(":").append(getRequest().getServerPort())
        }
        sb.append(getRequest().getContextPath())
        return sb
    }

    //
    //  Validation routines.
    //

    void addFacesMessage(ConstraintViolation<?> violation) {
        addValidationError(violation.getMessage(), violation.getPropertyPath().toString())
    }

    void addFacesMessage(ConstraintViolation<?> violation,
                         Map<String, String> fieldNamesMapping) {
        String pageFieldId = fieldNamesMapping[violation.propertyPath.toString()]
        if (pageFieldId == null)
            pageFieldId = violation.propertyPath.toString()
        addValidationError(violation.message, pageFieldId)
    }

    void addValidationError(String message, String id) {
        def facesMessage = new FacesMessage(
                severity: FacesMessage.SEVERITY_ERROR,
                summary: message,
                detail: message)
        FacesContext.currentInstance.addMessage(id, facesMessage)
    }

    void addErrorMessage(String message) {
        addValidationError(message, "")
    }

    void addErrorMessage(String message, String id) {
        FacesMessage facesMessage = new FacesMessage()
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR)
        facesMessage.setSummary(message)
        facesMessage.setDetail(message)
        FacesContext.getCurrentInstance().addMessage(id, facesMessage)
    }

    public void addValidationError(String message) {
        addValidationError(message, "")
    }

    public void addInfoMessage(String message, String id) {
        FacesMessage facesMessage = new FacesMessage()
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO)
        facesMessage.setSummary(message)
        facesMessage.setDetail(message)
        FacesContext.getCurrentInstance().addMessage(id, facesMessage)
    }

    public void addInfoMessage(String message) {
        addInfoMessage(message, "")
    }

    boolean renderViolationMessage(Set<ConstraintViolation<?>> violations,
                                   Map<String, String> fieldNamesMapping) {

        return !violations
                .each { addFacesMessage(it, fieldNamesMapping) }
                .empty
    }

    boolean renderViolationMessage(Set<ConstraintViolation<?>> violations) {
        renderViolationMessage(violations, emptyMap())
    }
}
