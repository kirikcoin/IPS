package mobi.eyeline.ips.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 26.05.11
 * Time: 14:35
 */
public class AvailableLocales {
  public static class ExtLocale {
    public String caption;
    public Locale locale;

    public ExtLocale(String language, String caption) {
      this.locale = new Locale(language);
      this.caption = caption;
    }

    public ExtLocale(Locale locale, String caption) {
      this.locale = locale;
      this.caption = caption;
    }

    public String code() {
      return locale.getLanguage();
    }
  }

  private static List<ExtLocale> _locales = null;

  public static ExtLocale defaultLocale() {
    locales();
    return _defaultLocale;
  }

  private static ExtLocale _defaultLocale;

  static public List<ExtLocale> locales() {
    if (_locales == null) {
      ExtLocale englishLocale = new ExtLocale(Locale.ENGLISH, "English");
      ExtLocale russianLocale = new ExtLocale("ru", "Русский");
      _defaultLocale = russianLocale;
      _locales = new ArrayList<ExtLocale>();
      _locales.add(englishLocale);
      _locales.add(russianLocale);
    }
    return _locales;
  }

  static public ExtLocale findLocale(String code) {
    if (code != null) {
      for (ExtLocale locale : locales())
        if (code.equals(locale.code()))
          return locale;
    }
    return _defaultLocale;
  }

  static public String findExistCode(List<String> codes) {
    for (String code : codes) {
      if (code != null) {
        for (ExtLocale locale : locales())
          if (code.equals(locale.code()))
            return code;
      }
    }
    return "";
  }
}
