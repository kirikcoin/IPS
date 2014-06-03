package mobi.eyeline.ips.service.deliveries;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.DeliveryAbonentRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final DeliveryAbonentRepository deliveryAbonentRepository;
    private final DeliveryPushService deliveryPushService;

    private final int messagesQueueSize;
    final int pushThreadsNumber;

    private final ExecutorService fetchExecutor;
    private final ExecutorService pushExecutor;

    private final LinkedBlockingQueue<Delivery> toFetch = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Delivery> toSend = new LinkedBlockingQueue<>();

    private final ConcurrentHashMap<Integer, Delivery> deliveries = new ConcurrentHashMap<>();

    public DeliveryService(InvitationDeliveryRepository invitationDeliveryRepository,
                           DeliveryAbonentRepository deliveryAbonentRepository,
                           DeliveryPushService deliveryPushService,
                           Config config) {
        this.invitationDeliveryRepository = invitationDeliveryRepository;
        this.deliveryAbonentRepository = deliveryAbonentRepository;
        this.deliveryPushService = deliveryPushService;

        this.messagesQueueSize = config.getMessageQueueBaseline();
        this.pushThreadsNumber = config.getPushThreadsNumber();

        fetchExecutor =
                newFixedThreadPool(1, createTf("push-fetch-%d"));
        pushExecutor =
                newFixedThreadPool(pushThreadsNumber, createTf("push-%d"));
    }

    private static ThreadFactory createTf(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }


    //
    //  Lifecycle.
    //

    /**
     * Starts
     */
    public void start() {

        // Start the threads.

        final Timer timer = new Timer("push-timer");
        for (int i = 0; i < pushThreadsNumber; i++) {
            pushExecutor.submit(new PushThread(
                    toSend,
                    toFetch,
                    deliveryPushService,
                    deliveryAbonentRepository,
                    timer));
        }

        fetchExecutor.execute(
                new FetchThread(invitationDeliveryRepository, toFetch));

        // Start deliveries.

        deliveryAbonentRepository.clearQueued();
        for (InvitationDelivery delivery : invitationDeliveryRepository.list()) {
            start(delivery.getId());
        }

    }

    /**
     * Stops all the underlying threads, should be called on application shutdown.
     */
    public void stop() throws InterruptedException {
        pushExecutor.shutdownNow();
        fetchExecutor.shutdownNow();
    }


    //
    //  Delivery state management.
    //

    /**
     * @param invitationDeliveryId    Database ID.
     */
    public void stop(Integer invitationDeliveryId) {
        final Delivery delivery = deliveries.get(invitationDeliveryId);
        if (delivery == null) {
            throw new IllegalStateException("Unknown delivery, id = " + invitationDeliveryId);
        }

        if (delivery.isStopped()) {
            throw new IllegalStateException("Attempt to stop an already stopped delivery, " +
                    "id = " + invitationDeliveryId);
        }

        delivery.setStopped();
    }

    /**
     * @param invitationDeliveryId    Database ID.
     */
    public void start(Integer invitationDeliveryId) {

        final Delivery existing = deliveries.get(invitationDeliveryId);
        if (existing != null && !existing.isStopped()) {
            throw new IllegalStateException("Attempt to start an already running delivery, " +
                    "id = " + invitationDeliveryId);
        }

        final Delivery delivery = new Delivery(
                invitationDeliveryRepository.load(invitationDeliveryId),
                messagesQueueSize);

        deliveries.put(invitationDeliveryId, delivery);

        try {
            toSend.put(delivery);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * @param invitationDeliveryId Database ID.
     * @param newSpeed             Messages per second.
     */
    public void updateSpeed(Integer invitationDeliveryId, int newSpeed) {
        deliveries.get(invitationDeliveryId).setSpeed(newSpeed);
    }

}
