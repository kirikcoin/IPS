package mobi.eyeline.ips.service.deliveries;


import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static com.google.common.collect.Lists.transform;
import static mobi.eyeline.ips.service.deliveries.DeliveryWrapper.Message;

public class StateUpdateThread extends LoopThread {

  private static final Logger logger = LoggerFactory.getLogger(StateUpdateThread.class);

  private final int batchSize;
  private final BlockingQueue<Message> toStateUpdate;
  private final DeliverySubscriberRepository deliverySubscriberRepository;


  public StateUpdateThread(String name,
                           Config config,
                           BlockingQueue<Message> toStateUpdate,
                           DeliverySubscriberRepository deliverySubscriberRepository) {

    super(name);

    this.toStateUpdate = toStateUpdate;
    this.deliverySubscriberRepository = deliverySubscriberRepository;
    this.batchSize = config.getStateUpdateBatchSize();
  }

  @Override
  protected void loop() throws InterruptedException {
    final List<Message> chunk = fetchChunk();
    updateChunk(chunk);
  }

  private List<Message> fetchChunk() throws InterruptedException {
    final List<Message> chunk = new ArrayList<>(batchSize);
    chunk.add(toStateUpdate.take());
    toStateUpdate.drainTo(chunk, batchSize - 1);

    return chunk;
  }

  private void updateChunk(List<Message> chunk) {
    int retries = 3;

    do {
      try {
        doUpdateChunk(chunk);
        return;
      } catch (Exception e) {
        if (retries == 0) {
          logger.warn("State update error", e);
        } else {
          if (logger.isDebugEnabled()) {
            logger.debug("State update failed, retrying (" + e.getMessage() + " )");
          }
        }
      }

    } while (retries-- > 0);
  }

  private void doUpdateChunk(List<Message> chunk) {
    if (!chunk.isEmpty()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Updating " + chunk.size() + " entries");
      }
      // Update state only for FETCHED messages to handle the following scenario:
      // 1. Message gets sent
      // 2. Notification arrives, state is updated to either DELIVERED or UNDELIVERED
      // 3. Finally comes to updating to SENT after step 1.
      deliverySubscriberRepository.updateState(
          transform(chunk, Message.AS_PAIR),
          DeliverySubscriber.State.FETCHED);
    } else {
      logger.debug("Nothing to update");
    }
  }

  public void processRemaining() {
    final List<Message> chunk = new ArrayList<>(toStateUpdate.size());
    toStateUpdate.drainTo(chunk);
    logger.debug("Updating " + chunk.size() + " entries on shutdown");

    updateChunk(chunk);
  }
}
