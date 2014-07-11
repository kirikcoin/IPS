package mobi.eyeline.ips.web;




import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * author: Denis Enenko
 * date: 22.01.2014
 */
public class IPSViewHandler extends ViewHandlerWrapper
{
  public final static String LOCALE_PARAMETER = "ips_user_locale";

    private ViewHandler wrappedViewHandler;

    public IPSViewHandler(ViewHandler wrappedViewHandler) {
      this.wrappedViewHandler = wrappedViewHandler;
    }

    @Override
    public ViewHandler getWrapped() {
        return wrappedViewHandler;
    }

  @Override
  public Locale calculateLocale(FacesContext context) {
    Locale locale = (Locale) ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getAttribute(LOCALE_PARAMETER);
    return locale != null ? locale : super.calculateLocale(context);
  }
}
