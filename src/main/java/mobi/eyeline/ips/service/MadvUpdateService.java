package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.SurveyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MadvUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(MadvUpdateService.class);

    private final Config config;
    private Timer timer;

    public MadvUpdateService(Config config) {
        this.config = config;
    }

    public void start() {
        if (config.isMadvUpdateEnabled()) {
            if (timer != null) {
                throw new AssertionError("Update service already started");
            }

            timer = new Timer("smaq-update");
            // Schedule the task for immediate execution.
            final long delayMillis =
                    TimeUnit.MINUTES.toMillis(config.getMadvUpdateDelayMinutes());
            timer.schedule(new UpdateTask(), 0, delayMillis);

            logger.info("SMAQ update service started");

        } else {
            logger.info("SMAQ update service disabled");
        }
    }

    private static class UpdateTask extends TimerTask {

        // TODO: Ensure that sessions get actually created here,
        // cannot rely on OpenSessionInView pattern.
        private final SurveyRepository surveyRepository =
                Services.instance().getSurveyRepository();

        @Override
        public void run() {
            try {
                update();

//            } catch (InterruptedException e) {
//                throw e;
            } catch (Exception e) {
                logger.error("Error in scheduled update", e);
            }
        }

        private void update() throws Exception {
            for (Survey survey : surveyRepository.list()) {
                final String campaign = survey.getStatistics().getCampaign();
                if (campaign != null) {
                    update(survey, campaign);
                }
            }
        }

        private void update(Survey survey, String campaign) {
            logger.debug("Updating: survey = [" + survey + "], campaign = [" + campaign + "]");

            // TODO: do the actual update here.
        }
    }
}
