package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.util.RequestParseUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class UssdOption {

    public static final String PARAM_SURVEY_ID          = "survey_id";

    /**
     * If set to {@code true}, we won't check if survey is active (in terms of begin/end dates).
     * Deleted surveys will be handled as is.
     */
    public static final String PARAM_SKIP_VALIDATION    = "skip_validation";

    public static final String PARAM_MSISDN             = "abonent";

    public static final String PARAM_MESSAGE_TYPE       = "type";

    public static final String PARAM_BAD_COMMAND        = "bad_command";

    private final int key;
    private final String text;
    private final boolean skipValidation;

    private final UssdOptionType type;

    private final int surveyId;

    private final String linkType;

    protected UssdOption(int key,
                         String text,
                         boolean skipValidation,
                         int surveyId,
                         UssdOptionType type) {
        this(key, text, skipValidation, surveyId, type, null);
    }

    protected UssdOption(int key,
                         String text,
                         boolean skipValidation,
                         int surveyId,
                         UssdOptionType type,
                         String linkType) {
        this.key = key;
        this.text = text;
        this.skipValidation = skipValidation;
        this.type = type;
        this.surveyId = surveyId;
        this.linkType = linkType;
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

    public String getLinkType() {
        return linkType;
    }

    public Map<String, Object> getProperties() {
        return new LinkedHashMap<String, Object>() {{
            put("type", type.name());
            put(PARAM_SURVEY_ID, surveyId);
            put(PARAM_SKIP_VALIDATION, skipValidation);
        }};
    }

    public String getUri() {
        final URIBuilder builder = new URIBuilder();
        for (Map.Entry<String, Object> entry : getProperties().entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue().toString());
        }

        try {
            return builder.build().getRawQuery();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSkipValidation() {
        return skipValidation;
    }

    public abstract UssdResponseModel handle(String msisdn, MessageHandler handler);

    @Override
    public String toString() {
        return "UssdOption{" +
                "key=" + key +
                ", text='" + text + '\'' +
                ", surveyId=" + surveyId +
                '}';
    }

    /**
     * @return {@code null} if the provided options do not match any valid message,
     *  {@code BadCommandOption} if the provided options have 'bad_command' parameter
     *  or AnswerOption if options have type parameter equals 'ANSWER'.
     *  @throws java.lang.AssertionError if type equals another value.
     *
     */
    public static UssdOption parse(Map<String, String[]> options)
            throws MissingParameterException {

        if(options.get(PARAM_BAD_COMMAND) != null) {
            return BadCommandOption.parse(options);
        }

        if (options.get(PARAM_MESSAGE_TYPE) == null) {
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
