package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final DeliverySubscriberRepository deliverySubscriberRepository;

    public NotificationService(DeliverySubscriberRepository deliverySubscriberRepository) {
        this.deliverySubscriberRepository = deliverySubscriberRepository;
    }

    public boolean handleNotification(Notification notification) {
        final DeliverySubscriber.State state =
                (notification.getResult() == 2) ? DELIVERED : UNDELIVERED;

        final int updated =
                deliverySubscriberRepository.updateState(notification.getId(), state);

        if (updated == 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Notification: ID unknown: [" + notification.getId() + "]");
            }
            return false;

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Notification:" +
                        " state = [" + state + "]," +
                        " id = [" + notification.getId() + "]");
            }
            return true;
        }
    }

    public static class Notification {
        private final int id;
        private final int result;

        public Notification(int id, int result) {
            this.id = id;
            this.result = result;
        }

        public int getId() {
            return id;
        }

        public int getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "Notification{" +
                    "id=" + id +
                    ", result=" + result +
                    '}';
        }
    }
}
