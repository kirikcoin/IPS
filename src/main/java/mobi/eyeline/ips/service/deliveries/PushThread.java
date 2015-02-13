package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Throwables;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.service.BasePushService;
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

        if (logger.isInfoEnabled()) {
            logger.info("Delivery-" + delivery.getModel().getId() + ": start to process.");
        }

        if (delivery.isStopped()) {
            if (logger.isInfoEnabled()) {
                logger.info("Delivery-" + delivery.getModel().getId() + ": delivery stopped. Ignore it.");
            }
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

        boolean hasRetries = delivery.getModel().getRetriesEnabled() && delivery.hasMessagesToRetry();

        if (logger.isDebugEnabled()) {
            logger.debug("Delivery-" + delivery.getModel().getId() + ": handle empty, isEmpty = " + delivery.isEmpty() + ", hasRetries = " + hasRetries);
        }

        if (delivery.isEmpty() && !hasRetries) {

            if (logger.isInfoEnabled()) {
                logger.info("Delivery-" + delivery.getModel().getId() + ": will be marked as complete");
            }
            // Fetch process marked this one as having no more entries in DB,
            // so just update the state accordingly.
            final InvitationDelivery dbModel =
                    invitationDeliveryRepository.load(delivery.getModel().getId());
            dbModel.setState(InvitationDelivery.State.COMPLETED);
            invitationDeliveryRepository.update(dbModel);

            // XXX:DEBUG
            Services.instance().getDeliveryService().onDeliveryKick(delivery);

        } else {
//            logger.info("Delivery-" + delivery.getModel().getId() + ": will be returned toSend");
            toSend.put(DelayedDeliveryWrapper.forDelay(timeSource, delivery, WAIT_TO_FILL));
        }
    }

    private void handleMessage(DeliveryWrapper delivery,
                               DeliveryWrapper.Message message) throws InterruptedException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Delivery-" + delivery.getModel().getId() + ": new message ready to push: " + message.getId() + ", state: " + message.getState());
            }
            doHandleMessage(delivery, message);

        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, InterruptedException.class);
            logger.error("Delivery-" + delivery.getModel().getId() + ": can't send message: " + message.getId(), e);
        }
    }

    private void doHandleMessage(final DeliveryWrapper delivery,
                                 final DeliveryWrapper.Message message) throws InterruptedException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Delivery-" + delivery.getModel().getId() + ": try to push message: " + message.getId());
            }
            doPush(delivery, message, new BasePushService.RequestExecutionListener() {
                @Override
                public void onAfterSendRequest() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Delivery-" + delivery.getModel().getId() + ": message has been sent: " + message.getId());
                    }
                    toSend.put(DelayedDeliveryWrapper.forSent(timeSource, delivery));
                }
            });

            onSentAttempt(delivery, message, true);

        } catch (IOException e) {
            logger.error("Delivery-" + delivery.getModel().getId() + ": can't sent message: " + message.getId(), e);
            onSentAttempt(delivery, message, false);
        }
    }

    private void doPush(DeliveryWrapper delivery,
                        DeliveryWrapper.Message message,
                        BasePushService.RequestExecutionListener listener) throws IOException {
        final int msgId = message.getId();
        final Survey survey = delivery.getModel().getSurvey();
        final String msisdn = message.getMsisdn();

        final InvitationDelivery.Type type = delivery.getModel().getType();
        switch (type) {
            case USSD_PUSH:

                incAttempts(delivery, msisdn, msgId);
                deliveryPushService.pushUssd(
                        msgId, survey, msisdn, delivery.getModel().getText(), listener);
                break;
            case SMS:

                incAttempts(delivery, msisdn, msgId);
                deliveryPushService.pushSms(
                        msgId, survey, msisdn, delivery.getModel().getText(), listener);
                break;
            case NI_DIALOG:
                incAttempts(delivery, msisdn, msgId);
                deliveryPushService.niDialog(msgId, survey, msisdn, listener);
                break;
            default:
                throw new AssertionError("Unknown delivery type: " + type);
        }
    }

    private void incAttempts(DeliveryWrapper deliveryWrapper, String msisdn, int id) {
        Integer oldValue = deliveryWrapper.getRespondentAttemptsNumber().get(msisdn);
        if (oldValue == null) {
            deliveryWrapper.getRespondentAttemptsNumber().put(msisdn, 1);
        } else {
            deliveryWrapper.getRespondentAttemptsNumber().put(msisdn, oldValue + 1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Delivery-" + deliveryWrapper.getModel().getId() + ": inc attempts for : " + id + ". New value = " + deliveryWrapper.getRespondentAttemptsNumber().get(msisdn));
        }
    }

    private void onSentAttempt(DeliveryWrapper deliveryWrapper,
                               DeliveryWrapper.Message message,
                               boolean success) throws InterruptedException {

        // FIXME
        if (message.incrementAndGet() >= retryAttempts || success) {
            if (logger.isDebugEnabled()) {
                logger.debug("Delivery-" + deliveryWrapper.getModel().getId() + ": message sent: " + message.getId() + ", state: " + success);
            }
            toMark.put(message.setSent(success));

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Delivery-" + deliveryWrapper.getModel().getId() + ": message is not sent: " + message.getId() + ". Reschedule.");
            }
            deliveryWrapper.put(message);
        }

    }
}
