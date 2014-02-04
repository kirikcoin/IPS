package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.web.IPSViewHandler

import javax.faces.context.FacesContext
import javax.servlet.http.HttpServletRequest

/**
 * author: Denis Enenko
 * date: 22.01.2014
 */
public class LocaleController extends BaseController {

    public void changeLocale() {
        Locale locale = new Locale(getParamValue("lang").asString())

        FacesContext.currentInstance.viewRoot.locale = locale
        request.session.setAttribute(IPSViewHandler.LOCALE_PARAMETER, locale)

        FacesContext.currentInstance.externalContext.redirect(getFullUrl())
    }

    private String getFullUrl() {
        // XXX: need to filter parameters here.
        def context = FacesContext.currentInstance.externalContext

        def searchParams = (context.request as HttpServletRequest).getParameter("param")
        String search =
                searchParams?.replaceAll('\\{', '?')?.replaceAll(', ', '&')?.replaceAll('}', '')

        return context.requestServletPath + (search.length() <= 1 ? '' : search)
    }
}
