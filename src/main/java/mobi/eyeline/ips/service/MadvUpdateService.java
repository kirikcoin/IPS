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
    private final MadvSoapApi madvSoapApi;
    private final SurveyStatsRepository surveyStatsRepository;
    private final SurveyRepository surveyRepository;

    private final Timer timer;

    private volatile boolean regularUpdateStarted;
    private final Object startLock = new Object();

    public MadvUpdateService(Config config,
                             MadvSoapApi madvSoapApi,
                             SurveyStatsRepository surveyStatsRepository,
                             SurveyRepository surveyRepository) {
        this.config = config;
        this.madvSoapApi = madvSoapApi;
        this.surveyStatsRepository = surveyStatsRepository;
        this.surveyRepository = surveyRepository;

        timer = createTimer();
    }

    public void start() {
        if (!config.isMadvUpdateEnabled()) {
            logger.info("MADV update service disabled");
            return;
        }

        synchronized (startLock) {
            if (regularUpdateStarted) {
                throw new AssertionError("MADV update service already started");
            }
            regularUpdateStarted = true;
        }

        // Schedule the task for immediate execution.
        final UpdateAllTask task =
                new UpdateAllTask(config, madvSoapApi, surveyStatsRepository, surveyRepository);
        final long delayMillis =
                TimeUnit.MINUTES.toMillis(config.getMadvUpdateDelayMinutes());
        timer.schedule(task, 0, delayMillis);

        logger.info("MADV update service started");
    }

    protected Timer createTimer() {
        return new Timer("madv-update");
    }

    /**
     * Puts another update in the queue.<br/>
     * In case another update process is being executed now, a new one is scheduled next.
     */
    public void runNow() {
        final UpdateAllTask task =
                new UpdateAllTask(config, madvSoapApi, surveyStatsRepository, surveyRepository);
        timer.schedule(task, 0);
        logger.info("MADV update task scheduled");
    }

    public void runNow(int surveyId) {
        final UpdateSingleTask task = new UpdateSingleTask(config,
                madvSoapApi, surveyStatsRepository, surveyRepository, surveyId);
        timer.schedule(task, 0);
        logger.info("MADV update task scheduled");
    }


    //
    //
    //

    private static abstract class BaseUpdateTask extends TimerTask {
        private final Config config;
        private final MadvSoapApi madvSoapApi;
        private final SurveyStatsRepository surveyStatsRepository;

        protected BaseUpdateTask(Config config,
                                 MadvSoapApi madvSoapApi,
                                 SurveyStatsRepository surveyStatsRepository) {
            this.config = config;
            this.madvSoapApi = madvSoapApi;
            this.surveyStatsRepository = surveyStatsRepository;
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
                final CampaignsSoapImpl api = madvSoapApi.get(
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

        private final SurveyRepository surveyRepository;

        public UpdateAllTask(Config config,
                             MadvSoapApi madvSoapApi,
                             SurveyStatsRepository surveyStatsRepository,
                             SurveyRepository surveyRepository) {
            super(config, madvSoapApi, surveyStatsRepository);
            this.surveyRepository = surveyRepository;
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

        private final SurveyRepository surveyRepository;
        private final int surveyId;

        public UpdateSingleTask(Config config,
                                MadvSoapApi madvSoapApi,
                                SurveyStatsRepository surveyStatsRepository,
                                SurveyRepository surveyRepository,
                                int surveyId) {
            super(config, madvSoapApi, surveyStatsRepository);
            this.surveyRepository = surveyRepository;
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
