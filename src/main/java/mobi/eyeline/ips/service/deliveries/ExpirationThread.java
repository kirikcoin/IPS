package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.FETCHED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.NEW;

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
                final int count = deliverySubscriberRepository.expire(SENT, UNDELIVERED, sentExpirationDelaySeconds);
                logger.debug("Stale sent deliveries: entries expired: " + count);

            } catch (Exception e) {
                logger.error("Stale sent deliveries expiration failure", e);

            }

            try {
                final int count = deliverySubscriberRepository.expire(FETCHED, NEW, fetchedExpirationDelaySeconds);
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
