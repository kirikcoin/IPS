package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Throwables;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.util.TimeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.service.deliveries.DeliveryWrapper.DelayedDeliveryWrapper;

class PushThread extends LoopThread {

    private static final Logger logger = LoggerFactory.getLogger(PushThread.class);

    private static final long WAIT_TO_FILL = TimeUnit.SECONDS.toMillis(2);

    private final int retryAttempts;
    private final TimeSource timeSource;
    private final DelayQueue<DelayedDeliveryWrapper> toSend;
    private final BlockingQueue<DeliveryWrapper> toFetch;
    private final BlockingQueue<DeliveryWrapper.Message> toMark;

    private final DeliveryPushService deliveryPushService;
    private final InvitationDeliveryRepository invitationDeliveryRepository;

    public PushThread(String name,

                      int retryAttempts,

                      TimeSource timeSource,

                      DelayQueue<DelayedDeliveryWrapper> toSend,
                      BlockingQueue<DeliveryWrapper> toFetch,
                      BlockingQueue<DeliveryWrapper.Message> toMark,

                      DeliveryPushService deliveryPushService,
                      InvitationDeliveryRepository invitationDeliveryRepository) {

        super(name);

        this.retryAttempts = retryAttempts;
        this.timeSource = timeSource;

        this.toSend = toSend;
        this.toFetch = toFetch;
        this.toMark = toMark;

        this.deliveryPushService = deliveryPushService;
        this.invitationDeliveryRepository = invitationDeliveryRepository;
    }

    @Override
    protected void loop() throws InterruptedException {
        final DeliveryWrapper delivery = toSend.take().getDeliveryWrapper();

        if (delivery.isStopped()) {
            // Just removed from the queue, so it's OK.
            logger.info("Delivery is stopped, throwing out [" + delivery + "]");

        } else {
            final DeliveryWrapper.Message message = delivery.poll();
            if (message == null) {
                handleEmpty(delivery);

            } else {
                handleMessage(delivery, message);
            }

            if (delivery.shouldBeFilled()) {
                toFetch.put(delivery);
            }
        }
    }

    private void handleEmpty(DeliveryWrapper delivery) {
        if (logger.isTraceEnabled()) {
            logger.trace("Delivery: [" + delivery + "] is empty");
        }

        if (delivery.isEmpty()) {
            // Fetch process marked this one as having no more entries in DB,
            // so just update the state accordingly.
            final InvitationDelivery dbModel =
                    invitationDeliveryRepository.load(delivery.getModel().getId());
            dbModel.setState(InvitationDelivery.State.COMPLETED);
            invitationDeliveryRepository.update(dbModel);

            // XXX:DEBUG
            Services.instance().getDeliveryService().onDeliveryKick(delivery);

        } else {
            toSend.put(DelayedDeliveryWrapper.forDelay(timeSource, delivery, WAIT_TO_FILL));
        }
    }

    private void handleMessage(DeliveryWrapper delivery,
                               DeliveryWrapper.Message message) throws InterruptedException {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Delivery: [" + delivery + "], message = [" + message + "]");
            }
            doHandleMessage(delivery, message);

        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, InterruptedException.class);
            logger.error("Processing failed, " +
                    "delivery = [" + delivery + "], message = [" + message + "]");
        }
    }

    private void doHandleMessage(DeliveryWrapper delivery,
                                 DeliveryWrapper.Message message) throws InterruptedException {
        try {
            try {
                doPush(delivery, message);
            } finally {
                toSend.put(DelayedDeliveryWrapper.forSent(timeSource, delivery));
            }

            onSentAttempt(delivery, message, true);

        } catch (IOException e) {
            logger.warn("Message sending failed", e);
            onSentAttempt(delivery, message, false);
        }
    }

    private void doPush(DeliveryWrapper next,
                        DeliveryWrapper.Message message) throws IOException {
        final InvitationDelivery.Type type = next.getModel().getType();
        switch (type) {
            case USSD_PUSH:
                deliveryPushService.pushUssd(
                        message.getId(),
                        next.getModel().getSurvey(),
                        message.getMsisdn(),
                        next.getModel().getText());
                break;
            case NI_DIALOG:
                deliveryPushService.niDialog(
                        message.getId(),
                        next.getModel().getSurvey(),
                        message.getMsisdn(),
                        next.getModel().getSurvey().getId());
                break;
            default:
                throw new AssertionError("Unknown delivery type: " + type);
        }
    }

    private void onSentAttempt(DeliveryWrapper deliveryWrapper,
                               DeliveryWrapper.Message message,
                               boolean success) throws InterruptedException {

        if (message.incrementAndGet() >= retryAttempts | success) {
            if (logger.isTraceEnabled()) {
                logger.trace("Scheduling to mark [" + message + "]");
            }
            toMark.put(message.setSent(success));

        } else {
            if (logger.isTraceEnabled()) {
                logger.trace("Rescheduling on failure [" + message + "]");
            }
            deliveryWrapper.put(message);
        }
    }
}
