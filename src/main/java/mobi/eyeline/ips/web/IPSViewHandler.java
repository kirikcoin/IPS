package mobi.eyeline.ips.web;


import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class IPSViewHandler extends ViewHandlerWrapper {

    public final static String LOCALE_PARAMETER = "ips_user_locale";

    private final ViewHandler delegate;

    public IPSViewHandler(ViewHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public ViewHandler getWrapped() {
        return delegate;
    }

    @Override
    public Locale calculateLocale(FacesContext context) {
        final FacesContext ctx = FacesContext.getCurrentInstance();
        final HttpSession session =
                ((HttpServletRequest) ctx.getExternalContext().getRequest()).getSession();
        final Locale locale = (Locale) session.getAttribute(LOCALE_PARAMETER);

        return (locale != null) ? locale : super.calculateLocale(context);
    }
}
