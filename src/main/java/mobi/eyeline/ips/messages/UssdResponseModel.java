package mobi.eyeline.ips.messages;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class UssdResponseModel {

  /**
   * Generic non-localized messages.
   */
  public static final ResourceBundle USSD_BUNDLE = ResourceBundle.getBundle("messages");

  private final String text;
  private final List<UssdOption> options;

  /** If non-null, redirect to the specified URL. */
  private final String redirectUrl;

  protected UssdResponseModel(String text, List<UssdOption> options, String performRedirect) {
    this.text = text;
    this.options = options;
    this.redirectUrl = performRedirect;
  }

  public String getText() {
    return text;
  }

  public List<UssdOption> getOptions() {
    return options;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  /**
   * Model with no options, just text.
   */
  public static class TextUssdResponseModel extends UssdResponseModel {
    public TextUssdResponseModel(String text) {
      super(text, Collections.<UssdOption>emptyList(), null);
    }
  }

  /**
   * Model with no options, just text.
   */
  public static class OptionsResponseModel extends UssdResponseModel {
    public OptionsResponseModel(String text, List<? extends UssdOption> options) {
      super(text, Collections.unmodifiableList(options), null);
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
   * Perform redirect.
   */
  public static class RedirectUssdResponseModel extends UssdResponseModel {
    public RedirectUssdResponseModel(String url) {
      super(null, Collections.<UssdOption>emptyList(), url);
    }
  }

  /**
   * Indicates some kind of request handling error.
   */
  public static class ErrorResponseModel extends TextUssdResponseModel {
    public ErrorResponseModel() {
      super(USSD_BUNDLE.getString("ussd.error"));
    }

    @Override
    public String toString() {
      return "UssdResponseModel{FATAL_ERROR}";
    }
  }

  @Override
  public String toString() {
    return "UssdResponseModel{" +
        "text='" + text + '\'' +
        ", options=" + options +
        '}';
  }
}
