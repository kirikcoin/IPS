package mobi.eyeline.ips.web.controllers;


import mobi.eyeline.ips.web.IPSViewHandler;

import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * author: Denis Enenko
 * date: 22.01.2014
 */
public class LocaleController extends IPSController
{
  public void changeLocale() {
    String language = getParamValue("lang").asString();
    Locale locale = new Locale(language);
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    getRequest().getSession().setAttribute(IPSViewHandler.LOCALE_PARAMETER, locale);
  }
}
