package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

public class NotificationService {
    private final DeliverySubscriberRepository deliverySubscriberRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    public NotificationService(DeliverySubscriberRepository deliverySubscriberRepository) {
        this.deliverySubscriberRepository = deliverySubscriberRepository;
    }

    public void handleNotification(int id, int status) {
        DeliverySubscriber subscriber = deliverySubscriberRepository.load(id);
        subscriber.setState(status == 2 ?  DELIVERED : UNDELIVERED);
        deliverySubscriberRepository.update(subscriber);
        logger.debug("Subscriber, id = " + id + " was updated, status = ",subscriber.getState());
    }
}
