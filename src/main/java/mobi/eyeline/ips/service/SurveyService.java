package mobi.eyeline.ips.service;

import mobi.eyeline.ips.exceptions.SurveyNotFound;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.repository.DB;
import mobi.eyeline.ips.repository.QuestionStatsRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyInvitationRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

    private final DB db;
    private final SurveyRepository surveyRepository;
    private final SurveyInvitationRepository surveyInvitationRepository;

    public SurveyService(DB db,
                         SurveyRepository surveyRepository,
                         SurveyInvitationRepository surveyInvitationRepository) {
        this.db = db;
        this.surveyRepository = surveyRepository;
        this.surveyInvitationRepository = surveyInvitationRepository;
    }

    /**
     * Loads survey by ID.
     * <br/>
     * Returns {@code null} if at least one of these conditions is not met:
     * <ol>
     *     <li>Survey with this ID exists,</li>
     *     <li>This survey is running now,
     *     i.e. current date is between {@link Survey#startDate} and {@link Survey#endDate}</li>
     *     <li>Survey is not marked as deleted</li>
     * </ol>
     */
    public Survey findSurvey(int surveyId) {
        final Survey survey = surveyRepository.get(surveyId);

        if (survey == null) {
            logger.info("Survey not found for ID = [" + surveyId + "]");
            return null;

        } else if (!survey.isRunningNow()) {
            logger.info("Survey is not active now, ID = [" + surveyId + "]");
            return null;

        } else if (!survey.isActive()) {
            logger.info("Survey is disabled, ID = [" + surveyId + "]");
            return null;
        }

        return survey;
    }

    /**
     * @return Overall invitations count, including both manually specified and obtained from MADV,
     */
    public int countInvitations(Survey survey) {
        return survey.getStatistics().getSentCount() +
                surveyInvitationRepository.count(survey);
    }
}
