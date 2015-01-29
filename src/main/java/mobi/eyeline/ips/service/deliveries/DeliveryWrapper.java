package mobi.eyeline.ips.service.deliveries;

import com.google.common.base.Function;
import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.model.InvitationDelivery;
import mobi.eyeline.ips.util.TimeSource;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

public class DeliveryWrapper {

    private static final double LOAD_FACTOR = 0.5;

    private final InvitationDelivery invitationDelivery;
    private final int messagesQueueSize;
    private final Queue<Message> messages = new ConcurrentLinkedQueue<>();
    private final HashMap<String,Integer> respondentAttemptsNumber = new HashMap<>();

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
        this.proposedDelayMillis = (long) ((1.0 / messagesPerSecond) * 1000);
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

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped() {
        this.stopped = true;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean isEmpty) {
        this.empty = isEmpty;
    }

    @Override
    public String toString() {
        return "DeliveryWrapper{" +
                "model.id=" + getModel().getId() +
                ",size=" + size() +
                ",stopped=" + stopped +
                ",empty=" + empty +
                '}';
    }

    // Single thread access
    public static class Message {
        private final int id;
        private final String msisdn;
        private DeliverySubscriber.State state;

        private int sendAttempts = 0;

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

        Message setSent(boolean sent) {
            return setState(sent ? SENT : UNDELIVERED);
        }

        public int incrementAndGet() {
            return ++sendAttempts;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "id=" + id +
                    ", msisdn='" + msisdn + '\'' +
                    ", sendAttempts=" + sendAttempts +
                    '}';
        }

        public static final Function<Message, Pair<Integer, DeliverySubscriber.State>> AS_PAIR =
                new Function<Message, Pair<Integer, DeliverySubscriber.State>>() {
                    @Override
                    public Pair<Integer, DeliverySubscriber.State> apply(Message input) {
                        return Pair.of(input.getId(), input.getState());
                    }
                };
    }

    public static class DelayedDeliveryWrapper implements Delayed {

        private final TimeSource timeSource;

        private final DeliveryWrapper deliveryWrapper;

        private final long from;
        private final long delayMillis;

        private DelayedDeliveryWrapper(TimeSource timeSource,
                                       DeliveryWrapper deliveryWrapper,
                                       long delayMillis) {
            this.timeSource = timeSource;
            this.deliveryWrapper = deliveryWrapper;
            this.from = timeSource.currentTimeMillis();
            this.delayMillis = delayMillis;
        }

        @Override
        public long getDelay(@SuppressWarnings("NullableProblems") TimeUnit unit) {
            return unit.convert(
                    Math.max(from + delayMillis - timeSource.currentTimeMillis(), 0),
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

        public static DelayedDeliveryWrapper forSent(TimeSource timeSource,
                                                     DeliveryWrapper wrapper) {
            return new DelayedDeliveryWrapper(
                    timeSource, wrapper, wrapper.getProposedDelayMillis());
        }

        public static DelayedDeliveryWrapper forDelay(TimeSource timeSource,
                                                      DeliveryWrapper wrapper,
                                                      long millis) {
            return new DelayedDeliveryWrapper(timeSource, wrapper, millis);
        }
    }

    public boolean hasMessagesToRetry() {
        return respondentAttemptsNumber.size() != 0;
    }

    public HashMap<String, Integer> getRespondentAttemptsNumber() {
        return respondentAttemptsNumber;
    }
}