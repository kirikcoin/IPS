package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.model.Survey;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static mobi.eyeline.ips.messages.ParseUtils.getInt;
import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.REGISTRATION_ACCEPTED;
import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.REGISTRATION_DECLINED;

public abstract class RegistrationOption extends UssdOption {

    protected RegistrationOption(int key,
                                 String text,
                                 int surveyId,
                                 UssdOptionType type) {
        super(key, text, surveyId, type);
    }

    public static List<RegistrationOption> getDefaultOptions(final Survey survey,
                                                             String textYes,
                                                             String textNo) {
        return Arrays.asList(
                new RegistrationAccepted(1, textYes, survey.getId()),
                new RegistrationDeclined(2, textNo, survey.getId())
        );
    }

    public static class RegistrationAccepted extends RegistrationOption {

        public RegistrationAccepted(int key, String text, int surveyId) {
            super(key, text, surveyId, REGISTRATION_ACCEPTED);
        }

        public static RegistrationAccepted parse(Map<String, String[]> options)
                throws MissingParameterException {
            return new RegistrationAccepted(
                    -1,
                    null,
                    getInt(options, "survey_id")
            );
        }

        @Override
        public UssdModel handle(String msisdn, MessageHandler handler) {
            return handler.handle(msisdn, this);
        }
    }

    public static class RegistrationDeclined extends RegistrationOption {

        public RegistrationDeclined(int key, String text, int surveyId) {
            super(key, text, surveyId, REGISTRATION_DECLINED);
        }

        public static RegistrationDeclined parse(Map<String, String[]> options)
                throws MissingParameterException {
            return new RegistrationDeclined(
                    -1,
                    null,
                    getInt(options, "survey_id")
            );
        }

        @Override
        public UssdModel handle(String msisdn, MessageHandler handler) {
            return handler.handle(msisdn, this);
        }
    }
}
