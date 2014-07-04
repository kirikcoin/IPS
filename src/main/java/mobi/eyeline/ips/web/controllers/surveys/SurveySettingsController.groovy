package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.SurveyPattern
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.CouponService
import mobi.eyeline.ips.service.EsdpService
import mobi.eyeline.ips.service.EsdpServiceSupport
import mobi.eyeline.ips.service.PushService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.PhoneValidator
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableModel
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableRow

import javax.faces.context.FacesContext
import javax.faces.model.SelectItem

import static mobi.eyeline.ips.web.controllers.surveys.SurveySettingsController.EndSmsType.*

@SuppressWarnings("UnnecessaryQualifiedReference")
@CompileStatic
@Slf4j('logger')
class SurveySettingsController extends BaseSurveyController {

    private final QuestionRepository questionRepository = Services.instance().questionRepository
    private final UserRepository userRepository = Services.instance().userRepository
    private final AccessNumberRepository accessNumberRepository = Services.instance().accessNumberRepository

    private final PushService pushService = Services.instance().pushService
    private final CouponService couponService = Services.instance().couponService
    private final EsdpService esdpService = Services.instance().esdpService
    private final EsdpServiceSupport esdpServiceSupport = Services.instance().esdpServiceSupport

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

    EndSmsType endSmsType = determineEndSmsType()
    EndSmsType determineEndSmsType() {
        if (!survey.details.endSmsEnabled) return DISABLED
        return survey.activePattern != null ? COUPON : SMS
    }

    boolean couponEnabled = endSmsType == COUPON
    SurveyPattern.Mode currentPatternMode =
            !couponEnabled ? SurveyPattern.Mode.DIGITS : survey.activePattern.mode
    int currentPatternLength = survey.activePattern == null ? 4 : survey.activePattern.length

    List<SelectItem> patternModes = SurveyPattern.Mode.values()
            .collect { SurveyPattern.Mode mode ->
        def key = "survey.settings.end.message.coupon.format.$mode".toString()

        new SelectItem( mode, BaseController.strings[key] as String)
    }.toList()

    String generatorName = !couponEnabled ? null :
            BaseController.strings["survey.settings.end.message.coupon.format.${survey.activePattern.mode}".toString()]

    long couponsSent = survey.patterns.collect { SurveyPattern sp -> sp.position }.sum(0) as long
    long couponsAvailable = !couponEnabled ? 0 : couponService.getAvailable(survey)

    boolean showWarning = couponEnabled && (couponService.getPercentAvailable(survey) <= 10)
    boolean showDisabled = couponEnabled && (couponsAvailable == 0)

    String accessNumberNumber = survey.statistics.accessNumber?.number
    Integer accessNumberId = survey.statistics.accessNumber?.id

    SurveySettingsController() {
        super()
        newSurveyClientId = survey.client.id
    }

    String getSurveyUrl() { esdpServiceSupport.getServiceUrl(persistedSurvey) }

    void saveMessage() {
        survey.details.endSmsEnabled = endSmsType != DISABLED

        boolean validationError =
                renderViolationMessage(validator.validate(survey.details), [
                        'endSmsTextSet': 'endSmsText',
                        'endSmsFromSet': 'endSmsFrom'
                ])
        if (validationError) {
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap['errorId']
            return
        }

        copyEndMessage()
        copyCoupons()

        surveyRepository.update(persistedSurvey)
        goToSurvey(surveyId)
    }

    private void copyEndMessage() {
        persistedSurvey.details.endText = survey.details.endText

        persistedSurvey.details.endSmsEnabled = survey.details.endSmsEnabled
        if (persistedSurvey.details.endSmsEnabled) {
            persistedSurvey.details.endSmsText = survey.details.endSmsText
            persistedSurvey.details.endSmsFrom = survey.details.endSmsFrom

        } else {
            persistedSurvey.details.endSmsText = null
            persistedSurvey.details.endSmsFrom = null
        }
    }

    private void copyCoupons() {
        def activePattern = persistedSurvey.activePattern
        if (persistedSurvey.details.endSmsEnabled && endSmsType == COUPON) {
            if (activePattern != null &&
                    activePattern.mode == currentPatternMode &&
                    activePattern.length == currentPatternLength) {
                // Do nothing as pattern is unchanged.

            } else {
                persistedSurvey.patterns.each { SurveyPattern p -> p.active = false }

                final SurveyPattern existing = persistedSurvey.patterns.find { SurveyPattern p ->
                    p.mode == currentPatternMode && p.length == currentPatternLength
                }

                if (existing) {
                    existing.active = true

                } else {
                    persistedSurvey.patterns << new SurveyPattern(
                            survey: persistedSurvey,
                            mode: currentPatternMode,
                            length: currentPatternLength,
                            active: true
                    )
                }
            }
        } else {
            if (activePattern != null) {
                persistedSurvey.patterns.each { SurveyPattern p -> p.active = false }
            }
        }
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
                    FacesContext.currentInstance.externalContext.requestParameterMap['errorId']
            return
        }

        persistedSurvey.details.title = survey.details.title
        persistedSurvey.startDate = survey.startDate
        persistedSurvey.endDate = survey.endDate

        if (accessNumberId == null) {
            persistedSurvey.statistics.accessNumber = null
        } else {
            persistedSurvey.statistics.accessNumber = accessNumberRepository.load(accessNumberId)
        }
        persistedSurvey.client = survey.client

        surveyRepository.update(persistedSurvey)

        try {
            esdpService.update(getCurrentUser(), persistedSurvey)

        } catch (Exception e) {
            logger.error(e.message, e)
            addErrorMessage strings['esdp.error.survey.update']
            return
        }

        goToSurvey(surveyId)
    }

    String deleteSurvey() {
        // Feels safer to reload
        def survey = surveyRepository.load(surveyId)

        try {
            esdpService.delete(getCurrentUser(), survey)

        } catch (Exception e) {
            logger.error(e.message, e)
            addErrorMessage strings['esdp.error.survey.deletion']
            errorId = FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]

            return null
        }

        survey.active = false
        if (survey.statistics.accessNumber) {
            survey.statistics.accessNumber = null
        }
        surveyRepository.update(survey)

        return 'SURVEY_LIST'
    }

    void moveUp() {
        int questionId = getParamValue('questionId').asInteger()
        persistedSurvey.moveUp(questionRepository.load(questionId))
        surveyRepository.update(persistedSurvey)

        persistedSurvey = surveyRepository.load(surveyId)
    }

    void moveDown() {
        int questionId = getParamValue('questionId').asInteger()
        persistedSurvey.moveDown(questionRepository.load(questionId))
        surveyRepository.update(persistedSurvey)

        persistedSurvey = surveyRepository.load(surveyId)
    }

    void deleteQuestion() {
        int questionId = getParamValue('questionId').asInteger()

        def question = questionRepository.load(questionId)
        question.active = false
        questionRepository.update(question)

        persistedSurvey = surveyRepository.load(surveyId)
    }

    String modifyQuestion() {
        Integer questionId = getParamValue('questionId').asInteger()

        if (questionId != null) {
            question = questionRepository.load(questionId)

            questionOptions = new DynamicTableModel()
            question.options
                    .findAll { QuestionOption it -> it.active }
                    .each { QuestionOption it ->
                def row = new DynamicTableRow()
                row.setValue('answer', it.answer)
                row.setValue('terminal', it.terminal)
                row.setValue('id', it.id)
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

    @SuppressWarnings("GrMethodMayBeStatic")
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

    List<SelectItem> getAvailableAccessNumbers() {
        final List<SelectItem> items = [
                new SelectItem(null, BaseController.strings['no.access.number'] as String)
        ]

        def available = { AccessNumber number ->
            (number.surveyStats == null) || ((survey.statistics.accessNumber != null) && (number.id == survey.statistics.accessNumber.id)) }

        accessNumberRepository.list().each {AccessNumber number ->
            items << new SelectItem(number.id, number.number, number.number, !available(number))
        }

        return items
    }

    static void goToSurvey(int surveyId) {
        FacesContext.currentInstance.externalContext
                .redirect("/pages/surveys/settings.faces?id=${surveyId}")
    }

    static abstract class TerminalOption {
        abstract boolean getValue()
        abstract String getLabel()

        static final TerminalOption TRUE = [
                getValue: { true },
                getLabel: { BaseController.strings['question.option.terminal.yes'] }
        ] as TerminalOption

        static final TerminalOption FALSE = [
                getValue: { false },
                getLabel: { BaseController.strings['question.option.terminal.no'] }
        ] as TerminalOption
    }

    static enum EndSmsType {
        DISABLED,
        SMS,
        COUPON
    }
}
