package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.InvitationDelivery;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Delivery {

    private final InvitationDelivery invitationDelivery;
    private final int messagesQueueSize;
    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    private long lastSentMillis = 0;
    private volatile long proposedDelayMillis;

    private volatile boolean stopped;

    public Delivery(InvitationDelivery invitationDelivery,
                    int messagesQueueSize) {

        this.invitationDelivery = invitationDelivery;
        this.messagesQueueSize = messagesQueueSize;
        setSpeed(invitationDelivery.getSpeed());
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
    public long getCurrentDelay() {
        final long now = System.currentTimeMillis();

        if (lastSentMillis == 0) {
            return 0;

        } else {
            final long proposedNext = lastSentMillis + proposedDelayMillis;
            return now > proposedNext ? 0 : proposedNext - now;
        }
    }

    public Message poll() {
        return messages.poll();
    }

    public void put(Message message) throws InterruptedException {
        messages.put(message);
    }

    public boolean shouldBeFilled() {
        final double loadFactor = 0.5;
        return (messages.size() / messagesQueueSize) < loadFactor;
    }

    public int getFreeSize() {
        return messagesQueueSize - messages.size();
    }

    /**
     * Updates the last sent timestamp.
     */
    public void onMessageSent() {
        lastSentMillis = System.currentTimeMillis();
    }

    public static class Message {
        private final int id;
        private final String msisdn;

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
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped() {
        this.stopped = true;
    }
}
