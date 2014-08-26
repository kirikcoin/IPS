package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.web.IPSViewHandler

import javax.faces.context.FacesContext
import javax.servlet.http.HttpServletRequest

class LocaleController extends BaseController {

    void changeLocale() {
        Locale locale = new Locale(getParamValue('lang').asString())
        setLocaleAttributes(locale)

        FacesContext.currentInstance.externalContext.redirect(getFullUrl())
    }

    private void setLocaleAttributes(Locale locale) {
        FacesContext.currentInstance.viewRoot.locale = locale
        request.session.setAttribute(IPSViewHandler.LOCALE_PARAMETER, locale)
    }

    void changeLocale(User user) {
        setLocaleAttributes(user.locale.asLocale())
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private String getFullUrl() {
        def context = FacesContext.currentInstance.externalContext

        def id = (context.request as HttpServletRequest)
                .getParameter("param")
                .split('\\{|\\}|, ')
                .find {it.startsWith('id=')}

        return context.requestServletPath + ((id != null) ? "?${id}" : '')
    }
}
