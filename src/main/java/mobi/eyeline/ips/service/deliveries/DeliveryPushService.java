package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.service.BasePushService;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class DeliveryPushService extends BasePushService {

    private final String ussdPushUrl;
    private final String niDialogPushUrl;

    public DeliveryPushService(Config config) {
        super(config);

        ussdPushUrl = config.getDeliveryUssdPushUrl();
        niDialogPushUrl = config.getDeliveryNIPushUrl();
    }

    public void pushUssd(final int id,
                         final String msisdn,
                         final String message) throws IOException {
        try {
            final URIBuilder builder = new URIBuilder(ussdPushUrl) {{
                addParameter("subscriber", msisdn);
                addParameter("message", message);
                addParameter("resource_id", String.valueOf(id));
            }};

            doRequest(builder.build());

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public void niDialog(final int id,
                         final String msisdn,
                         final int surveyId) throws IOException {
        try {
            final URIBuilder builder = new URIBuilder(niDialogPushUrl) {{
                addParameter("subscriber", msisdn);
                addParameter("survey_id", String.valueOf(surveyId));
                addParameter("resource_id", String.valueOf(id));
            }};

            doRequest(builder.build());

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
