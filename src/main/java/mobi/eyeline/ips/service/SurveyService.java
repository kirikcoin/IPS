package mobi.eyeline.ips.service;

import mobi.eyeline.ips.exceptions.SurveyNotFound;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.repository.DB;
import mobi.eyeline.ips.repository.PermissionsRepository;
import mobi.eyeline.ips.repository.QuestionStatsRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurveyService {

    private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);

    private final DB db;
    private final SurveyRepository surveyRepository;
    private final PermissionsRepository permissionsRepository;
    private final RespondentRepository respondentRepository;
    private final SurveyStatsRepository surveyStatsRepository;
    private final QuestionStatsRepository questionStatsRepository;

    public SurveyService(DB db,
                         SurveyRepository surveyRepository,
                         PermissionsRepository permissionsRepository,
                         RespondentRepository respondentRepository,
                         SurveyStatsRepository surveyStatsRepository,
                         QuestionStatsRepository questionStatsRepository) {
        this.db = db;
        this.surveyRepository = surveyRepository;
        this.permissionsRepository = permissionsRepository;
        this.respondentRepository = respondentRepository;
        this.surveyStatsRepository = surveyStatsRepository;
        this.questionStatsRepository = questionStatsRepository;
    }

    public Survey getAvailableSurvey(Integer surveyId,
                                     User user,
                                     boolean active) throws SurveyNotFound {

        final Survey survey = surveyRepository.get(surveyId);
        if (survey == null) {
            throw new SurveyNotFound(surveyId);
        }

        if ((active && !survey.isActive()) ||
            (permissionsRepository.get(survey, user) == null)) {
            throw new SurveyNotFound(surveyId);
        }

        return survey;
    }

    private void cleanSurveyData(Session session, Survey survey) {
        respondentRepository.deleteBySurvey(session, survey);
        questionStatsRepository.delete(session, survey);
//        surveyRepository.deleteSelectedQuestions(session, survey);
        surveyStatsRepository.delete(session, survey);

        session.update(survey);
    }

}
