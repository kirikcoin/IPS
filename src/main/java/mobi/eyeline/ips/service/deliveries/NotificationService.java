package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Function;
import com.j256.simplejmx.common.JmxAttributeMethod;
import com.j256.simplejmx.common.JmxResource;
import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.util.TimeSource;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.transform;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.NEW;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

@JmxResource(domainName = "mobi.eyeline.ips")
public class NotificationService {
  private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

  private final TimeSource timeSource;

  private final DelayQueue<DelayedNotification> toUpdate = new DelayQueue<>();

  private final DeliveryService deliveryService;

  private final NotificationServiceThread thread;

  private final DeliverySubscriberRepository deliverySubscriberRepository;

  public NotificationService(TimeSource timeSource, DeliverySubscriberRepository deliverySubscriberRepository, DeliveryService deliveryService) {
    this.deliverySubscriberRepository = deliverySubscriberRepository;
    this.timeSource = timeSource;
    this.deliveryService = deliveryService;

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

  public boolean handleNotification(Notification notification) {

    DeliverySubscriber deliverySubscriber = deliverySubscriberRepository.get(notification.getId());
    InvitationDelivery delivery = deliverySubscriber.getInvitationDelivery();
    DeliveryWrapper deliveryWrapper = deliveryService.getDeliveryWrapper(delivery.getId());

    if (logger.isTraceEnabled()) {
      logger.debug("Delivery-" + delivery.getId() + ": new notification for: " + deliverySubscriber.getId() + ", state: " + notification.asState() + ", delivered: " + notification.isDelivered());
    }

    if (delivery.getRetriesEnabled() && notification.asState() == UNDELIVERED) {
      int attemptsCount = deliveryWrapper.getRespondentCommittedAttemptsNumber().get(deliverySubscriber.getMsisdn());

      if (attemptsCount < delivery.getRetriesMax() + 1) {
        notification.setState(NEW);
        toUpdate.put(DelayedNotification.forDelay(
                timeSource,
                notification,
                TimeUnit.MINUTES.toMillis(delivery.getRetriesIntervalMinutes()))

        );
        if (logger.isTraceEnabled()) {
          logger.debug("Delivery-" + delivery.getId() + ": message will be retried: " + deliverySubscriber.getId() + ", attempts: " + attemptsCount);
        }
        return true;
      }
    }

    if (logger.isTraceEnabled()) {
      logger.debug("Delivery-" + delivery.getId() + ": message will be finalized: " + deliverySubscriber.getId() + ", state: " + notification.asState());
    }

    deliveryWrapper.getRespondentCommittedAttemptsNumber().remove(deliverySubscriber.getMsisdn());
    toUpdate.put(DelayedNotification.forImmediate(timeSource, notification));
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
    private final boolean isDelivered;
    private DeliverySubscriber.State state = null;
    private static final int SUCCESS_RESULT = 2;

    public Notification(int id, int result, boolean isDelivered) {
      this.id = id;
      this.result = result;
      this.isDelivered = isDelivered;
    }

    public int getId() {
      return id;
    }

    public int getResult() {
      return result;
    }

    public boolean isDelivered() {
      return isDelivered;
    }

    public DeliverySubscriber.State getState() {
      return state;
    }

    public void setState(DeliverySubscriber.State state) {
      this.state = state;
    }

    DeliverySubscriber.State asState() {
      if (state != null) return state;
      return (getResult() == SUCCESS_RESULT || isDelivered) ? DELIVERED : UNDELIVERED;
    }

    @Override
    public String toString() {
      return "Notification{" +
          "id=" + id +
          ", result=" + result +
          '}';
    }
  }

  public static class DelayedNotification implements Delayed {

    private final TimeSource timeSource;

    private final Notification notification;

    private final long from;
    private final long delayMillis;

    public DelayedNotification(TimeSource timeSource,
                               Notification notification,
                               long delayMillis) {
      this.timeSource = timeSource;
      this.notification = notification;
      this.delayMillis = delayMillis;
      this.from = timeSource.currentTimeMillis();
    }

    @Override
    public long getDelay(@SuppressWarnings("NullableProblems") TimeUnit unit) {
      return unit.convert(
          Math.max(from + delayMillis - timeSource.currentTimeMillis(), 0),
          TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Delayed o) {
      return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }

    public Notification getNotification() {
      return notification;
    }

    public static DelayedNotification forImmediate(TimeSource timeSource, Notification notification) {
      return new DelayedNotification(timeSource, notification, 0);
    }

    public static DelayedNotification forDelay(TimeSource timeSource, Notification notification,
                                               long millis) {
      return new DelayedNotification(timeSource, notification, millis);
    }
  }

  private static class NotificationServiceThread extends LoopThread {

    private static final Function<DelayedNotification, Pair<Integer, DeliverySubscriber.State>> asPair =
        new Function<DelayedNotification, Pair<Integer, DeliverySubscriber.State>>() {
          @Override
          public Pair<Integer, DeliverySubscriber.State> apply(DelayedNotification input) {
            return Pair.of(input.getNotification().getId(), input.getNotification().asState());
          }
        };

    private final int batchSize;
    private final DelayQueue<DelayedNotification> toUpdate;
    private final DeliverySubscriberRepository deliverySubscriberRepository;

    public NotificationServiceThread(String name,
                                     int batchSize,
                                     DelayQueue<DelayedNotification> toUpdate,
                                     DeliverySubscriberRepository deliverySubscriberRepository) {
      super(name);
      this.batchSize = batchSize;
      this.toUpdate = toUpdate;
      this.deliverySubscriberRepository = deliverySubscriberRepository;
    }

    @Override
    protected void loop() throws InterruptedException {
      final List<DelayedNotification> chunk = fetchChunk();
      updateChunk(chunk);
    }

    private List<DelayedNotification> fetchChunk() throws InterruptedException {
      final List<DelayedNotification> chunk = new ArrayList<>(batchSize);
      chunk.add(toUpdate.take());
      toUpdate.drainTo(chunk, batchSize - 1);

      return chunk;
    }

    private void updateChunk(List<DelayedNotification> chunk) {
      int retries = 3;

      do {
        try {
          doUpdateChunk(chunk);
          return;
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

    private void doUpdateChunk(List<DelayedNotification> chunk) {
      if (!chunk.isEmpty()) {
        if (logger.isDebugEnabled()) {
          logger.debug("Update states from notifications for " + chunk.size() + " entries");
        }
        deliverySubscriberRepository.updateState(transform(chunk, asPair));

      } else {
        logger.debug("Nothing to update");
      }
    }

    public void processRemaining() {
      final List<DelayedNotification> chunk = new ArrayList<>(toUpdate.size());
      toUpdate.drainTo(chunk);
      logger.debug("Updating " + chunk.size() + " entries on shutdown");

      updateChunk(chunk);
    }
  }
}
