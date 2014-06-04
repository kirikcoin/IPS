package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.service.BasePushService;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DeliveryPushService extends BasePushService {

    public DeliveryPushService(Config config) {
        super(config);
    }

    public void pushUssd(int id, String msisdn, String message) throws IOException {
        try {
            final String pushUrl = config.getDeliveryUssdPushUrl();
            final URIBuilder builder;
            builder = new URIBuilder(pushUrl);

            builder.addParameter("subscriber", msisdn);
            builder.addParameter("message", message);
            builder.addParameter("resource_id", String.valueOf(id));

            doRequest(new URI(pushUrl));

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public void niDialog(int id, String msisdn, int surveyId) throws IOException {
        try {
            final String pushUrl = config.getDeliveryNIPushUrl();
            final URIBuilder builder = new URIBuilder(pushUrl);

            builder.addParameter("subscriber", msisdn);
            builder.addParameter("survey_id", String.valueOf(surveyId));
            builder.addParameter("resource_id", String.valueOf(id));

            doRequest(new URI(pushUrl));

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
