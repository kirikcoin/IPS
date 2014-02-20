package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.util.RequestParseUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class UssdOption {

    public static final String PARAM_SURVEY_ID          = "survey_id";

    /**
     * If set to {@code true}, we won't check if survey is active (in terms of begin/end dates).
     * Deleted surveys will be handled as is.
     */
    public static final String PARAM_SKIP_VALIDATION    = "skip_validation";

    private final int key;
    private final String text;
    private final boolean skipValidation;

    private final UssdOptionType type;

    private final int surveyId;

    protected UssdOption(int key,
                         String text,
                         boolean skipValidation,
                         int surveyId,
                         UssdOptionType type) {
        this.key = key;
        this.text = text;
        this.skipValidation = skipValidation;
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
            put(PARAM_SURVEY_ID, surveyId);
            put(PARAM_SKIP_VALIDATION, skipValidation);
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

    public boolean isSkipValidation() {
        return skipValidation;
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
