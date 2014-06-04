package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Throwables;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.service.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.NEW;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

class PushThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PushThread.class);

    private final DelayQueue<DeliveryWrapper> deliveriesToSend;
    private final BlockingQueue<DeliveryWrapper> deliveriesToFetch;
    private final DeliveryPushService deliveryPushService;
    private final DeliverySubscriberRepository deliverySubscriberRepository;
    private final InvitationDeliveryRepository invitationDeliveryRepository;

    public PushThread(String name,
                      DelayQueue<DeliveryWrapper> deliveriesToSend,
                      BlockingQueue<DeliveryWrapper> deliveriesToFetch,
                      DeliveryPushService deliveryPushService,
                      DeliverySubscriberRepository deliverySubscriberRepository,
                      InvitationDeliveryRepository invitationDeliveryRepository) {

        super(name);

        this.deliveriesToSend = deliveriesToSend;
        this.deliveriesToFetch = deliveriesToFetch;
        this.deliveryPushService = deliveryPushService;
        this.deliverySubscriberRepository = deliverySubscriberRepository;
        this.invitationDeliveryRepository = invitationDeliveryRepository;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
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
            logger.info("Delivery is stopped, throwing out, id = [" + delivery + "]");

        } else {
            final DeliveryWrapper.Message message = delivery.poll();
            if (message == null) {
                logger.trace("Delivery: [" + delivery + "] is empty");

                if (delivery.isEmpty()) {
                    final InvitationDelivery dbModel = delivery.getModel();
                    dbModel.setState(InvitationDelivery.State.COMPLETED);
                    invitationDeliveryRepository.update(dbModel);
                    Services.instance().getDeliveryService().onDeliveryKick(delivery);

                } else {
                    delivery.setDelay(TimeUnit.SECONDS.toMillis(1));
                    doSchedule(delivery);
                }

            } else {
                handleNextMessage(delivery, message);
            }

            if (delivery.shouldBeFilled()) {
                deliveriesToFetch.put(delivery);
            }
        }
    }

    private void handleNextMessage(DeliveryWrapper delivery,
                                   DeliveryWrapper.Message message) throws InterruptedException {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Delivery: [" + delivery + "], message = [" + message + "]");
            }
            doProcess(delivery, message);
        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, InterruptedException.class);
            logger.error("Processing failed, " +
                    "delivery = [" + delivery + "], message = [" + message + "]");

        } finally {
            doSchedule(delivery);
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

    private void doSchedule(final DeliveryWrapper delivery) {
        deliveriesToSend.put(delivery);
    }
}
