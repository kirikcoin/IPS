package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

class FetchThread extends LoopThread {

    private static final Logger logger = LoggerFactory.getLogger(FetchThread.class);

    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final BlockingQueue<DeliveryWrapper> toFetch;

    public FetchThread(String name,
                       InvitationDeliveryRepository invitationDeliveryRepository,
                       BlockingQueue<DeliveryWrapper> queue) {
        super(name);

        this.invitationDeliveryRepository = invitationDeliveryRepository;
        this.toFetch = queue;
    }

    @Override
    protected void loop() throws InterruptedException {
        final DeliveryWrapper delivery = toFetch.take();

        try {
            doProcess(delivery);

        } catch (Exception e) {
            logger.error("Delivery processing failed: [" + delivery + "]", e);
            toFetch.put(delivery);
        }
    }

    private void doProcess(DeliveryWrapper delivery) throws InterruptedException {
        if (!delivery.shouldBeFilled()) {
            logger.debug("Skipping [" + delivery + "] as already full");
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Processing [" + delivery + "], size = [" + delivery.size() + "]");
        }

        final List<DeliverySubscriber> subscribers =
                invitationDeliveryRepository.fetchNext(
                        delivery.getModel(),
                        delivery.getMessagesQueueSize());

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
        delivery.setEmpty(true);
    }
}
