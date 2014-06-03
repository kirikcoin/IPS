package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.repository.DeliveryAbonentRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final DeliveryAbonentRepository deliveryAbonentRepository;
    private final DeliveryPushService deliveryPushService;
    private final int messagesChunkSize;

    private final PushThread[] pushThreads;

    private final LinkedBlockingQueue<Delivery> deliveriesToSend = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Delivery> deliveriesToFetch = new LinkedBlockingQueue<>();

    private final Timer timer = new Timer("push-sched");

    public DeliveryService(InvitationDeliveryRepository invitationDeliveryRepository,
                           DeliveryAbonentRepository deliveryAbonentRepository,
                           DeliveryPushService deliveryPushService,
                           int messagesChunkSize,
                           int sendingThreads) {

        this.invitationDeliveryRepository = invitationDeliveryRepository;
        this.deliveryAbonentRepository = deliveryAbonentRepository;
        this.deliveryPushService = deliveryPushService;
        this.messagesChunkSize = messagesChunkSize;

        pushThreads = new PushThread[sendingThreads];
        for (int i = 0; i < pushThreads.length; i++) {
            pushThreads[i] = new PushThread(
                    "push-" + i,
                    deliveriesToSend,
                    deliveriesToFetch,
                    deliveryPushService,
                    deliveryAbonentRepository,
                    timer);
        }

    }

    public void init() {

    }

    public void stop(Integer invitationDeliveryId) {

    }

    public void start(Integer invitationDeliveryId) {

    }

    public void updateSpeed(Integer invitationDeliveryId, int newSpeed) {

    }


}
