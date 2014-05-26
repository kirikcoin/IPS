package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.PushService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UssdService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.PhoneValidator
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableModel
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableRow

import javax.faces.context.FacesContext

@CompileStatic
@Slf4j('logger')
class SurveySettingsController extends BaseSurveyController {

    private final QuestionRepository questionRepository = Services.instance().questionRepository
    private final UserRepository userRepository = Services.instance().userRepository
    private final UssdService ussdService = Services.instance().ussdService
    private final PushService pushService = Services.instance().pushService

    String errorId

    int newSurveyClientId

    Integer questionId

    // Question modification
    Question question = new Question()
    DynamicTableModel questionOptions = new DynamicTableModel()

    List<TerminalOption> terminalValues = [TerminalOption.FALSE, TerminalOption.TRUE]

    // Phone number for survey preview.
    String phoneNumber = currentUser.phoneNumber

    boolean previewSentOk

    SurveySettingsController() {
        super()
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
                    .findAll { QuestionOption it -> it.active }
                    .each { QuestionOption it ->
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
                validator.validate(persistedQuestion), getPropertyMap(persistedQuestion))
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

    private Map<String, String> getPropertyMap(Question q) {
        def map = [:]
        (0..q.options.size()).each {
            map.put("options[${it}].answer".toString(), "questionOptions_${it}_answer".toString())
        }
        map
    }

    void onCancel() {
        survey = surveyRepository.load(surveyId)
    }

    void sendPreview() {
        if (!isPhoneValid()) {
            addErrorMessage(strings['invalid.phone.number'], 'previewPhone')
            errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
        } else {
            pushService.scheduleSend(survey, phoneNumber)
            previewSentOk = true
        }
    }

    private boolean isPhoneValid() {
        new PhoneValidator().validate(phoneNumber)
    }

    private void updateQuestionModel(Question persistedQuestion) {
        def getId       = { DynamicTableRow row -> row.getValue('id') as String }
        def getAnswer   = { DynamicTableRow row -> row.getValue('answer') as String }
        def getTerminal = { DynamicTableRow row -> (row.getValue('terminal') as String).toBoolean() }
        def index       = { DynamicTableRow row -> questionOptions.rows.indexOf(row) }

        persistedQuestion.title = question.title

        def handleRemoved = {
            def retainedOptionIds = questionOptions.rows
                    .collect { DynamicTableRow row -> getId(row) }
                    .findAll { String id -> !id.empty }
                    .collect { String id -> id.toInteger() }

            persistedQuestion.options
                    .findAll { QuestionOption opt -> !(opt.id in retainedOptionIds) }
                    .each { QuestionOption opt -> opt.active = false }
        }

        def handleUpdated = {
            persistedQuestion.activeOptions.each { QuestionOption option ->
                questionOptions.rows
                        .findAll { DynamicTableRow row -> !getId(row).empty }
                        .find { DynamicTableRow row -> getId(row).toInteger() == option.id }
                        .each { DynamicTableRow row ->
                    option.answer = getAnswer(row)
                    option.terminal = getTerminal(row)
                    option.moveTo index(row)
                }
            }
        }

        def handleAdded = {
            questionOptions.rows
                    .findAll { DynamicTableRow row -> getId(row).empty }
                    .each { DynamicTableRow row ->
                def option =
                        new QuestionOption(question: persistedQuestion, answer: getAnswer(row))
                persistedQuestion.options.add option
                option.moveTo index(row)
            }
        }

        handleRemoved()
        handleAdded()
        handleUpdated()
        print persistedQuestion
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
            String getLabel() { BaseController.strings['question.option.terminal.yes'] }
        }

        static final TerminalOption FALSE = new TerminalOption() {
            boolean getValue() { false }
            String getLabel() { BaseController.strings['question.option.terminal.no'] }
        }

        static TerminalOption forValue(boolean value) { value ? TRUE : FALSE }
    }
}
