package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode
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
import mobi.eyeline.ips.service.MobilizerSegmentation
import mobi.eyeline.ips.service.PushService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.SurveyService
import mobi.eyeline.ips.util.SurveyTreeUtil
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.PhoneValidator
import mobi.eyeline.ips.web.validators.SimpleConstraintViolation
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableModel
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableRow

import javax.faces.bean.ManagedBean
import javax.faces.context.FacesContext
import javax.faces.model.SelectItem
import javax.validation.ConstraintViolation
import java.text.MessageFormat

import static mobi.eyeline.ips.web.controllers.TimeZoneHelper.formatDateTime
import static mobi.eyeline.ips.web.controllers.surveys.SurveySettingsController.EndSmsType.COUPON
import static mobi.eyeline.ips.web.controllers.surveys.SurveySettingsController.EndSmsType.DISABLED
import static mobi.eyeline.ips.web.controllers.surveys.SurveySettingsController.EndSmsType.SMS

@SuppressWarnings('UnnecessaryQualifiedReference')
@CompileStatic
@Slf4j('logger')
@ManagedBean(name = "surveySettingsController")
class SurveySettingsController extends BaseSurveyController {

  private final QuestionRepository questionRepository = Services.instance().questionRepository
  private final UserRepository userRepository = Services.instance().userRepository
  private
  final AccessNumberRepository accessNumberRepository = Services.instance().accessNumberRepository

  private final PushService pushService = Services.instance().pushService
  private final CouponService couponService = Services.instance().couponService
  private final SurveyService surveyService = Services.instance().surveyService
  private final EsdpService esdpService = Services.instance().esdpService
  private final EsdpServiceSupport esdpServiceSupport = Services.instance().esdpServiceSupport

  String errorId

  String settingsStartDate
  String settingsEndDate

  boolean openedGraph

  int newSurveyClientId

  Integer questionId

  // Question modification
  Question question = new Question()
  Integer defaultQuestionId
  DynamicTableModel questionOptions = new DynamicTableModel()

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

    new SelectItem(mode, BaseController.strings[key] as String)
  }.toList()

  String generatorName = !couponEnabled ? null :
      BaseController.strings["survey.settings.end.message.coupon.format.${survey.activePattern.mode}".toString()]

  long couponsSent = survey.patterns.collect { it.position }.sum(0) as long
  long couponsAvailable = !couponEnabled ? 0 : couponService.getAvailable(survey)

  boolean showWarning = couponEnabled && (couponService.getPercentAvailable(survey) <= 10)
  boolean showDisabled = couponEnabled && (couponsAvailable == 0)

  String accessNumberNumber = survey.statistics.accessNumber?.number
  Integer accessNumberId = survey.statistics.accessNumber?.id

  TreeNode questionsGraph

  String questionDeletePrompt

  SurveySettingsController() {
    super()
    newSurveyClientId = survey.client.id

    updateQuestionsGraph()

    settingsStartDate = formatDateTime(survey.startDate, getTimeZone())
    settingsEndDate = formatDateTime(survey.endDate, getTimeZone())

  }

  List<SelectItem> getNextQuestionsList() {
    [
        new SelectItem(-1, strings['question.option.terminal.inlist'] as String),
        *activeQuestions
    ] as List<SelectItem>
  }

  List<SelectItem> getDefaultQuestionsList() {
    [
        // -1 - disabled default answer
        new SelectItem(-1, strings['survey.settings.question.default.question.disabled'] as String),
        new SelectItem(null, strings['question.option.terminal.inlist'] as String),
        *activeQuestions
    ] as List<SelectItem>
  }

  List<SelectItem> getActiveQuestions() {
    survey.activeQuestions.collect { q ->
      def idx = q.activeIndex + 1
      def maxLabel = 20
      def title = q.title.replace('\r\n', ' ').replace('\n', ' ').replace('\r', ' ')
      new SelectItem(
          q.id,
          "$idx. ${title.length() <= maxLabel ? title : title[0..<maxLabel - 3] + '...'}",
          "$idx. ${q.title} ")
    } as List<SelectItem>
  }

  private void updateQuestionsGraph() {
    def tree = SurveyTreeUtil.asTree(
        survey,
        strings['survey.settings.questions.tabs.graphs.end.label'],
        strings['survey.settings.questions.tabs.graphs.end.description'],
        strings['survey.settings.questions.option.unused'],
        strings['survey.settings.question.modify.default.question.label'],
        strings['survey.settings.question.modify.default.question.description'])

    def start = new TreeNode(-2,
        strings['survey.settings.questions.tabs.graphs.start.label'],
        strings['survey.settings.questions.tabs.graphs.start.label'])
    start.edges << new TreeEdge(-1,
        strings['survey.settings.questions.tabs.graphs.start.label'],
        strings['survey.settings.questions.tabs.graphs.start.label'],
        tree)

    questionsGraph = start
  }

  String getSurveyUrl() { esdpServiceSupport.getServiceUrl(persistedSurvey) }

  void saveMessage() {
    survey.details.endSmsEnabled = endSmsType != DISABLED

    boolean validationError =
        renderViolationMessage(validator.validate(survey.details), [
            'endSmsTextSet': 'endSmsText',
        ])
    if (validationError) {
      this.errorId =
          FacesContext.currentInstance.externalContext.requestParameterMap['errorId']
      return
    }

    copyEndMessage()
    copyCoupons()

    surveyRepository.update(persistedSurvey)
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
        persistedSurvey.patterns.each { it.active = false }

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
        persistedSurvey.patterns.each { it.active = false }
      }
    }
  }

  void saveSettings() {
    survey.client = userRepository.load(newSurveyClientId)

    boolean validationError = renderViolationMessage(
        validator.validate(survey),
        [
            'details.title'          : 'newSurveyTitle',
            'startDate'              : 'newSurveyStartDate',
            'endDate'                : 'newSurveyEndDate',
            'endDateAfterStartDate'  : 'newSurveyEndDate',
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

    try {
      esdpService.update(getCurrentUser(), persistedSurvey)
      surveyRepository.update(persistedSurvey)

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
      errorId = 'deleteDialog'

      return null
    }

    surveyService.delete survey

    return 'SURVEY_LIST'
  }

  void moveUp(int questionId) {
    persistedSurvey.moveUp(questionRepository.load(questionId))
    surveyRepository.update(persistedSurvey)

    persistedSurvey = surveyRepository.load(surveyId)
  }

  void moveDown(int questionId) {
    persistedSurvey.moveDown(questionRepository.load(questionId))
    surveyRepository.update(persistedSurvey)

    persistedSurvey = surveyRepository.load(surveyId)
  }

  void beforeDeleteQuestion(int questionId) {
    def question = questionRepository.load(questionId)
    this.questionId = questionId

    def refs = surveyService.getReferencesTo(question)
    def defaultRefs = surveyService.getDefaultReferencesTo(question)

    defaultRefs.each { Question q ->
      if (!refs.contains(q)) refs << q
    }

    if (refs.empty) {
      questionDeletePrompt = strings['survey.settings.questions.delete.prompt']

    } else if (refs.size() == 1) {
      questionDeletePrompt = MessageFormat.format(
          strings['survey.settings.questions.delete.prompt.with.references.single'] as String,
          refs.first().activeIndex + 1 as String)
    } else {
      questionDeletePrompt = MessageFormat.format(
          strings['survey.settings.questions.delete.prompt.with.references.plural'] as String,
          refs.collect { Question q -> q.activeIndex + 1 }.join(', '))
    }

    errorId = 'questionDeleteDialog'
  }

  void deleteQuestion() {
    int questionId = getParamValue('questionId').asInteger()

    surveyService.deleteQuestion(questionRepository.load(questionId))
    persistedSurvey = surveyRepository.load(surveyId)
    updateQuestionsGraph()
  }

  String modifyQuestion(Integer questionId) {
    this.questionId = questionId

    if (questionId) {
      question = questionRepository.load(questionId)
      defaultQuestionId = question.enabledDefaultAnswer ? question.defaultQuestion?.id : -1

      questionOptions = new DynamicTableModel()
      question.options
          .findAll { it.active }
          .each { QuestionOption it ->
        def row = new DynamicTableRow() {
          {
            setValue 'answer', it.answer
            setValue 'nextQuestion', it.nextQuestion ? it.nextQuestion.id : -1 as String
            setValue 'id', it.id
          }
        }
        questionOptions.addRow(row)
      }

    } else {
      question = new Question()
      defaultQuestionId = -1
      questionOptions = new DynamicTableModel()
    }

    errorId = 'questionModificationDialog'

    return null
  }

  void saveQuestion() {
    def persistedQuestion = questionId ?
        questionRepository.load(questionId) : new Question(survey: persistedSurvey)

    updateQuestionModel(persistedQuestion)

    Set<ConstraintViolation> violations = validator.validate(persistedQuestion)
    if (!violations) {
      def segmentationFailures = checkSegmentation(persistedQuestion)
      if (segmentationFailures) {
        violations = new HashSet<>(violations)
        violations.addAll(segmentationFailures)
      }
    }

    final List<String> fieldOrder = [
        'title',
        'validOptions',
        (0..<persistedQuestion.options.size()).collect { "options[$it].answer".toString() }
    ].flatten() as List<String>

    final boolean validationError = renderViolationMessage(
        violations, getPropertyMap(persistedQuestion), fieldOrder)
    if (validationError) {
      errorId =
          FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
      return
    }

    if (!questionId) {
      persistedSurvey.questions.add(persistedQuestion)
      surveyRepository.update(persistedSurvey)

    } else {
      questionRepository.saveOrUpdate(persistedQuestion)
    }

    updateQuestionsGraph()
    goToSurvey(surveyId)
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  private Collection<ConstraintViolation<Question>> checkSegmentation(
      Question persistedQuestion) {

    final Integer failedOption = MobilizerSegmentation.checkOptionLength(persistedQuestion)
    if (failedOption == null) {
      return []

    } else if (failedOption < 0) {
      return [new SimpleConstraintViolation<>(
          "title",
          strings['mobi.eyeline.constraints.size.max'])]

    } else {
      return [new SimpleConstraintViolation<>(
          "options[${failedOption}].answer",
          strings['mobi.eyeline.constraints.size.max'])]
    }
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  private Map<String, String> getPropertyMap(Question q) {
    (0..<q.options.size()).collectEntries { i ->
      ["options[$i].answer".toString(), "questionOptions_${i}_answer".toString()]
    }
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
    def getId = { DynamicTableRow row -> row.getValue('id') as String }
    def getAnswer = { DynamicTableRow row -> row.getValue('answer') as String }
    def getNextQuestion = { DynamicTableRow row ->
      int nextId = row.getValue('nextQuestion') as Integer
      nextId == -1 ? null : questionRepository.get(nextId) as Question
    }
    def index = { DynamicTableRow row -> questionOptions.rows.indexOf(row) }

    persistedQuestion.title = question.title
    persistedQuestion.enabledDefaultAnswer = (defaultQuestionId != -1)
    if (persistedQuestion.enabledDefaultAnswer) {
      persistedQuestion.defaultQuestion = defaultQuestionId ? questionRepository.get(defaultQuestionId) : null
    } else {
      persistedQuestion.defaultQuestion = null
    }

    def handleRemoved = {
      def retainedOptionIds = questionOptions.rows
          .collect { DynamicTableRow row -> getId(row) }
          .findAll { String id -> !id.empty }
          .collect { String id -> id.toInteger() }

      persistedQuestion.options
          .findAll { opt -> !(opt.id in retainedOptionIds) }
          .each { opt -> opt.active = false }
    }

    def handleUpdated = {
      persistedQuestion.activeOptions.each { option ->
        questionOptions.rows
            .findAll { DynamicTableRow row -> !getId(row).empty }
            .find { DynamicTableRow row -> getId(row).toInteger() == option.id }
            .each { DynamicTableRow row ->
          option.answer = getAnswer(row)
          option.nextQuestion = getNextQuestion(row)
          option.moveTo index(row)
        }
      }
    }

    def handleAdded = {
      questionOptions.rows
          .findAll { DynamicTableRow row -> getId(row).empty }
          .each { DynamicTableRow row ->
        def option = new QuestionOption(
            question: persistedQuestion,
            answer: getAnswer(row),
            nextQuestion: getNextQuestion(row))
        persistedQuestion.options.add option
        option.moveTo index(row)
      }
    }

    handleRemoved()
    handleAdded()
    handleUpdated()
  }

  List<SelectItem> getAvailableAccessNumbers() {
    final List<SelectItem> items = [
        new SelectItem(null, BaseController.strings['no.access.number'] as String)
    ]

    def available = { AccessNumber number ->
      !number.surveyStats || (survey.statistics.accessNumber && (number.id == survey.statistics.accessNumber.id))
    }

    accessNumberRepository.list().each { number ->
      items << new SelectItem(number.id, number.number, number.number, !available(number))
    }

    return items
  }

  static void goToSurvey(int surveyId) {
    FacesContext.currentInstance.externalContext
        .redirect("/pages/surveys/settings.faces?id=${surveyId}")
  }

  static enum EndSmsType {
    DISABLED,
    SMS,
    COUPON
  }
}
