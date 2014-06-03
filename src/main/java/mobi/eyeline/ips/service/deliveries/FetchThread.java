package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliveryAbonent;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.model.InvitationDeliveryStatus;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

class FetchThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FetchThread.class);

    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final BlockingQueue<Delivery> toFetch;

    public FetchThread(InvitationDeliveryRepository invitationDeliveryRepository,
                       BlockingQueue<Delivery> queue) {

        this.invitationDeliveryRepository = invitationDeliveryRepository;
        this.toFetch = queue;
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
        final Delivery delivery = toFetch.take();

        try {
            doProcessDelivery(delivery);

        } catch (Exception e) {
            logger.error("Delivery processing failed: [" + delivery + "]", e);
            toFetch.put(delivery);
        }
    }

    private void doProcessDelivery(Delivery delivery) throws InterruptedException {
        final List<DeliveryAbonent> subscribers =
                invitationDeliveryRepository.fetchAndMark(
                        delivery.getModel(),
                        delivery.getFreeSize());

        if (subscribers.isEmpty()) {
            logger.info("All messages in delivery = [" + delivery + "] are processed");
            onCompleted(delivery);

        } else {
            for (DeliveryAbonent subscriber : subscribers) {
                final Delivery.Message message =
                        new Delivery.Message(subscriber.getId(), subscriber.getMsisdn());
                delivery.put(message);
            }

            onAfterFetch(delivery);
        }
    }

    private void onAfterFetch(Delivery delivery) {
        // Nothing here.
    }

    private void onCompleted(Delivery delivery) {
        final InvitationDelivery dbModel = delivery.getModel();
        dbModel.setStatus(InvitationDeliveryStatus.COMPLETED);
        invitationDeliveryRepository.update(dbModel);

        delivery.setStopped();
    }
}
