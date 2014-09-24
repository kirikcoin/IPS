package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.UiProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import static mobi.eyeline.ips.model.UiProfile.Skin.MOBAK;

public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final String defaultLoginUrl;
    private UiProfile.Skin skin;

    public LocationService(String defaultLoginUrl) {
        this.defaultLoginUrl = defaultLoginUrl;
        this.skin = MOBAK;
    }

    public String getLoginUrl() {
        try {
            final StringBuffer requestURL = getRequest().getRequestURL();
            return requestURL.substring(0, requestURL.lastIndexOf(getRequest().getRequestURI()));

        } catch (Exception e) {
            logger.error("Could not determine login url", e);
            return defaultLoginUrl;
        }
    }

    public UiProfile.Skin getSkin() {
        return skin;
    }

    public void setSkin(UiProfile.Skin skin) {
        this.skin = skin;
    }


    private HttpServletRequest getRequest() {
        final ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        return (HttpServletRequest) externalContext.getRequest();
    }
}
