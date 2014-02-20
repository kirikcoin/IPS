package mobi.eyeline.ips.service;

import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.external.madv.BannerInfo;
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.external.madv.DeliveryInfo;
import mobi.eyeline.ips.model.InvitationUpdateStatus;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class MadvUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(MadvUpdateService.class);

    private final Config config;
    private Timer timer;
    private Date lastUpdate;


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
        timer.schedule(new UpdateTask(), 0);
        logger.info("MADV update task sheduled.");
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
                lastUpdate = new Date();
                survey.getStatistics().setUpdateStatus(InvitationUpdateStatus.SUCCESSFUL);
                survey.getStatistics().setSentCount(count);
                survey.getStatistics().setLastUpdate(lastUpdate);
                surveyRepository.update(survey);

            } catch (ServiceException | SOAPException e) {
                survey.getStatistics().setUpdateStatus(InvitationUpdateStatus.SERVER_IS_NOT_AVAILABLE);
                survey.getStatistics().setSentCount(0);
                surveyRepository.update(survey);
                logger.error("Error in scheduled update", e);
            } catch (RemoteException e) {
                survey.getStatistics().setUpdateStatus(InvitationUpdateStatus.CAMPAIGN_NOT_FOUND);
                survey.getStatistics().setSentCount(0);
                surveyRepository.update(survey);
                logger.error("Error in scheduled update", e);
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
