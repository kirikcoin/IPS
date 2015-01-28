package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ExpirationThread extends LoopThread {

    private static final Logger logger = LoggerFactory.getLogger(ExpirationThread.class);

    private final long expirationDelaySeconds;
    private final DeliverySubscriberRepository deliverySubscriberRepository;

    public ExpirationThread(String name,
                            long expirationDelaySeconds,
                            DeliverySubscriberRepository deliverySubscriberRepository) {

        super(name);

        this.expirationDelaySeconds = expirationDelaySeconds;
        this.deliverySubscriberRepository = deliverySubscriberRepository;
    }

    @Override
    protected void loop() throws InterruptedException {
        try {
            final int count = deliverySubscriberRepository.expireSent(expirationDelaySeconds);
            logger.debug("Stale deliveries: entries expired: " + count);

        } catch (Exception e) {
            logger.error("Stale deliveries expiration failure", e);

        } finally {
            sleep(TimeUnit.SECONDS.toMillis(expirationDelaySeconds));
        }
    }
}
