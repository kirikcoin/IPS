package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.faces.context.FacesContext

class SurveySettingsController extends BaseSurveyController {

    private static final Logger logger = LoggerFactory.getLogger(SurveySettingsController)

    private final QuestionRepository questionRepository = Services.instance().questionRepository
    private final UserRepository userRepository = Services.instance().userRepository

    String errorId

    int newSurveyClientId

    Integer questionId

    SurveySettingsController() {
        super()
        newSurveyClientId = survey.client.id
    }

    void saveMessage() {
        boolean validationError =
                renderViolationMessage(validator.validate(survey.details))
        if (validationError) {
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        surveyRepository.update(survey)
        goToSurvey(surveyId)
    }

    void saveSettings() {
        survey.client = userRepository.load(newSurveyClientId)

        boolean validationError = renderViolationMessage(
                validator.validate(survey),
                [
                        'details.title': 'newSurveyTitle',
                        'startDate': 'newSurveyStartDate',
                        'endDate': 'newSurveyEndDate',
                        'statistics.accessNumber': 'newAccessNumber'
                ])

        if (validationError) {
            surveyRepository.refresh(survey)
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        surveyRepository.update(survey)
        goToSurvey(surveyId)
    }

    String deleteSurvey() {
        // Feels safer to reload
        def survey = surveyRepository.load(surveyId)
        survey.active = false
        surveyRepository.update(survey)

        return "SURVEY_LIST"
    }

    void moveUp() {
        int questionId = getParamValue("questionId").asInteger()
        survey.moveUp(questionRepository.load(questionId))
        surveyRepository.update(survey)

        survey = surveyRepository.load(surveyId)
    }

    void moveDown() {
        int questionId = getParamValue("questionId").asInteger()
        survey.moveDown(questionRepository.load(questionId))
        surveyRepository.update(survey)

        survey = surveyRepository.load(surveyId)
    }

    void deleteQuestion() {
        int questionId = getParamValue("questionId").asInteger()
        def question = questionRepository.load(questionId)

        survey.questions.remove(question)
        surveyRepository.update(survey)

        survey = surveyRepository.load(surveyId)
    }

    static void goToSurvey(int surveyId) {
        FacesContext.currentInstance.externalContext
                .redirect("/pages/surveys/settings.faces?id=${surveyId}")
    }

//    private void redirect() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ext = context.getExternalContext();
//        UIViewRoot view = context.getViewRoot();
//        String actionUrl = context.getApplication().getViewHandler().getActionURL(
//                context, view.getViewId());
//        try {
//            // TODO encode id value
//            actionUrl = ext.encodeActionURL(actionUrl + "?id=" + surveyId);
//            ext.redirect(actionUrl);
//        } catch (IOException e) {
//            throw new FacesException(e);
//        }
//    }
}
