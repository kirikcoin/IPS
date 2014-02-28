package mobi.eyeline.ips.messages;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public class UssdResponseModel {

    /**
     * Generic non-localized messages.
     */
    public static final ResourceBundle USSD_BUNDLE = ResourceBundle.getBundle("messages");

    private final String text;
    private final List<UssdOption> options;

    public UssdResponseModel(String text, List<? extends UssdOption> options) {
        this.text = requireNonNull(text);
        this.options = Collections.unmodifiableList(options);
    }

    public UssdResponseModel(String text) {
        this(text, Collections.<UssdOption>emptyList());
    }

    public String getText() {
        return text;
    }

    public List<UssdOption> getOptions() {
        return options;
    }

    /**
     * Model with no options, just text.
     */
    public static class TextUssdResponseModel extends UssdResponseModel {
        public TextUssdResponseModel(String text) {
            super(text);
        }
    }

    /**
     * Indicates that request asks for an unavailable survey.
     */
    public static class NoSurveyResponseModel extends TextUssdResponseModel {
        public NoSurveyResponseModel() {
            super(USSD_BUNDLE.getString("ussd.survey.not.available"));
        }
    }

    /**
     * Indicates some kind of request handling error.
     */
    public static class ErrorResponseModel extends TextUssdResponseModel {
        public ErrorResponseModel() {
            super(USSD_BUNDLE.getString("ussd.error"));
        }
    }
}
