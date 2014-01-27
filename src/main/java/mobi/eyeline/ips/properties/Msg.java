package mobi.eyeline.ips.properties;


import java.io.IOException;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: maxim.romanovsky
 * Date: 12.04.11
 * Time: 14:44
 */
public class Msg extends ResourceBundleController {
    private static Msg _instance = null;

    static public String getFmt(Locale locale, String messageName, Object... args) {
        String result;
        try {
            String text = instance().loadProperty(messageName, locale);
            result = String.format(text, args);
        } catch (IOException e) {
            result = "not found message with name \"" + messageName + "\", because " + e.getMessage();
        }
        return result;
    }

    public Msg() {
        super("messages");
    }

    public static Msg instance() {
        if (_instance == null)
            _instance = new Msg();
        return _instance;
    }


}
