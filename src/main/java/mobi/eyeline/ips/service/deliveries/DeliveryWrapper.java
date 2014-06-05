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

class DeliveryWrapper implements Delayed {

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

    /**
     * @return Required delay till the next proposed send, positive or zero millis.
     */
    private long getCurrentDelay() {
        final long now = System.currentTimeMillis();

        if (lastSentMillis == 0) {
            return 0;

        } else {
            final long proposedNext = lastSentMillis + proposedDelayMillis;
            return Math.max(0, proposedNext - now);
        }
    }

    void setDelay(long delayMillis) {
        lastSentMillis = System.currentTimeMillis() + delayMillis;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getCurrentDelay(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(
                this.getDelay(TimeUnit.MILLISECONDS),
                o.getDelay(TimeUnit.MILLISECONDS));
    }

    public Message poll() {
        return messages.poll();
    }

    public void put(Message message) {
        messages.add(message);
    }

    public boolean shouldBeFilled() {
        return (messages.size() / messagesQueueSize) < LOAD_FACTOR;
    }

    public int getFreeSize() {
        return Math.max(0, messagesQueueSize - messages.size());
    }

    public int getMessagesQueueSize() {
        return messagesQueueSize;
    }

    /**
     * Updates the last sent timestamp.
     */
    public DeliveryWrapper onMessageSent() {
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

    public void setEmpty(boolean empty) {
        this.empty = empty;
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
}
