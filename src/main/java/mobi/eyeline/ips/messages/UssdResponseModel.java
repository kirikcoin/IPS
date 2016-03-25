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

  private final boolean defaultAnswerEnabled;
  private final int surveyId;

  protected UssdResponseModel(String text,
                              List<UssdOption> options,
                              String performRedirect,
                              boolean defaultAnswerEnabled,
                              int surveyId) {
    this.text = text;
    this.options = options;
    this.redirectUrl = performRedirect;
    this.defaultAnswerEnabled = defaultAnswerEnabled;
    this.surveyId = surveyId;
  }

  protected UssdResponseModel(String text,
                              List<UssdOption> options,
                              String performRedirect,
                              int surveyId) {
    this(text, options, performRedirect, false, surveyId);
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

  public boolean isDefaultAnswerEnabled() {
    return defaultAnswerEnabled;
  }

  public int getSurveyId() {
    return surveyId;
  }

  /**
   * Model with no options, just text.
   */
  public static class TextUssdResponseModel extends UssdResponseModel {
    public TextUssdResponseModel(String text, int surveyId) {
      super(text, Collections.<UssdOption>emptyList(), null, surveyId);
    }
  }

  /**
   * Model with no options, just text.
   */
  public static class OptionsResponseModel extends UssdResponseModel {
    public OptionsResponseModel(String text, List<? extends UssdOption> options, int surveyId) {
      this(text, options, false, surveyId);
    }

    public OptionsResponseModel(String text,
                                List<? extends UssdOption> options,
                                boolean defaultAnswerEnabled,
                                int surveyId) {
      super(text, Collections.unmodifiableList(options), null, defaultAnswerEnabled, surveyId);
    }
  }

  /**
   * Indicates that request asks for an unavailable survey.
   */
  public static class NoSurveyResponseModel extends TextUssdResponseModel {
    public NoSurveyResponseModel(int surveyId) {
      super(USSD_BUNDLE.getString("ussd.survey.not.available"), surveyId);
    }
  }

  /**
   * Perform redirect.
   */
  public static class RedirectUssdResponseModel extends UssdResponseModel {
    public RedirectUssdResponseModel(String url, int surveyId) {
      super(null, Collections.<UssdOption>emptyList(), url, surveyId);
    }
  }

  /**
   * Indicates some kind of request handling error.
   */
  public static class ErrorResponseModel extends TextUssdResponseModel {
    public ErrorResponseModel(int surveyId) {
      super(USSD_BUNDLE.getString("ussd.error"), surveyId);
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
