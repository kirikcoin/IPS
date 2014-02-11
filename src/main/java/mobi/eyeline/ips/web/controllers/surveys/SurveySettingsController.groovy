package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UssdService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableModel
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableRow
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.faces.context.FacesContext

class SurveySettingsController extends BaseSurveyController {

    private static final Logger logger = LoggerFactory.getLogger(SurveySettingsController)

    private final QuestionRepository questionRepository = Services.instance().questionRepository
    private final UserRepository userRepository = Services.instance().userRepository
    private final UssdService ussdService = Services.instance().ussdService

    String errorId

    int newSurveyClientId

    Integer questionId

    // Question modification
    boolean questionEditMode
    Question question = new Question()
    DynamicTableModel questionOptions = new DynamicTableModel()

    List<TerminalOption> terminalValues = [TerminalOption.TRUE, TerminalOption.FALSE]

    SurveySettingsController() {
        super()
        newSurveyClientId = survey.client.id
    }

    String getSurveyUrl() { ussdService.getSurveyUrl(survey) }

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

    String modifyQuestion() {
        Integer questionId = getParamValue("questionId").asInteger()

        if (questionId != null) {
            question = questionRepository.load(questionId)

            questionOptions = new DynamicTableModel()
            question.options.each {
                def row = new DynamicTableRow()
                row.setValue("answer", it.answer)
                row.setValue("terminal", it.terminal)
                row.setValue("id", it.id)
                questionOptions.addRow(row)
            }
        } else {
            question = new Question()
            questionOptions = new DynamicTableModel()
        }

        questionEditMode = true

        return null
    }

    void saveQuestion() {
        questionEditMode = true

        def persistedQuestion = (questionId != null) ?
                questionRepository.load(questionId) : new Question(survey: survey)

        updateQuestionModel(persistedQuestion)

        boolean validationError = renderViolationMessage(
                validator.validate(persistedQuestion))
        if (validationError) {
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        if (questionId == null) {
            survey.questions.add(persistedQuestion)
            surveyRepository.update(survey)

        } else {
            questionRepository.saveOrUpdate(persistedQuestion)
        }

        goToSurvey(surveyId)
    }

    private void updateQuestionModel(Question persistedQuestion) {
        def getId = { row -> row.getValue('id') as String }
        def getAnswer = { row -> row.getValue('answer') as String }
        def getTerminal = { row -> (row.getValue('terminal') as String).toBoolean() }

        persistedQuestion.title = question.title

        def handleRemoved = {
            def retainedOptionIds = questionOptions.rows
                    .collect { getId(it) }
                    .findAll { !it.empty }
                    .collect { it.toInteger() }

            persistedQuestion.options.retainAll { it.id in retainedOptionIds }
        }

        def handleUpdated = {
            persistedQuestion.options.each { option ->
                def row = questionOptions.rows
                        .findAll { !getId(it).empty }
                        .find { getId(it).toInteger() == option.id }
                option.answer = getAnswer(row)
                option.terminal = getTerminal(row)
            }
        }

        def handleAdded = {
            questionOptions.rows
                    .findAll { getId(it).empty }
                    .each {
                persistedQuestion.options.add new QuestionOption(
                        question: persistedQuestion, answer: getAnswer(it))
            }
        }

        handleRemoved()
        handleUpdated()
        handleAdded()
    }

    static void goToSurvey(int surveyId) {
        FacesContext.currentInstance.externalContext
                .redirect("/pages/surveys/settings.faces?id=${surveyId}")
    }

    static abstract class TerminalOption {
        abstract boolean getValue()
        abstract String getLabel()

        static final TerminalOption TRUE = new TerminalOption() {
            boolean getValue() { true }
            String getLabel() { BaseController.getResourceBundle().getString("question.option.terminal.yes") }
        }

        static final TerminalOption FALSE = new TerminalOption() {
            boolean getValue() { false }
            String getLabel() { BaseController.getResourceBundle().getString("question.option.terminal.no") }
        }

        static TerminalOption forValue(boolean value) { value ? TRUE : FALSE }
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
