package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Function;
import com.j256.simplejmx.common.JmxAttributeMethod;
import com.j256.simplejmx.common.JmxResource;
import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.collect.Lists.transform;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

@JmxResource(domainName = "mobi.eyeline.ips")
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final BlockingQueue<Notification> toUpdate = new LinkedBlockingQueue<>();

    private NotificationServiceThread thread;

    public NotificationService(DeliverySubscriberRepository deliverySubscriberRepository) {
        thread = new NotificationServiceThread(
                "push-notify",
                10,
                toUpdate,
                deliverySubscriberRepository);
    }

    @JmxAttributeMethod
    public int getToUpdateSize() {
        return toUpdate.size();
    }

    public void start() {
        thread.start();
    }

    public boolean handleNotification(Notification notification)
            throws InterruptedException {
        toUpdate.put(notification);
        return true;
    }

    public void stop() throws InterruptedException {
        thread.interrupt();
        thread.processRemaining();
        thread.join();
    }

    public static class Notification {
        private final int id;
        private final int result;

        public Notification(int id, int result) {
            this.id = id;
            this.result = result;
        }

        public int getId() {
            return id;
        }

        public int getResult() {
            return result;
        }

        DeliverySubscriber.State asState() {
            return (getResult() == 2) ? DELIVERED : UNDELIVERED;
        }

        @Override
        public String toString() {
            return "Notification{" +
                    "id=" + id +
                    ", result=" + result +
                    '}';
        }
    }

    private static class NotificationServiceThread extends LoopThread {

        private static final Function<Notification, Pair<Integer, DeliverySubscriber.State>> asPair =
                new Function<Notification, Pair<Integer, DeliverySubscriber.State>>() {
                    @Override
                    public Pair<Integer, DeliverySubscriber.State> apply(Notification input) {
                        return Pair.of(input.getId(), input.asState());
                    }
                };

        private final int batchSize;
        private final BlockingQueue<Notification> toUpdate;
        private final DeliverySubscriberRepository deliverySubscriberRepository;

        public NotificationServiceThread(String name,
                                         int batchSize,
                                         BlockingQueue<Notification> toUpdate,
                                         DeliverySubscriberRepository deliverySubscriberRepository) {
            super(name);
            this.batchSize = batchSize;
            this.toUpdate = toUpdate;
            this.deliverySubscriberRepository = deliverySubscriberRepository;
        }

        @Override
        protected void loop() throws InterruptedException {
            final List<Notification> chunk = fetchChunk();
            updateChunk(chunk);
        }

        private List<Notification> fetchChunk() throws InterruptedException {
            final List<Notification> chunk = new ArrayList<>(batchSize);
            chunk.add(toUpdate.take());
            toUpdate.drainTo(chunk, batchSize - 1);

            return chunk;
        }

        private void updateChunk(List<Notification> chunk) {
            int retries = 3;

            do {
                try {
                    doUpdateChunk(chunk);

                } catch (Exception e) {
                    if (retries == 0) {
                        logger.warn("State update error", e);
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("State update failed, retrying (" + e.getMessage() + " )");
                        }
                    }
                }
            } while (retries-- > 0);
        }

        private void doUpdateChunk(List<Notification> chunk) {
            if (!chunk.isEmpty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Notifications for " + chunk.size() + " entries");
                }
                deliverySubscriberRepository.updateState(transform(chunk, asPair));

            } else {
                logger.debug("Nothing to update");
            }
        }

        public void processRemaining() {
            final List<Notification> chunk = new ArrayList<>(toUpdate.size());
            toUpdate.drainTo(chunk);
            logger.debug("Updating " + chunk.size() + " entries on shutdown");

            updateChunk(chunk);
        }
    }
}
