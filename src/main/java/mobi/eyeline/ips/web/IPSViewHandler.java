package mobi.eyeline.ips.web;


import mobi.eyeline.util.jsf.components.TrinidadAlternateViewHandler;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * author: Denis Enenko
 * date: 22.01.2014
 */
public class IPSViewHandler extends TrinidadAlternateViewHandler
{
  public final static String LOCALE_PARAMETER = "ips_user_locale";


  public IPSViewHandler(ViewHandler parent) {
    super(parent);
  }

  @Override
  public Locale calculateLocale(FacesContext context) {
    Locale locale = (Locale) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute(LOCALE_PARAMETER);
    return locale != null ? locale : super.calculateLocale(context);
  }
}
