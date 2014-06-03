package mobi.eyeline.ips.service;

import com.eyeline.utils.DelayedQueue;
import com.eyeline.utils.XDelayedQueue;
import mobi.eyeline.ips.repository.DeliveryAbonentRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;

import java.util.Timer;

public class DeliveryService {
    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final DeliveryAbonentRepository deliveryAbonentRepository;

    private final Timer timer;

    public DeliveryService(InvitationDeliveryRepository invitationDeliveryRepository,
                           DeliveryAbonentRepository deliveryAbonentRepository) {
        this.invitationDeliveryRepository = invitationDeliveryRepository;
        this.deliveryAbonentRepository = deliveryAbonentRepository;

        timer = createTimer();
    }

    protected Timer createTimer() {
        return new Timer("delivery-service");
    }

    public static void main(String ... args) {
        DelayedQueue queue = new DelayedQueue();

        queue.offer(new Object(), System.currentTimeMillis() + 10000);
        queue.offer(new Object(), System.currentTimeMillis() + 10000);

        long start = System.currentTimeMillis();
        Object o = queue.take();

        XDelayedQueue q1 = new XDelayedQueue();
        q1.visit(new XDelayedQueue.Visitor() {
            @Override
            public boolean visit(Object o) {
                return false;
            }
        });

    }


}
