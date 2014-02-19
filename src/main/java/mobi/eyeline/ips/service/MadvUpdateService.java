package mobi.eyeline.ips.service;

import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.external.madv.BannerInfo;
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.external.madv.DeliveryInfo;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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

    private class UpdateTask extends TimerTask {

        // TODO: Ensure that sessions get actually created here,
        // cannot rely on OpenSessionInView pattern.
        private final SurveyRepository surveyRepository =
                Services.instance().getSurveyRepository();

        private void processSurvey(Survey survey, int campaignId) {
            try {
                final CampaignsSoapImpl campaignSoap = MadvSoapApi.get(config.getMadvUrl(),
                                                                     config.getMadvUserLogin(),
                                                                     config.getMadvUserPassword());
                int count = 0;

                final DeliveryInfo[] listDeliveries =
                        campaignSoap.listDeliveries(campaignId);

                if (listDeliveries != null) {
                    for (DeliveryInfo delivery : listDeliveries) {
                        count += delivery.getImpressionsCount();
                    }
                }

                final BannerInfo[] listBanners =
                        campaignSoap.listBanners(campaignId);
                if (listBanners != null) {
                    for (BannerInfo banner : listBanners) {
                        count += banner.getImpressionsCount();
                    }
                }
                survey.getStatistics().setSentCount(count);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                update();

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
            try {
                if (isNotEmpty(campaign) && StringUtils.isInteger(campaign)) {
                    processSurvey(survey, Integer.parseInt(campaign));
                }
            } catch (Exception e) {
             logger.error("Error in update process");
            }
            // TODO: do the actual update here.
        }
    }
}
