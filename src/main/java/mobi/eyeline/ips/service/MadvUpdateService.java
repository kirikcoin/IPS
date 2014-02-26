package mobi.eyeline.ips.service;

import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.external.madv.BannerInfo;
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.external.madv.DeliveryInfo;
import mobi.eyeline.ips.model.InvitationUpdateStatus;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyStats;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static mobi.eyeline.ips.model.InvitationUpdateStatus.CAMPAIGN_NOT_FOUND;
import static mobi.eyeline.ips.model.InvitationUpdateStatus.SERVER_IS_NOT_AVAILABLE;
import static mobi.eyeline.ips.model.InvitationUpdateStatus.SUCCESSFUL;
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

            timer = new Timer("madv-update");
            // Schedule the task for immediate execution.
            final long delayMillis =
                    TimeUnit.MINUTES.toMillis(config.getMadvUpdateDelayMinutes());
            timer.schedule(new UpdateAllTask(config), 0, delayMillis);

            logger.info("MADV update service started");

        } else {
            logger.info("MADV update service disabled");
        }
    }

    /**
     * Puts another update in the queue.<br/>
     * In case another update process is being executed now, a new one is scheduled next.
     */
    public void runNow() {
        timer.schedule(new UpdateAllTask(config), 0);
        logger.info("MADV update task sheduled");
    }

    public void runNow(int surveyId) {
        timer.schedule(new UpdateSingleTask(config, surveyId), 0);
        logger.info("MADV update task sheduled");
    }


    //
    //
    //

    private static abstract class BaseUpdateTask extends TimerTask {
        protected final SurveyStatsRepository surveyStatsRepository =
                Services.instance().getSurveyStatsRepository();

        private final Config config;

        protected BaseUpdateTask(Config config) {
            this.config = config;
        }

        protected void tryUpdate(Survey survey) {
            final String campaign = survey.getStatistics().getCampaign();

            logger.debug("Updating: survey = [" + survey + "], campaign = [" + campaign + "]");
            if (isNotEmpty(campaign) && StringUtils.isNumeric(campaign)) {
                try {
                    update(survey, Integer.parseInt(campaign));
                } catch (Exception e) {
                    logger.error("MADV update failed: survey = [" + survey + "]", e);
                }
            }
        }

        private void update(Survey survey, int campaignId) {
            try {
                final CampaignsSoapImpl api = MadvSoapApi.get(
                        config.getMadvUrl(),
                        config.getMadvUserLogin(),
                        config.getMadvUserPassword());

                final int count = countViews(campaignId, api);
                updateStats(survey, SUCCESSFUL, count);

            } catch (ServiceException | SOAPException | MalformedURLException e) {
                logger.error("MADV update failed, " +
                        "survey = [" + survey + "], " +
                        "campaign ID = [" + survey.getStatistics().getCampaign() + "]", e);
                updateStats(survey, SERVER_IS_NOT_AVAILABLE, 0);

            } catch (RemoteException e) {
                logger.error("MADV update failed, " +
                        "survey = [" + survey + "], " +
                        "campaign ID = [" + survey.getStatistics().getCampaign() + "]", e);
                updateStats(survey, CAMPAIGN_NOT_FOUND, 0);
            }
        }

        private void updateStats(Survey survey,
                                 InvitationUpdateStatus state,
                                 int count) {

            final SurveyStats stats = survey.getStatistics();

            stats.setUpdateStatus(state);
            stats.setSentCount(count);
            stats.setLastUpdate(new Date());
            surveyStatsRepository.update(stats);
        }

        private int countViews(int campaignId, CampaignsSoapImpl api)
                throws RemoteException {
            int count = 0;

            final DeliveryInfo[] listDeliveries = api.listDeliveries(campaignId);
            if (listDeliveries != null) {
                for (DeliveryInfo delivery : listDeliveries) {
                    count += delivery.getImpressionsCount();
                }
            }

            final BannerInfo[] listBanners = api.listBanners(campaignId);
            if (listBanners != null) {
                for (BannerInfo banner : listBanners) {
                    count += banner.getImpressionsCount();
                }
            }
            return count;
        }
    }


    //
    //
    //

    private static class UpdateAllTask extends BaseUpdateTask {

        private final SurveyRepository surveyRepository =
                Services.instance().getSurveyRepository();

        public UpdateAllTask(Config config) {
            super(config);
        }

        @Override
        public void run() {
            try {
                for (Survey survey : surveyRepository.list()) {
                    tryUpdate(survey);
                }

            } catch (Exception e) {
                logger.error("Error in MADV update", e);
            }
        }
    }


    //
    //
    //

    private static class UpdateSingleTask extends BaseUpdateTask {

        private final SurveyRepository surveyRepository =
                Services.instance().getSurveyRepository();

        private final int surveyId;

        public UpdateSingleTask(Config config, int surveyId) {
            super(config);
            this.surveyId = surveyId;
        }

        @Override
        public void run() {
            try {
                tryUpdate(surveyRepository.load(surveyId));

            } catch (Exception e) {
                logger.error("Error in MADV update, survey id = [" + surveyId + "]", e);
            }
        }
    }
}
