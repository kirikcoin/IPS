package mobi.eyeline.ips.messages;

import java.util.LinkedHashMap;
import java.util.Map;

import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.ANSWER;
import static mobi.eyeline.ips.util.RequestParseUtils.getBoolean;
import static mobi.eyeline.ips.util.RequestParseUtils.getInt;
import static mobi.eyeline.ips.util.RequestParseUtils.getString;

public class BadCommandOption extends UssdOption {

    private final int questionId;
    private final int answerId;
    private final String answerText;

    public static final String PARAM_QUESTION_ID    = "questionId";
    public static final String PARAM_ANSWER_ID      = "answerId";


    private BadCommandOption(int key,
                             String text,
                             boolean skipValidation,
                             int surveyId,
                             int questionId,
                             int answerId,
                             String answerText) {

        super(key, text, skipValidation, surveyId, ANSWER);
        this.questionId = questionId;
        this.answerId = answerId;
        this.answerText = answerText;
    }

    @Override
    public UssdResponseModel handle(String msisdn, MessageHandler handler) {
        return handler.handle(msisdn, this);
    }

    @Override
    public Map<String, Object> getProperties() {
        return new LinkedHashMap<String, Object>(super.getProperties()) {{
            put(PARAM_QUESTION_ID, questionId);
            put(PARAM_ANSWER_ID, answerId);
            put(PARAM_BAD_COMMAND, answerText);
        }};
    }

    public int getAnswerId() {
        return answerId;
    }

    public int getQuestionId(){
        return questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    @Override
    public String toString() {
        return "BadCommandOption{" +
                "questionId=" + questionId +
                ", answerId=" + answerId +
                '}';
    }

    public static BadCommandOption parse(Map<String, String[]> options)
            throws MissingParameterException {
        return new BadCommandOption(
                -1,
                null,
                getBoolean(options, PARAM_SKIP_VALIDATION, false),
                getInt(options, PARAM_SURVEY_ID),
                getInt(options, PARAM_QUESTION_ID, -1),
                getInt(options, PARAM_ANSWER_ID, -1),
                getString(options, PARAM_BAD_COMMAND)
        );
    }
}
