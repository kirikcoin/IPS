package mobi.eyeline.ips.service;

import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.model.InvitationUpdateStatus;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveyStats;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final MadvSoapApi soapApi;
    private final MadvService madvService;
    private final SurveyStatsRepository surveyStatsRepository;
    private final SurveyRepository surveyRepository;

    private final Timer timer;

    private volatile boolean regularUpdateStarted;
    private final Object startLock = new Object();

    public MadvUpdateService(Config config,
                             MadvSoapApi soapApi,
                             MadvService madvService,
                             SurveyStatsRepository surveyStatsRepository,
                             SurveyRepository surveyRepository) {
        this.config = config;
        this.soapApi = soapApi;
        this.madvService = madvService;
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
                new UpdateAllTask(soapApi, madvService, surveyStatsRepository, surveyRepository);
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
                new UpdateAllTask(soapApi, madvService, surveyStatsRepository, surveyRepository);
        timer.schedule(task, 0);
        logger.info("MADV update task scheduled");
    }

    public void runNow(int surveyId) {
        final UpdateSingleTask task = new UpdateSingleTask(soapApi,
                madvService, surveyStatsRepository, surveyRepository, surveyId);
        timer.schedule(task, 0);
        logger.info("MADV update task scheduled");
    }


    //
    //
    //

    private static abstract class BaseUpdateTask extends TimerTask {
        private final MadvSoapApi madvSoapApi;
        private final MadvService madvService;
        private final SurveyStatsRepository surveyStatsRepository;

        protected BaseUpdateTask(MadvSoapApi madvSoapApi,
                                 MadvService madvService,
                                 SurveyStatsRepository surveyStatsRepository) {
            this.madvSoapApi = madvSoapApi;
            this.madvService = madvService;
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

            final CampaignsSoapImpl soapApi;
            try {
                soapApi = madvSoapApi.getSoapApi();
            } catch (Exception e) {
                logger.error("MADV update failed, " +
                        "survey = [" + survey + "], " +
                        "campaign ID = [" + survey.getStatistics().getCampaign() + "]", e);
                updateStats(survey, SERVER_IS_NOT_AVAILABLE, 0);
                return;
            }

            try {
                final int count = madvService.countViews(soapApi, campaignId);
                updateStats(survey, SUCCESSFUL, count);

            } catch (Exception e) {
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

    }


    //
    //
    //

    private static class UpdateAllTask extends BaseUpdateTask {

        private final SurveyRepository surveyRepository;

        public UpdateAllTask(MadvSoapApi soapApi,
                             MadvService madvService,
                             SurveyStatsRepository surveyStatsRepository,
                             SurveyRepository surveyRepository) {
            super(soapApi, madvService, surveyStatsRepository);
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

        public UpdateSingleTask(MadvSoapApi soapApi,
                                MadvService madvService,
                                SurveyStatsRepository surveyStatsRepository,
                                SurveyRepository surveyRepository,
                                int surveyId) {
            super(soapApi, madvService, surveyStatsRepository);
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
