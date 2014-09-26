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
        return defaultLoginUrl;
    }

    public UiProfile.Skin getSkin() {
        return skin;
    }

    public void setSkin(UiProfile.Skin skin) {
        this.skin = skin;
    }

}
