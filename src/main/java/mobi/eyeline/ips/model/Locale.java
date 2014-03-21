package mobi.eyeline.ips.model;

public enum Locale {
    EN,
    RU;

    public java.util.Locale asLocale() {
        return new java.util.Locale(name());
    }
}
