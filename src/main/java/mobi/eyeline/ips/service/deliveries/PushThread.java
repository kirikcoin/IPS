package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Throwables;
import mobi.eyeline.ips.model.DeliveryAbonentStatus;
import mobi.eyeline.ips.model.InvitationDeliveryType;
import mobi.eyeline.ips.repository.DeliveryAbonentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;

class PushThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PushThread.class);

    private final BlockingQueue<Delivery> deliveriesToSend;
    private final BlockingQueue<Delivery> deliveriesToFetch;
    private final DeliveryPushService deliveryPushService;
    private final DeliveryAbonentRepository deliveryAbonentRepository;
    private final Timer timer;

    public PushThread(String name,
                      BlockingQueue<Delivery> deliveriesToSend,
                      BlockingQueue<Delivery> deliveriesToFetch,
                      DeliveryPushService deliveryPushService,
                      DeliveryAbonentRepository deliveryAbonentRepository,
                      Timer timer) {

        super(name);

        this.deliveriesToSend = deliveriesToSend;
        this.deliveriesToFetch = deliveriesToFetch;
        this.deliveryPushService = deliveryPushService;
        this.deliveryAbonentRepository = deliveryAbonentRepository;
        this.timer = timer;
    }

    @Override
    public void run() {
        try {
            while (!interrupted()) {
                final Delivery next = deliveriesToSend.take();
                final Delivery.Message message = next.getMessages().poll();
                if (message == null) {
                    logger.debug("Delivery: [" + next + "] is empty, ignoring");

                } else {
                    try {
                        doProcess(next, message);
                    } catch (Exception e) {
                        Throwables.propagateIfInstanceOf(e, InterruptedException.class);
                        logger.error("Processing failed, " +
                                "delivery = [" + next + "], message = [" + message + "]");
                    }
                }
            }

        } catch (InterruptedException e) {
            logger.info(e.getMessage(), e);
        }
    }

    private void doProcess(Delivery delivery, Delivery.Message message) {

        try {
            doPush(delivery, message);
            doMark(message, true);

        } catch (IOException e) {
            doMark(message, false);
        }

    }

    private void doPush(Delivery next, Delivery.Message message) throws IOException {
        final InvitationDeliveryType type = next.getInvitationDelivery().getType();
        switch (type) {
            case USSD_PUSH:
                deliveryPushService.pushUssd(
                        message.getId(),
                        message.getMsisdn(),
                        next.getInvitationDelivery().getText());
                break;
            case NI_DIALOG:
                deliveryPushService.niDialog(
                        message.getId(),
                        message.getMsisdn(),
                        next.getInvitationDelivery().getId());
                break;
            default:
                throw new AssertionError("Unknown delivery type: " + type);
        }
    }

    private void doMark(Delivery.Message message, boolean success) {
        deliveryAbonentRepository.updateState(message.getId(),
                success ? DeliveryAbonentStatus.SENT : DeliveryAbonentStatus.UNDELIVERED);
    }
}
