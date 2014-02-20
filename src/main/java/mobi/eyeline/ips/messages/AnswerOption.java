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
        final Map<String, Object> properties = new HashMap<>();

        properties.putAll(super.getProperties());

        properties.put("questionId", questionId);
        properties.put("answerId", answerId);

        return properties;
    }

    @Override
    public UssdModel handle(String msisdn, MessageHandler handler) {
        return handler.handle(msisdn, this);
    }

    public static AnswerOption parse(Map<String, String[]> options)
            throws MissingParameterException {
        return new AnswerOption(
                -1,
                null,
                getBoolean(options, PARAM_SKIP_VALIDATION),
                getInt(options, PARAM_SURVEY_ID),
                getInt(options, "questionId"),
                getInt(options, "answerId")
        );
    }
}
