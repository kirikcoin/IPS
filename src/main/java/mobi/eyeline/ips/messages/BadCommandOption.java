package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.util.RequestParseUtils;

import java.util.HashMap;
import java.util.Map;

import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.ANSWER;
import static mobi.eyeline.ips.util.RequestParseUtils.getBoolean;
import static mobi.eyeline.ips.util.RequestParseUtils.getInt;

public class BadCommandOption extends UssdOption {

    private final int questionId;
    private final int answerId;

    public static final String PARAM_QUESTION_ID    = "questionId";
    public static final String PARAM_ANSWER_ID      = "answerId";

    private BadCommandOption(int key,
                             String text,
                             boolean skipValidation,
                             int surveyId,
                             int questionId,
                             int answerId) {

        super(key, text, skipValidation, surveyId, ANSWER);
        this.questionId = questionId;
        this.answerId = answerId;
    }

    @Override
    public UssdResponseModel handle(String msisdn, MessageHandler handler) {
        return handler.handle(msisdn, this);
    }

    @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<>(super.getProperties());

        properties.put(PARAM_QUESTION_ID, questionId);
        properties.put(PARAM_ANSWER_ID, answerId);

        return properties;
    }

    public int getAnswerId() {
        return answerId;
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
                getInt(options, PARAM_QUESTION_ID),
                getInt(options, PARAM_ANSWER_ID)
        );
    }
}