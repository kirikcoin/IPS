package mobi.eyeline.ips.service.deliveries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class LoopThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(LoopThread.class);

    public LoopThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                loop();
            }

        } catch (InterruptedException e) {
            logger.info("Thread " + getName() + " interrupted");
        }
    }

    protected abstract void loop() throws InterruptedException;

}
