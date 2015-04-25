package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.model.QuestionOption;

import java.util.LinkedHashMap;
import java.util.Map;

import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.ANSWER;
import static mobi.eyeline.ips.util.RequestParseUtils.getBoolean;
import static mobi.eyeline.ips.util.RequestParseUtils.getInt;

public class AnswerOption extends UssdOption {

  private final int questionId;
  private final int answerId;

  public static final String PARAM_QUESTION_ID = "questionId";
  public static final String PARAM_ANSWER_ID = "answerId";

  private AnswerOption(int key,
                       String text,
                       boolean skipValidation,
                       int surveyId,
                       int questionId,
                       int answerId,
                       String linkType) {

    super(key, text, skipValidation, surveyId, ANSWER, linkType);
    this.questionId = questionId;
    this.answerId = answerId;
  }

  public AnswerOption(int key,
                      QuestionOption option,
                      boolean skipValidation,
                      boolean isExitLink) {
    this(
        key,
        option.getAnswer(),
        skipValidation,
        option.getQuestion().getSurvey().getId(),
        option.getQuestion().getId(),
        option.getId(),
        isExitLink ? "exit" : null);

    assert !option.isDeleted() : "Sending deleted answer option";
  }

  public int getAnswerId() {
    return answerId;
  }

  @Override
  public Map<String, Object> getProperties() {
    return new LinkedHashMap<String, Object>(super.getProperties()) {{
      put(PARAM_QUESTION_ID, questionId);
      put(PARAM_ANSWER_ID, answerId);
    }};
  }

  @Override
  public UssdResponseModel handle(String msisdn,
                                  MessageHandler handler,
                                  OuterRequest outerRequest) {
    return handler.handle(msisdn, this, outerRequest);
  }

  @Override
  public String toString() {
    return "AnswerOption{" +
        "questionId=" + questionId +
        ", answerId=" + answerId +
        '}';
  }

  public static AnswerOption parse(Map<String, String[]> options)
      throws MissingParameterException {
    return new AnswerOption(
        -1,
        null,
        getBoolean(options, PARAM_SKIP_VALIDATION, false),
        getInt(options, PARAM_SURVEY_ID),
        getInt(options, PARAM_QUESTION_ID),
        getInt(options, PARAM_ANSWER_ID),
        null);
  }
}
