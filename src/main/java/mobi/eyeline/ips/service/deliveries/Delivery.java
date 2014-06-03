package mobi.eyeline.ips.service.deliveries;

import mobi.eyeline.ips.model.InvitationDelivery;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Delivery {

    private final InvitationDelivery invitationDelivery;

    private long lastSentMillis = 0;
    private long proposedDelayMillis;

    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();


    Delivery(InvitationDelivery invitationDelivery) {
        this.invitationDelivery = invitationDelivery;
        this.proposedDelayMillis = TimeUnit.SECONDS.toMillis(1 / invitationDelivery.getSpeed());
    }

    public InvitationDelivery getInvitationDelivery() {
        return invitationDelivery;
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

    public BlockingQueue<Message> getMessages() {
        return messages;
    }

    /**
     * Updates the last sent timestamp.
     * @return Current required delay.
     */
    public long markSentNow() {
        lastSentMillis = System.currentTimeMillis();
        return getCurrentDelay();
    }

    public String getUrl(String msisdn, Long id) {
        return null;
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
}
