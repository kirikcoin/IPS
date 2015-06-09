package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.web.IPSViewHandler

import javax.enterprise.context.SessionScoped
import javax.enterprise.inject.Model
import javax.faces.context.FacesContext

@Model
@SessionScoped
class LocaleController extends BaseController {

  void changeLocale(String localeName) {
    setLocaleAttributes(new Locale(localeName))
  }

  private void setLocaleAttributes(Locale locale) {
    FacesContext.currentInstance.viewRoot.locale = locale
    request.session.setAttribute(IPSViewHandler.LOCALE_PARAMETER, locale)
  }

  void changeLocale(User user) {
    setLocaleAttributes(user.locale.asLocale())
  }
}
