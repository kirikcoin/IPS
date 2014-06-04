package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Throwables;
import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.NEW;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

class PushThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PushThread.class);

    private final BlockingQueue<DeliveryWrapper> deliveriesToSend;
    private final BlockingQueue<DeliveryWrapper> deliveriesToFetch;
    private final DeliveryPushService deliveryPushService;
    private final DeliverySubscriberRepository deliverySubscriberRepository;
    private final Timer timer;

    public PushThread(BlockingQueue<DeliveryWrapper> deliveriesToSend,
                      BlockingQueue<DeliveryWrapper> deliveriesToFetch,
                      DeliveryPushService deliveryPushService,
                      DeliverySubscriberRepository deliverySubscriberRepository,
                      Timer timer) {

        this.deliveriesToSend = deliveriesToSend;
        this.deliveriesToFetch = deliveriesToFetch;
        this.deliveryPushService = deliveryPushService;
        this.deliverySubscriberRepository = deliverySubscriberRepository;
        this.timer = timer;
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                loop();
            }

        } catch (InterruptedException e) {
            logger.info("Push thread interrupted");
        }
    }

    private void loop() throws InterruptedException {
        final DeliveryWrapper delivery = deliveriesToSend.take();

        if (delivery.isStopped()) {
            // Just removed from the queue, so it's OK.
            logger.debug("Delivery is stopped, throwing out, id = [" + delivery + "]");

        } else {
            final DeliveryWrapper.Message message = delivery.poll();
            if (message == null) {
                logger.debug("Delivery: [" + delivery + "] is empty");
                doSchedule(delivery, TimeUnit.SECONDS.toMillis(1));

            } else {
                handleNextMessage(delivery, message);
            }
        }
    }

    private void handleNextMessage(DeliveryWrapper delivery,
                                   DeliveryWrapper.Message message) throws InterruptedException {
        try {
            doProcess(delivery, message);
        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, InterruptedException.class);
            logger.error("Processing failed, " +
                    "delivery = [" + delivery + "], message = [" + message + "]");

        } finally {
            doSchedule(delivery, delivery.getCurrentDelay());
        }

        if (delivery.shouldBeFilled()) {
            deliveriesToFetch.put(delivery);
        }
    }

    private void doProcess(DeliveryWrapper delivery,
                           DeliveryWrapper.Message message) {
        try {
            try {
                doPush(delivery, message);
            } finally {
                delivery.onMessageSent();
            }

            doMark(message, true);

        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            doMark(message, false);
        }
    }

    private void doPush(DeliveryWrapper next,
                        DeliveryWrapper.Message message) throws IOException {
        final InvitationDelivery.Type type = next.getModel().getType();
        switch (type) {
            case USSD_PUSH:
                deliveryPushService.pushUssd(
                        message.getId(),
                        message.getMsisdn(),
                        next.getModel().getText());
                break;
            case NI_DIALOG:
                deliveryPushService.niDialog(
                        message.getId(),
                        message.getMsisdn(),
                        next.getModel().getId());
                break;
            default:
                throw new AssertionError("Unknown delivery type: " + type);
        }
    }

    private void doMark(DeliveryWrapper.Message message, boolean success) {
        // Update state only for NEW messages to handle the following scenario:
        // 1. Message gets sent
        // 2. Notification arrives, state is updated to either DELIVERED or UNDELIVERED
        // 3. Finally comes to updating to SENT after step 1.
        deliverySubscriberRepository.updateState(message.getId(),
                success ? SENT : UNDELIVERED, NEW);
    }

    private void doSchedule(final DeliveryWrapper delivery,
                            long delayMillis) {

        // XXX: Consider changing Timer to DelayedQueue.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    deliveriesToSend.put(delivery);
                } catch (InterruptedException e) {
                    logger.info(e.getMessage(), e);
                }
            }
        }, delayMillis);
    }
}
