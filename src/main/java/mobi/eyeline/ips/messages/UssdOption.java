package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.util.RequestParseUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class UssdOption {
    private final int key;
    private final String text;

    private final UssdOptionType type;

    private final int surveyId;

    protected UssdOption(int key,
                         String text,
                         int surveyId,
                         UssdOptionType type) {
        this.key = key;
        this.text = text;
        this.type = type;
        this.surveyId = surveyId;
    }

    public int getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{
            put("type", type.name());
            put("survey_id", surveyId);
        }};
    }

    public String getUri() {
        final StringBuilder uri = new StringBuilder();
        for (Map.Entry<String, Object> entry : getProperties().entrySet()) {
            uri.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        if (uri.length() != 0) {
            uri.replace(uri.length() - 1, uri.length(), "");
        }

        return uri.toString();
    }

    public abstract UssdModel handle(String msisdn, MessageHandler handler);

    /**
     * @return {@code null} if the provided options do not match any valid message.
     */
    public static UssdOption parse(Map<String, String[]> options)
            throws MissingParameterException {

        if (options.get("type") == null) {
            return null;
        }

        final UssdOptionType type =
                UssdOptionType.valueOf(RequestParseUtils.getString(options, "type"));

        switch (type) {
            case ANSWER:                    return AnswerOption.parse(options);
            default:                        throw new AssertionError();
        }
    }

    public static enum UssdOptionType {

        /**
         * Answer to a question.
         */
        ANSWER
    }
}
