package mobi.eyeline.ips.service;

public class SkinService {

    private final String defaultSkin;

    public SkinService(String defaultSkin) {
        this.defaultSkin = defaultSkin;
    }

    public String getDefaultSkin() {
        return defaultSkin;
    }
}
