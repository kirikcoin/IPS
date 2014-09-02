package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.service.BasePushService;
import mobi.eyeline.ips.service.EsdpServiceSupport;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class DeliveryPushService extends BasePushService {

    private final EsdpServiceSupport esdpServiceSupport;

    public DeliveryPushService(Config config,
                               EsdpServiceSupport esdpServiceSupport) {
        super(config);

        this.esdpServiceSupport = esdpServiceSupport;
    }

    public void pushUssd(final int id,
                         Survey survey,
                         final String msisdn,
                         final String message,
                         RequestExecutionListener listener) throws IOException {
        try {
            final URIBuilder builder = new URIBuilder(esdpServiceSupport.getServiceUrl(survey)) {{
                addParameter("scenario", "push-inform");
                addParameter("protocol", "ussd");
                addParameter("subscriber", msisdn);
                addParameter("message", message);
                addParameter("resource_id", String.valueOf(id));
            }};

            doRequest(builder.build(), listener);

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public void pushSms(final int id,
                         Survey survey,
                         final String msisdn,
                         final String message,
                         RequestExecutionListener listener) throws IOException {
        try {
            final URIBuilder builder = new URIBuilder(esdpServiceSupport.getServiceUrl(survey)) {{
                addParameter("scenario", "push-inform");
                addParameter("protocol", "sms");
                addParameter("subscriber", msisdn);
                addParameter("message", message);
                addParameter("resource_id", String.valueOf(id));
            }};

            doRequest(builder.build(), listener);

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public void niDialog(final int id,
                         final Survey survey,
                         final String msisdn,
                         RequestExecutionListener listener) throws IOException {
        try {
            final URIBuilder builder = new URIBuilder(esdpServiceSupport.getServiceUrl(survey)) {{
                addParameter("scenario", "default-inform");
                addParameter("subscriber", msisdn);
                addParameter("survey_id", String.valueOf(survey.getId()));
                addParameter("resource_id", String.valueOf(id));
            }};

            doRequest(builder.build(), listener);

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
