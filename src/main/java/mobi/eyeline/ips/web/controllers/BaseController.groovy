package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.util.DelegateResourceBundle
import mobi.eyeline.ips.util.RequestParam
import mobi.eyeline.ips.web.auth.WebUser
import mobi.eyeline.ips.web.validators.LocalizedMessageInterpolator

import javax.faces.application.FacesMessage
import javax.faces.context.ExternalContext
import javax.faces.context.FacesContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

import static java.util.Collections.emptyMap

@CompileStatic
@Slf4j('logger')
public abstract class BaseController implements Serializable {

    private final UserRepository userRepository = Services.instance().userRepository

    BaseController() {
        logger.trace("Controller instantiated: [${this.class.name}]")
    }

    //
    //  Auth routines.
    //

    /**
     * Returns principal of the current logged in user, possibly {@code null}.
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    protected WebUser getUserPrincipal() {
        return FacesContext.currentInstance.externalContext.userPrincipal as WebUser
    }

    boolean isAuthenticated() { userPrincipal != null }

    @SuppressWarnings("GrMethodMayBeStatic")
    boolean inRole(Role role) {
        final ExternalContext context = FacesContext.currentInstance.externalContext
        return context.isUserInRole(role.name)
    }

    User getCurrentUser() {userRepository.load(userPrincipal.id)}

    /**
     * Returns current request locale, which may differ from the user's one.
     *
     * <p>Use this snippet to access persisted user locale:
     * <code>getCurrentUser().getLocale().asLocale()</code>
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    Locale getLocale() { FacesContext.currentInstance.viewRoot.locale }

    TimeZone getTimeZone() { TimeZone.getTimeZone(getCurrentUser().getTimeZoneId()) }

    boolean isClientRole() { inRole(Role.CLIENT) }

    boolean isManagerRole() { inRole(Role.MANAGER) }

    boolean isAdminRole() { return inRole(Role.ADMIN) }

    //
    //  Request/session routines.
    //

    /**
     * Returns the current HTTP session.
     *
     * @param createIfNeeded Specifies if the session should be created if absent.
     */
    @SuppressWarnings("GrMethodMayBeStatic")
    protected HttpSession getHttpSession(boolean createIfNeeded) {
        final ExternalContext context = FacesContext.currentInstance.externalContext
        return context.getSession(createIfNeeded) as HttpSession
    }

    @SuppressWarnings("UnnecessaryQualifiedReference")
    protected mobi.eyeline.ips.util.RequestParam getParam() {
        return new RequestParam(request)
    }

    @SuppressWarnings("UnnecessaryQualifiedReference")
    mobi.eyeline.ips.util.RequestParam.Value getParamValue(String name) {
        return getParam().get(name)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
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
        addValidationError(violation.message, violation.propertyPath.toString())
    }

    void addFacesMessage(ConstraintViolation<?> violation,
                         Map<String, String> fieldNamesMapping) {
        def property = violation.propertyPath.toString()
        addValidationError(violation.message, fieldNamesMapping[property] ?: property)
    }

    void addValidationError(String message, String id) {
        addErrorMessage(message, id)
    }

    void addValidationError(String message) {
        addValidationError(message, "")
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    void addErrorMessage(String message, String id) {
        def facesMessage = new FacesMessage(
                severity: FacesMessage.SEVERITY_ERROR,
                summary: message,
                detail: message)
        FacesContext.currentInstance.addMessage(id, facesMessage)
    }

    void addErrorMessage(String message) {
        addValidationError(message, "")
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    public void addInfoMessage(String message, String id) {
        def facesMessage = new FacesMessage(
                severity: FacesMessage.SEVERITY_INFO,
                summary: message,
                detail: message)
        FacesContext.currentInstance.addMessage(id, facesMessage)
    }

    public void addInfoMessage(String message) {
        addInfoMessage(message, "")
    }

    boolean renderViolationMessage(Set<ConstraintViolation<?>> violations,
                                   Map<String, String> fieldNamesMapping) {

        violations.each { addFacesMessage(it, fieldNamesMapping) }
    }

    boolean renderViolationMessage(Set<ConstraintViolation<?>> violations) {
        renderViolationMessage(violations, emptyMap())
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    Validator getValidator() {
        return Validation
                .byDefaultProvider()
                .configure()
                .messageInterpolator(new LocalizedMessageInterpolator("ips"))
                .buildValidatorFactory()
                .validator
    }

    static IndexedBundle getStrings() {
        def context = FacesContext.currentInstance
        return new IndexedBundle(context.application.getResourceBundle(context, "bundle"))
    }

    static class IndexedBundle extends DelegateResourceBundle {
        IndexedBundle(ResourceBundle delegate) {
            super(delegate)
        }

        String getAt(String key) { getString(key) }
    }

    static <T> T beanByName(String name, Class<T> clazz) {
        def context = FacesContext.currentInstance
        (T) context.application.evaluateExpressionGet(context, '#{' + name + '}', clazz)
    }
}
