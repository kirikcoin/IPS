package mobi.eyeline.ips.service.deliveries;

import com.j256.simplejmx.common.JmxAttributeMethod;
import com.j256.simplejmx.common.JmxOperation;
import com.j256.simplejmx.common.JmxResource;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.util.TimeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.j256.simplejmx.common.JmxOperationInfo.OperationAction.ACTION;
import static mobi.eyeline.ips.model.InvitationDelivery.State.ACTIVE;

@JmxResource(domainName = "mobi.eyeline.ips")
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private final TimeSource timeSource;

    private final InvitationDeliveryRepository invitationDeliveryRepository;

    private final int messagesQueueSize;

    private final StateUpdateThread stateUpdateThread;

    @SuppressWarnings("FieldCanBeLocal")
    private final LinkedBlockingQueue<DeliveryWrapper> toFetch =
            new LinkedBlockingQueue<>();

    private final DelayQueue<DeliveryWrapper.DelayedDeliveryWrapper> toSend =
            new DelayQueue<>();

    @SuppressWarnings("FieldCanBeLocal")
    private final BlockingQueue<DeliveryWrapper.Message> toMark =
            new LinkedBlockingQueue<>();

    public static final ConcurrentHashMap<Integer, DeliveryWrapper> deliveries = new ConcurrentHashMap<>();

    private final List<Thread> allThreads = new ArrayList<>();

    public DeliveryService(TimeSource timeSource,
                           InvitationDeliveryRepository invitationDeliveryRepository,
                           DeliverySubscriberRepository deliverySubscriberRepository,
                           DeliveryPushService deliveryPushService,
                           Config config) {
        this.timeSource = timeSource;

        this.invitationDeliveryRepository = invitationDeliveryRepository;

        this.messagesQueueSize = config.getMessageQueueBaseline();

        final int pushThreadsNumber = config.getPushThreadsNumber();
        for (int i = 0; i < pushThreadsNumber; i++) {
            allThreads.add(new PushThread("push-" + i,
                    config.getRetryAttempts(),
                    timeSource,
                    toSend,
                    toFetch,
                    toMark,
                    deliveryPushService,
                    invitationDeliveryRepository));
        }

        allThreads.add(new FetchThread("push-fetch", invitationDeliveryRepository, toFetch));
        allThreads.add(stateUpdateThread = new StateUpdateThread(
                "push-state", config, toMark, deliverySubscriberRepository));
        allThreads.add(new ExpirationThread(
                "push-expire",
                config.getSentExpirationDelaySeconds(),
                config.getFetchedExpirationDelaySeconds(),
                deliverySubscriberRepository));
    }

    // XXX:DEBUG
    void onDeliveryKick(DeliveryWrapper delivery) {
        System.out.println("XXX:DEBUG: " + delivery + "," + " time = " + (new Date().getTime() - delivery.lastStartMillis));
    }

    @JmxAttributeMethod
    public int getToFetchSize() {
        return toFetch.size();
    }

    @JmxAttributeMethod
    public int getToSendSize() {
        return toSend.size();
    }

    @JmxAttributeMethod
    public int getToMarkSize() {
        return toMark.size();
    }

    @JmxAttributeMethod
    public String getDeliveryInfo() {
        return deliveries.values().toString();
    }

    //
    //  Lifecycle.
    //

    /**
     * Starts
     */
    public void start() {

        // Start the threads.
        for (Thread thread : allThreads) {
            thread.start();
        }

        // Start deliveries.

        for (InvitationDelivery delivery : invitationDeliveryRepository.list(ACTIVE)) {
            start(delivery.getId());
        }

    }

    /**
     * Stops all the underlying threads, should be called on application shutdown.
     */
    public void stop() throws InterruptedException {

        for (Thread thread : allThreads) {
            thread.interrupt();
        }

        stateUpdateThread.processRemaining();

        for (Thread thread : allThreads) {
            thread.join();
        }
    }


    //
    //  Delivery state management.
    //

    /**
     * @param invitationDeliveryId    Database ID.
     */
    @JmxOperation(parameterNames = {"invitationDeliveryId"}, operationAction = ACTION)
    public void stop(Integer invitationDeliveryId) {
        final DeliveryWrapper delivery = deliveries.get(invitationDeliveryId);
        if (delivery == null) {
            // Attempt to stop an already stopped delivery
            return;
        }

        delivery.setStopped();
    }

    public void stop(InvitationDelivery delivery) {
        stop(delivery.getId());
    }

    /**
     * @param invitationDeliveryId    Database ID.
     */
    @JmxOperation(parameterNames = {"invitationDeliveryId"}, operationAction = ACTION)
    public void start(Integer invitationDeliveryId) {
        start(invitationDeliveryRepository.load(invitationDeliveryId));
    }

    public void start(InvitationDelivery delivery) {
        delivery = invitationDeliveryRepository.load(delivery.getId());

        DeliveryWrapper wrapper = deliveries.get(delivery.getId());
        if (wrapper != null && !wrapper.isStopped()) {
            // Attempt to start an already running delivery.
            return;

        } else {
            wrapper = new DeliveryWrapper(delivery, messagesQueueSize);
            deliveries.put(delivery.getId(), wrapper);
        }

        invitationDeliveryRepository.update(delivery);

        toSend.put(DeliveryWrapper.DelayedDeliveryWrapper.forSent(timeSource, wrapper));
    }

    /**
     * @param invitationDeliveryId Database ID.
     * @param newSpeed             Messages per second.
     */
    @JmxOperation(parameterNames = {"invitationDeliveryId", "newSpeed"}, operationAction = ACTION)
    public void updateSpeed(Integer invitationDeliveryId, int newSpeed) {
        final DeliveryWrapper delivery = deliveries.get(invitationDeliveryId);
        if (delivery == null) {
            logger.debug("Changing speed of inactive delivery, id = " + invitationDeliveryId);
            return;
        }

        delivery.setSpeed(newSpeed);
    }

    public void updateSpeed(InvitationDelivery delivery) {
        updateSpeed(delivery.getId(), delivery.getSpeed());
    }

}
