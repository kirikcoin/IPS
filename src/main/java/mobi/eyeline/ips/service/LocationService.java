package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.UiProfile;
import mobi.eyeline.ips.properties.LocationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final List<LocationProperties> locations;
    private final String defaultLoginUrl;
    private UiProfile.Skin skin;

    public LocationService(List<LocationProperties> locations,
                           String defaultLoginUrl) {

        this.locations = locations;
        this.defaultLoginUrl = defaultLoginUrl;
        this.skin = UiProfile.Skin.ARAKS;
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

    public String getSkin() {
        return skin.toString().toLowerCase();
    }

    public void setSkin(UiProfile.Skin skin) {
        this.skin = skin;
    }


    private LocationProperties getLocationProperties() {
        try {
            return getLocationProperties0();
        } catch (Exception e) {
            logger.error("Could not determine location", e);
            return null;
        }
    }

    private LocationProperties getLocationProperties0() throws Exception {
        final String baseUrl = getRequest().getServerName();
        for (LocationProperties location : locations) {
            if (location.getCompiledPattern().matcher(baseUrl).matches()) {
                return location;
            }
        }

        throw new Exception("Unknown location: [" + baseUrl + "]");
    }

    private HttpServletRequest getRequest() {
        final ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        return (HttpServletRequest) externalContext.getRequest();
    }
}
