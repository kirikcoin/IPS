package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static mobi.eyeline.ips.messages.UssdOption.PARAM_SKIP_VALIDATION;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SURVEY_ID;

public class PushService extends BasePushService {

    private static final Logger logger = LoggerFactory.getLogger(PushService.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PushService(Config config) {
        super(config);
    }

    public void scheduleSend(final Survey survey,
                             final String msisdn) {

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    send(survey, msisdn);
                } catch (URISyntaxException | IOException e) {
                    logger.error("Error sending PUSH-request, " +
                            "survey = [" + survey + "], msisdn = [" + msisdn + "]", e);
                }
            }
        });

        logger.debug("Scheduled PUSH request:" +
                " survey = [" + survey + "], msisdn = [" + msisdn + "]");
    }

    public void scheduleSendSms(final Survey survey,
                                final String from,
                                final String text,
                                final String msisdn) {

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    sendSms(survey, from, text, msisdn);
                } catch (URISyntaxException | IOException e) {
                    logger.error("Error sending SMS PUSH-request, " +
                            "survey = [" + survey + "], msisdn = [" + msisdn + "]", e);
                }
            }
        });

        logger.debug("Scheduled SMS PUSH request:" +
                " survey = [" + survey + "], msisdn = [" + msisdn + "]");
    }

    protected void send(Survey survey,
                        String msisdn) throws URISyntaxException, IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Sending PUSH request:" +
                    " survey = [" + survey + "], msisdn = [" + msisdn + "]");
        }

        final URI uri = buildUri(msisdn, survey.getId());
        doRequest(uri);
    }

    protected void sendSms(Survey survey,
                           String from,
                           String text,
                           String msisdn)
            throws URISyntaxException, IOException {

        assert survey.getDetails().isEndSmsEnabled();

        if (logger.isDebugEnabled()) {
            logger.debug("Sending SMS PUSH request for:" +
                    " survey = [" + survey + "]," +
                    " msisdn = [" + msisdn + "]," +
                    " message = [" + text + "]," +
                    " oa = [" + from + "]");
        }

        final URI uri = buildUri(msisdn, text, from);
        doRequest(uri);
    }

    private URI buildUri(String msisdn, int surveyId)
            throws URISyntaxException {

        final String pushUrl = config.getSadsPushUrl();

        final URIBuilder builder = new URIBuilder(pushUrl);
        builder.addParameter(PARAM_SURVEY_ID, String.valueOf(surveyId));
        builder.addParameter("subscriber", msisdn);
        builder.addParameter(PARAM_SKIP_VALIDATION, "true");

        return builder.build();
    }

    private URI buildUri(String msisdn,
                         String message,
                         String originatingAddress) throws URISyntaxException {

        final String pushUrl = config.getSadsSmsPushUrl();

        final URIBuilder builder = new URIBuilder(pushUrl);
        builder.addParameter("subscriber", msisdn);
        builder.addParameter("message", message);
        builder.addParameter("address", originatingAddress);

        return builder.build();
    }
}
