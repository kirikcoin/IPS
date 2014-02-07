package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.model.QuestionOption;

import java.util.HashMap;
import java.util.Map;

import static mobi.eyeline.ips.messages.ParseUtils.getInt;
import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.ANSWER;

public class AnswerOption extends UssdOption {

    private final int questionId;
    private final int answerId;

    private AnswerOption(int key,
                         String text,
                         int surveyId,
                         int questionId,
                         int answerId) {

        super(key, text, surveyId, ANSWER);
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public AnswerOption(int key,
                        QuestionOption option) {
        this(
                key,
                option.getAnswer(),
                option.getQuestion().getSurvey().getId(),
                option.getQuestion().getId(),
                option.getId()
        );
    }

    public int getQuestionId() {
        return questionId;
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
                getInt(options, "survey_id"),
                getInt(options, "questionId"),
                getInt(options, "answerId")
        );
    }
}
