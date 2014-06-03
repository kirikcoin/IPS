package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Throwables;
import mobi.eyeline.ips.model.DeliveryAbonentStatus;
import mobi.eyeline.ips.model.InvitationDeliveryType;
import mobi.eyeline.ips.repository.DeliveryAbonentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class PushThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PushThread.class);

    private final BlockingQueue<Delivery> deliveriesToSend;
    private final BlockingQueue<Delivery> deliveriesToFetch;
    private final DeliveryPushService deliveryPushService;
    private final DeliveryAbonentRepository deliveryAbonentRepository;
    private final Timer timer;

    public PushThread(BlockingQueue<Delivery> deliveriesToSend,
                      BlockingQueue<Delivery> deliveriesToFetch,
                      DeliveryPushService deliveryPushService,
                      DeliveryAbonentRepository deliveryAbonentRepository,
                      Timer timer) {

        this.deliveriesToSend = deliveriesToSend;
        this.deliveriesToFetch = deliveriesToFetch;
        this.deliveryPushService = deliveryPushService;
        this.deliveryAbonentRepository = deliveryAbonentRepository;
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
        final Delivery delivery = deliveriesToSend.take();

        if (delivery.isStopped()) {
            // Just removed from the queue, so it's OK.
            logger.debug("Delivery is stopped, throwing out, id = [" + delivery + "]");

        } else {
            final Delivery.Message message = delivery.poll();
            if (message == null) {
                logger.debug("Delivery: [" + delivery + "] is empty");
                doSchedule(delivery, TimeUnit.SECONDS.toMillis(1));

            } else {
                handleNextMessage(delivery, message);
            }
        }
    }

    private void handleNextMessage(Delivery delivery,
                                   Delivery.Message message) throws InterruptedException {
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

    private void doProcess(Delivery delivery,
                           Delivery.Message message) {
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

    private void doPush(Delivery next,
                        Delivery.Message message) throws IOException {
        final InvitationDeliveryType type = next.getModel().getType();
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

    private void doMark(Delivery.Message message, boolean success) {
        deliveryAbonentRepository.updateState(message.getId(),
                success ? DeliveryAbonentStatus.SENT : DeliveryAbonentStatus.UNDELIVERED);
    }

    private void doSchedule(final Delivery delivery,
                            long delayMillis) {

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
