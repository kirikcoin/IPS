package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ExpirationThread extends LoopThread {

    private static final Logger logger = LoggerFactory.getLogger(ExpirationThread.class);

    private final long sentExpirationDelaySeconds;
    private final long fetchedExpirationDelaySeconds;
    private final DeliverySubscriberRepository deliverySubscriberRepository;

    public ExpirationThread(String name,
                            long sentExpirationDelaySeconds,
                            long fetchedExpirationDelaySeconds,
                            DeliverySubscriberRepository deliverySubscriberRepository) {

        super(name);

        this.sentExpirationDelaySeconds = sentExpirationDelaySeconds;
        this.fetchedExpirationDelaySeconds = fetchedExpirationDelaySeconds;
        this.deliverySubscriberRepository = deliverySubscriberRepository;
    }

    @Override
    protected void loop() throws InterruptedException {
        try {
            try {
                final int count = deliverySubscriberRepository.expireSent(sentExpirationDelaySeconds);
                logger.debug("Stale sent deliveries: entries expired: " + count);

            } catch (Exception e) {
                logger.error("Stale sent deliveries expiration failure", e);

            }

            try {
                final int count = deliverySubscriberRepository.expireFetched(fetchedExpirationDelaySeconds);
                logger.debug("Stale fetched deliveries: entries expired: " + count);

            } catch (Exception e) {
                logger.error("Stale fetched deliveries expiration failure", e);
            }

        } finally {
            sleep(TimeUnit.SECONDS.toMillis(
                    Math.min(sentExpirationDelaySeconds,fetchedExpirationDelaySeconds))
            );
        }
    }
}
