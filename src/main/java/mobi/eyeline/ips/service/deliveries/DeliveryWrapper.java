package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

class DeliveryWrapper {

    private static final double LOAD_FACTOR = 0.5;

    private final InvitationDelivery invitationDelivery;
    private final int messagesQueueSize;
    private final Queue<Message> messages = new ConcurrentLinkedQueue<>();

    private long lastSentMillis = 0;
    private volatile long proposedDelayMillis;

    private volatile boolean stopped;
    private volatile boolean empty;

    // XXX:DEBUG
    long lastStartMillis = new Date().getTime();

    public DeliveryWrapper(InvitationDelivery model,
                           int messagesQueueSize) {

        this.invitationDelivery = model;
        this.messagesQueueSize = messagesQueueSize;
        setSpeed(model.getSpeed());
    }

    public InvitationDelivery getModel() {
        return invitationDelivery;
    }

    public void setSpeed(int messagesPerSecond) {
        assert messagesPerSecond > 0;
        this.proposedDelayMillis = (long) (1.0 / messagesPerSecond) * 1000;
    }

    public long getProposedDelayMillis() {
        return proposedDelayMillis;
    }

    public Message poll() {
        return messages.poll();
    }

    public void put(Message message) {
        messages.add(message);
    }

    public int size() {
        return messages.size();
    }

    public boolean shouldBeFilled() {
        return (size() / messagesQueueSize) < LOAD_FACTOR;
    }

    public int getMessagesQueueSize() {
        return messagesQueueSize;
    }

    /**
     * Updates the last sent timestamp.
     */
    DeliveryWrapper onMessageSent() {
        lastSentMillis = System.currentTimeMillis();
        return this;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped() {
        this.stopped = true;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty() {
        this.empty = true;
    }

    @Override
    public String toString() {
        return "DeliveryWrapper{" +
                "model.id=" + getModel().getId() +
                '}';
    }

    public static class Message {
        private final int id;
        private final String msisdn;
        private DeliverySubscriber.State state;

        public Message(int id, String msisdn) {
            this.id = id;
            this.msisdn = msisdn;
        }

        public int getId() {
            return id;
        }

        public String getMsisdn() {
            return msisdn;
        }

        DeliverySubscriber.State getState() {
            return state;
        }

        Message setState(DeliverySubscriber.State state) {
            this.state = state;
            return this;
        }

        Message setState(boolean sent) {
            return setState(sent ? SENT : UNDELIVERED);
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", msisdn='" + msisdn + '\'' +
                    '}';
        }
    }

    public static class DelayedDeliveryWrapper implements Delayed {

        private final DeliveryWrapper deliveryWrapper;

        private final long from = System.currentTimeMillis();
        private final long delayMillis;

        private DelayedDeliveryWrapper(DeliveryWrapper deliveryWrapper,
                                         long delayMillis) {
            this.deliveryWrapper = deliveryWrapper;
            this.delayMillis = delayMillis;
        }

        @Override
        public long getDelay(@SuppressWarnings("NullableProblems") TimeUnit unit) {
            return unit.convert(
                    Math.max(from + delayMillis - System.currentTimeMillis(), 0),
                    TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@SuppressWarnings("NullableProblems") Delayed o) {
            return Long.compare(
                    this.getDelay(TimeUnit.MILLISECONDS),
                    o.getDelay(TimeUnit.MILLISECONDS));
        }

        public DeliveryWrapper getDeliveryWrapper() {
            return deliveryWrapper;
        }

        public static DelayedDeliveryWrapper forSent(DeliveryWrapper wrapper) {
            return new DelayedDeliveryWrapper(wrapper, wrapper.getProposedDelayMillis());
        }

        public static DelayedDeliveryWrapper forDelay(DeliveryWrapper wrapper,
                                                      long millis) {
            return new DelayedDeliveryWrapper(wrapper, millis);
        }
    }
}
