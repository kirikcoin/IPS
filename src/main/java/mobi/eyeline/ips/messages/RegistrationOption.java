package mobi.eyeline.ips.messages;

import mobi.eyeline.ips.model.Survey;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static mobi.eyeline.ips.messages.ParseUtils.getInt;
import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.REGISTRATION_ACCEPTED;
import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.REGISTRATION_DECLINED;

public class RegistrationOption extends UssdOption {

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
                new RegistrationOption(1, textYes, survey.getId(), REGISTRATION_ACCEPTED),
                new RegistrationOption(2, textNo, survey.getId(), REGISTRATION_DECLINED)
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
    }
}
