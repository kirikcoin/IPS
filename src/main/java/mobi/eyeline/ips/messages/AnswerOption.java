package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.model.QuestionOption;

import java.util.HashMap;
import java.util.Map;

import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.ANSWER;
import static mobi.eyeline.ips.util.RequestParseUtils.getBoolean;
import static mobi.eyeline.ips.util.RequestParseUtils.getInt;

public class AnswerOption extends UssdOption {

    private final int questionId;
    private final int answerId;

    public static final String PARAM_QUESTION_ID    = "questionId";
    public static final String PARAM_ANSWER_ID      = "answerId";

    private AnswerOption(int key,
                         String text,
                         boolean skipValidation,
                         int surveyId,
                         int questionId,
                         int answerId) {

        super(key, text, skipValidation, surveyId, ANSWER);
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public AnswerOption(int key,
                        QuestionOption option,
                        boolean skipValidation) {
        this(
                key,
                option.getAnswer(),
                skipValidation,
                option.getQuestion().getSurvey().getId(),
                option.getQuestion().getId(),
                option.getId()
        );

        assert option.isActive() : "Sending inactive answer option";
    }

    public int getAnswerId() {
        return answerId;
    }

    @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<>(super.getProperties());

        properties.put(PARAM_QUESTION_ID, questionId);
        properties.put(PARAM_ANSWER_ID, answerId);

        return properties;
    }

    @Override
    public UssdResponseModel handle(String msisdn, MessageHandler handler) {
        return handler.handle(msisdn, this);
    }

    public static AnswerOption parse(Map<String, String[]> options)
            throws MissingParameterException {
        return new AnswerOption(
                -1,
                null,
                getBoolean(options, PARAM_SKIP_VALIDATION, false),
                getInt(options, PARAM_SURVEY_ID),
                getInt(options, PARAM_QUESTION_ID),
                getInt(options, PARAM_ANSWER_ID)
        );
    }
}
