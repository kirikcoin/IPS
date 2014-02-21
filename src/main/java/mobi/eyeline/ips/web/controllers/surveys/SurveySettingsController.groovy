package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.PushService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UssdService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.PhoneValidator
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
    private final PushService pushService = Services.instance().pushService

    String errorId

    int newSurveyClientId

    Integer questionId

    final User currentUser


    // Question modification
    Question question = new Question()
    DynamicTableModel questionOptions = new DynamicTableModel()

    List<TerminalOption> terminalValues = [TerminalOption.FALSE, TerminalOption.TRUE]

    // Phone number for survey preview.

    boolean previewSentOk

    SurveySettingsController() {
        super()
        currentUser = userRepository.getByLogin(this.userName)
        newSurveyClientId = survey.client.id
    }

    String getSurveyUrl() { ussdService.getSurveyUrl(persistedSurvey) }

    void saveMessage() {
        boolean validationError =
                renderViolationMessage(validator.validate(survey.details))
        if (validationError) {
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        persistedSurvey.details.endText = survey.details.endText
        surveyRepository.update(persistedSurvey)
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
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        persistedSurvey.details.title = survey.details.title
        persistedSurvey.startDate = survey.startDate
        persistedSurvey.endDate = survey.endDate
        persistedSurvey.statistics.accessNumber = survey.statistics.accessNumber

        surveyRepository.update(persistedSurvey)
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
        persistedSurvey.moveUp(questionRepository.load(questionId))
        surveyRepository.update(persistedSurvey)

        persistedSurvey = surveyRepository.load(surveyId)
    }

    void moveDown() {
        int questionId = getParamValue("questionId").asInteger()
        persistedSurvey.moveDown(questionRepository.load(questionId))
        surveyRepository.update(persistedSurvey)

        persistedSurvey = surveyRepository.load(surveyId)
    }

    void deleteQuestion() {
        int questionId = getParamValue("questionId").asInteger()

        def question = questionRepository.load(questionId)
        question.active = false
        questionRepository.update(question)

        persistedSurvey = surveyRepository.load(surveyId)
    }

    String modifyQuestion() {
        Integer questionId = getParamValue("questionId").asInteger()

        if (questionId != null) {
            question = questionRepository.load(questionId)

            questionOptions = new DynamicTableModel()
            question.options
                    .findAll { it.active }
                    .each {
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

        errorId = 'questionModificationDialog'

        return null
    }

    void saveQuestion() {
        def persistedQuestion = (questionId != null) ?
                questionRepository.load(questionId) : new Question(survey: persistedSurvey)

        updateQuestionModel(persistedQuestion)

        boolean validationError = renderViolationMessage(
                validator.validate(persistedQuestion))
        if (validationError) {
            errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        if (questionId == null) {
            persistedSurvey.questions.add(persistedQuestion)
            surveyRepository.update(persistedSurvey)

        } else {
            questionRepository.saveOrUpdate(persistedQuestion)
        }

        goToSurvey(surveyId)
    }

    void onCancel() {
        survey = surveyRepository.load(surveyId)
    }

    void sendPreview() {
        if (!isPhoneValid()) {
            addErrorMessage(
                    resourceBundle.getString('invalid.phone.number'),
                    'previewPhone')
            errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
        } else {
            pushService.scheduleSend(survey, currentUser.phoneNumber)
            previewSentOk = true
        }
    }

    private boolean isPhoneValid() {
        new PhoneValidator().validate(currentUser.phoneNumber)
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

            def deleted = persistedQuestion.options.findAll { !(it.id in retainedOptionIds) }
            deleted.each { it.active = false }
        }

        def handleUpdated = {
            persistedQuestion.options.each { option ->
                def row = questionOptions.rows
                        .findAll { !getId(it).empty }
                        .find { getId(it).toInteger() == option.id }
                if (row != null) {
                    option.answer = getAnswer(row)
                    option.terminal = getTerminal(row)
                }
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
}
