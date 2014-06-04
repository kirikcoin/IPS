package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

public class NotificationService {
    private final DeliverySubscriberRepository deliverySubscriberRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);


    public NotificationService(DeliverySubscriberRepository deliverySubscriberRepository) {
        this.deliverySubscriberRepository = deliverySubscriberRepository;
    }

    public int handleNotification(int id, int status) {
        DeliverySubscriber subscriber = deliverySubscriberRepository.get(id);

        if (subscriber == null) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
        subscriber.setState(status == 2 ? DELIVERED : UNDELIVERED);

        try {
            deliverySubscriberRepository.update(subscriber);
            logger.debug("Subscriber, id = " + id + " was updated, status = ", subscriber.getState());
        } catch (Exception ignored) {
            logger.debug("Error in subscriber updating");
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        return HttpServletResponse.SC_OK;

    }
}
