package mobi.eyeline.ips.service.deliveries;


import com.google.common.base.Function;
import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static com.google.common.collect.Lists.transform;

public class StateUpdateThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(StateUpdateThread.class);

    private final int batchSize;
    private final BlockingQueue<DeliveryWrapper.Message> toUpdate;
    private final DeliverySubscriberRepository deliverySubscriberRepository;

    private final Function<DeliveryWrapper.Message, Pair<Integer, DeliverySubscriber.State>> asPair =
            new Function<DeliveryWrapper.Message, Pair<Integer, DeliverySubscriber.State>>() {
                @Override
                public Pair<Integer, DeliverySubscriber.State> apply(DeliveryWrapper.Message input) {
                    return Pair.of(input.getId(), input.getState());
                }
            };

    public StateUpdateThread(String name,
                             Config config,
                             BlockingQueue<DeliveryWrapper.Message> toUpdate,
                             DeliverySubscriberRepository deliverySubscriberRepository) {

        super(name);

        this.toUpdate = toUpdate;
        this.deliverySubscriberRepository = deliverySubscriberRepository;
        this.batchSize = config.getStateUpdateBatchSize();
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                loop();
            }

        } catch (InterruptedException e) {
            logger.info("StateUpdate thread interrupted");
        }
    }

    private void loop() throws InterruptedException {
        final List<DeliveryWrapper.Message> chunk = fetchChunk();
        if (!chunk.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Updating " + chunk.size() + " entries");
            }
            deliverySubscriberRepository.updateState(
                    transform(chunk, asPair),
                    DeliverySubscriber.State.NEW);
        } else {
            logger.debug("Nothing to update");
        }
    }

    private List<DeliveryWrapper.Message> fetchChunk() throws InterruptedException {
        final List<DeliveryWrapper.Message> chunk = new ArrayList<>(batchSize);
        chunk.add(toUpdate.take());
        toUpdate.drainTo(chunk, batchSize - 1);

        return chunk;
    }

    public void processRemaining() {
        final List<DeliveryWrapper.Message> chunk = new ArrayList<>();
        toUpdate.drainTo(chunk);
        logger.debug("Updating " + chunk.size() + " entries on shutdown");

        if (!chunk.isEmpty()) {
            deliverySubscriberRepository.updateState(
                    transform(chunk, asPair),
                    DeliverySubscriber.State.NEW);
        }
    }
}
