package mobi.eyeline.ips.web.validators;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import javax.faces.context.FacesContext;
import java.util.Locale;

public class LocalizedMessageInterpolator extends ResourceBundleMessageInterpolator {

    public LocalizedMessageInterpolator(String bundleName) {
        super(new PlatformResourceBundleLocator(bundleName));
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        final Locale locale =
                FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return interpolate(messageTemplate, context, locale);
    }

}
