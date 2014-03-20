package mobi.eyeline.ips.model;

/**
 * Created by dizan on 20.03.14.
 */
public enum Locale {
    EN,
    RU;

    public java.util.Locale asLocale() {
        return new java.util.Locale(name());
    }
}
