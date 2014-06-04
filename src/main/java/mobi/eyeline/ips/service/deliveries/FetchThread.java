package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

class FetchThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FetchThread.class);

    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final BlockingQueue<DeliveryWrapper> toFetch;

    public FetchThread(InvitationDeliveryRepository invitationDeliveryRepository,
                       BlockingQueue<DeliveryWrapper> queue) {

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
        final DeliveryWrapper delivery = toFetch.take();

        try {
            doProcessDelivery(delivery);

        } catch (Exception e) {
            logger.error("Delivery processing failed: [" + delivery + "]", e);
            toFetch.put(delivery);
        }
    }

    private void doProcessDelivery(DeliveryWrapper delivery) throws InterruptedException {
        final List<DeliverySubscriber> subscribers =
                invitationDeliveryRepository.fetchNext(
                        delivery.getModel(),
                        delivery.getFreeSize());

        if (subscribers.isEmpty()) {
            logger.info("All messages in delivery = [" + delivery + "] are processed");
            onCompleted(delivery);

        } else {
            for (DeliverySubscriber subscriber : subscribers) {
                final DeliveryWrapper.Message message =
                        new DeliveryWrapper.Message(subscriber.getId(), subscriber.getMsisdn());
                delivery.put(message);
            }

            onAfterFetch(delivery);
        }
    }

    private void onAfterFetch(DeliveryWrapper delivery) {
        // Nothing here.
    }

    private void onCompleted(DeliveryWrapper delivery) {
        final InvitationDelivery dbModel = delivery.getModel();
        dbModel.setState(InvitationDelivery.State.COMPLETED);
        invitationDeliveryRepository.update(dbModel);

        delivery.setStopped();
    }
}
