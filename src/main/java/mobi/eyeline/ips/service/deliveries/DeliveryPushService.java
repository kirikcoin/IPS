package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.service.BasePushService;

import java.io.IOException;

public class DeliveryPushService extends BasePushService {

    public DeliveryPushService(Config config) {
        super(config);
    }

    public void pushUssd(int id, String msisdn, String message) throws IOException {
        // TODO
    }

    public void niDialog(int id, String msisdn, int surveyId) throws IOException {
        // TODO
    }
}
