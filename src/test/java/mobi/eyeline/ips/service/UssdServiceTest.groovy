package mobi.eyeline.ips.service

import mobi.eyeline.ips.messages.MissingParameterException
import mobi.eyeline.ips.messages.UssdOption
import mobi.eyeline.ips.messages.UssdResponseModel
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.properties.FailingMockConfig
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RepositoryMock
import mobi.eyeline.ips.utils.SurveyBuilder

import static mobi.eyeline.ips.messages.AnswerOption.PARAM_ANSWER_ID
import static mobi.eyeline.ips.messages.AnswerOption.PARAM_QUESTION_ID
import static mobi.eyeline.ips.messages.UssdOption.PARAM_BAD_COMMAND
import static mobi.eyeline.ips.messages.UssdOption.PARAM_MESSAGE_TYPE
import static mobi.eyeline.ips.messages.UssdOption.PARAM_MSISDN_DEPRECATED
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SKIP_VALIDATION
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SURVEY_ID
import static mobi.eyeline.ips.messages.UssdOption.UssdOptionType.ANSWER
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.instanceOf

@SuppressWarnings("UnnecessaryQualifiedReference")
@Mixin([RepositoryMock, UssdServiceUtils])
class UssdServiceTest extends DbTestCase {

  Config config

  // Dependencies

  SurveyService surveyService
  PushService pushService
  CouponService couponService

  UssdService ussdService

  final String msisdn = '123'
  final String sid = '1'

  void setUp() {
    super.setUp()

    // Configuration
    config = new FailingMockConfig() {
      String getBaseSurveyUrl() { 'http://localhost:39932' }
      int getSadsMaxSessions() { 2 }
    }

    initRepository(db)

    // Dependencies
    surveyService = new SurveyService(
        surveyRepository,
        questionRepository,
        extLinkPageRepository,
        questionOptionRepository,
        surveyInvitationRepository,
        invitationDeliveryRepository,
        accessNumberRepository)

    pushService = new PushService(config, new EsdpServiceSupport(null) {
      @Override
      String getServiceUrl(Survey survey) { "http://sads?push$survey.id" }
    })
    couponService = new CouponService(surveyPatternRepository, new MockMailService())

    ussdService = new UssdService(
        config,
        surveyService,
        pushService,
        couponService,
        surveyRepository,
        respondentRepository,
        answerRepository,
        questionRepository,
        questionOptionRepository,
        extLinkPageRepository)
  }

  def survey = { surveyRepository.load(sid.toInteger()) }

  def answer(option) {
    def props = [:]
    props.putAll option.properties
    props[UssdOption.PARAM_MSISDN_DEPRECATED] = msisdn
    request(props)
  }

  def textAnswer(option, text) {
    def props = [:]
    props.putAll option.properties
    props.put PARAM_BAD_COMMAND, text
    props[UssdOption.PARAM_MSISDN_DEPRECATED] = msisdn
    request(props)
  }

  void testNullSurveyUrl() {
    shouldFail(NullPointerException) { ussdService.getSurveyUrl(null) }
  }

  void testStartPageMissingSurvey() {
    request([
        (PARAM_SURVEY_ID): '100500', (PARAM_MSISDN_DEPRECATED): '123'
    ]).with {
      assertTrue it.options.empty
      assertThat it, instanceOf(UssdResponseModel.NoSurveyResponseModel)
    }
  }

  void testStartPageRequiredParam() {
    shouldFail(MissingParameterException) {
      request([(PARAM_SURVEY_ID): '100500'])
    }

    shouldFail(MissingParameterException) {
      request([(PARAM_MSISDN_DEPRECATED): '123'])
    }
  }

  void testStartPageUnknownRequest() {
    request([
        (PARAM_MSISDN_DEPRECATED)      : '123',
        (PARAM_MESSAGE_TYPE): 'foo'
    ]).with {
      assertThat it, instanceOf(UssdResponseModel.ErrorResponseModel)
    }
  }

  void testQuestionPageUnknownRequest1() {
    request([
        (PARAM_MSISDN_DEPRECATED)      : '123',
        (PARAM_MESSAGE_TYPE): ANSWER,
        (PARAM_QUESTION_ID) : 1,
        (PARAM_ANSWER_ID)   : 1,
        (PARAM_SURVEY_ID)   : 1   // Missing
    ]).with {
      assertThat it, instanceOf(UssdResponseModel.NoSurveyResponseModel)
    }
  }

  void testQuestionPageUnknownRequest2() {
    shouldFail(MissingParameterException) {
      request([
          (PARAM_MSISDN_DEPRECATED)      : '123',
          (PARAM_MESSAGE_TYPE): ANSWER,
          (PARAM_QUESTION_ID) : 1,
          (PARAM_ANSWER_ID)   : 1,
      ])
    }
  }

  void testQuestionPageUnknownRequest3() {
    shouldFail(MissingParameterException) {
      request([
          (PARAM_MSISDN_DEPRECATED)      : '123',
          (PARAM_MESSAGE_TYPE): "$ANSWER",
          (PARAM_ANSWER_ID)   : 1,
          (PARAM_SURVEY_ID)   : 1
      ])
    }
  }

  void testQuestionPageUnknownRequest4() {
    shouldFail(MissingParameterException) {
      request([
          (PARAM_MSISDN_DEPRECATED)      : '123',
          (PARAM_MESSAGE_TYPE): "$ANSWER",
          (PARAM_QUESTION_ID) : 1,
          (PARAM_SURVEY_ID)   : 1
      ])
    }
  }

  void createTestSurvey() {
    def survey =
        new Survey(startDate: new Date() - 2, endDate: new Date() + 2)
    survey.details = new SurveyDetails(survey: survey, title: 'Foo', endText: 'End text')
    surveyRepository.save survey

    def q1 = new Question(survey: survey, title: 'First one', enabledDefaultAnswer: true)
    def q2 = new Question(survey: survey, title: 'Second one')
    def q3 = new Question(survey: survey, title: 'With options')
    def q4 = new Question(survey: survey, title: 'Fourth one', enabledDefaultAnswer: true, defaultPage: q3)
    def q5 = new Question(survey: survey, title: 'Fifth one', enabledDefaultAnswer: true, defaultPage: q1)

    q1.with {
      enabledDefaultAnswer: true
      defaultQuestion:
      q3
    }

    q1.options << new QuestionOption(answer: 'O1', question: q1, nextPage: q2)
    q2.options << new QuestionOption(answer: 'O2', question: q2, nextPage: q3)
    q3.options << new QuestionOption(answer: 'O3', question: q3, nextPage: q4)
    q4.options << new QuestionOption(answer: 'O4', question: q4, nextPage: q5)
    q5.options << new QuestionOption(answer: 'O5', question: q5)

    survey.pages.addAll([q1, q2, q3, q4, q5])

    surveyRepository.update(survey)

    q3 = questionRepository.load(3)

    [
        new QuestionOption(answer: 'Option 1', question: q3),
        new QuestionOption(answer: 'Option 2', question: q3),
        new QuestionOption(answer: 'Option 3', question: q3)
    ].each { q3.options.add it }

    questionRepository.update(q3)
  }

  void testFullCycle() {
    createTestSurvey()

    def respondent = { respondentRepository.load(1) }

    //
    // Start page.
    //

    def page1 = request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ]).with {
      assertEquals 'First one', text
      assertThat options, hasSize(1)

      options.first().with {
        assertEquals(['O1', 1, 1, 1], [text, answerId, questionId, surveyId])
      }

      it
    }

    assertEquals 1, respondent().survey.id
    assertEquals 1, questionRepository.load(1).sentCount
    survey().with {
      assertEquals 0, respondentRepository.countFinishedBySurvey(it, false, null)
      assertEquals 1, respondentRepository.countBySurvey(it, null, null, false, null)
    }

    //
    // Answer #1.
    //

    def page2 = answer page1.options.first()
    page2.with {
      assertEquals 'Second one', text
      assertThat options, hasSize(1)

      options.first().with {
        assertEquals(['O2', 2, 2, 1], [text, answerId, questionId, surveyId])
      }
    }

    assertEquals 1, respondent().answersCount
    assertEquals 1, questionRepository.load(2).sentCount

    //
    // Answer #2.
    //

    def page3 = answer page2.options.first()
    page3.with {
      assertEquals 'With options', text
      assertThat options, hasSize(4)
    }

    assertEquals 2, respondent().answersCount
    survey().with {
      assertEquals 0, respondentRepository.countFinishedBySurvey(it, false, null)
      assertEquals 1, respondentRepository.countBySurvey(it, null, null, false, null)
    }

    //
    // Answer #3.
    //

    def page4 = answer page3.options.first()
    page4.with {
      assertEquals 'Fourth one', text
      assertThat options, hasSize(1)

      options.first().with {
        assertEquals(['O4', 4, 4, 1], [text, answerId, questionId, surveyId])
      }
    }

    assertEquals 3, respondent().answersCount
    assertEquals 1, questionRepository.load(4).sentCount



    def page5 = answer page4.options.first()
    page5.with {
      assertEquals 'Fifth one', text
      assertThat options, hasSize(1)

      options.first().with {
        assertEquals(['O5', 5, 5, 1], [text, answerId, questionId, surveyId])
      }
    }

    assertEquals 4, respondent().answersCount
    assertEquals 1, questionRepository.load(5).sentCount


    def page6 = textAnswer page5.options.first(), 'some text'
    page6.with {
      assertEquals 'First one', text
      assertThat options, hasSize(1)

      options.first().with {
        assertEquals(['O1', 1, 1, 1], [text, answerId, questionId, surveyId])
      }
    }

    assertEquals 5, respondent().answersCount
    assertEquals 1, questionRepository.load(5).sentCount


    def page7 = textAnswer page6.options.first(), 'some text'
    page7.with {
      assertEquals survey().details.endText, text
      assertThat it, instanceOf(UssdResponseModel.TextUssdResponseModel)
    }

    // Check final stats.

    respondent().with {
      assertEquals 6, answersCount
      assertTrue finished
    }

    survey().with {
      assertEquals 1, respondentRepository.countFinishedBySurvey(it, false, null)
      assertEquals 1, respondentRepository.countBySurvey(it, null, null, false, null)
    }
  }

  void testSkipValidation1() {
    createTestSurvey()

    survey().with {
      startDate = new Date().plus(100)
      endDate = startDate.plus(5)
      surveyRepository.update it
    }

    request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ]).with {
      assertThat it, instanceOf(UssdResponseModel.NoSurveyResponseModel)
    }

    request([
        (PARAM_MSISDN_DEPRECATED)         : msisdn,
        (PARAM_SURVEY_ID)      : sid,
        (PARAM_SKIP_VALIDATION): true
    ]).with {
      assertThat it.options, hasSize(1)
    }
  }

  void testNoQuestions() {
    def survey =
        new Survey(startDate: new Date().minus(2), endDate: new Date().plus(2))
    survey.details = new SurveyDetails(survey: survey, title: 'Foo', endText: 'End text')
    surveyRepository.save survey

    request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ]).with {
      assertEquals survey.details.endText, it.text
    }
  }

  void testTerminal() {
    createTestSurvey()
    survey().questions[0].options[0].with {
      nextPage = null
      questionOptionRepository.update it
    }

    def page1 = request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ])

    def page2 = answer page1.options.first()
    page2.with {
      assertThat it, instanceOf(UssdResponseModel.TextUssdResponseModel)
      assertEquals survey().details.endText, text
    }

    assertTrue respondentRepository.load(1).finished
  }

  void testRestore() {
    createTestSurvey()

    def page1 = request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ])
    answer page1.options.first()

    // Accessing start page again.
    def page1Again = request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ])

    assertEquals 0, respondentRepository.countFinishedBySurvey(survey(), false, null)
    assertNull 'Stats should be cleared',
        answerRepository.getLast(survey(), respondentRepository.load(1))

    //noinspection GroovyAccessibility
    assertThat 'Stats should be cleared',
        answerRepository.list(respondentRepository.load(1)), hasSize(0)

    def surveySessions = answerRepository.list(
        survey(),
        survey().startDate,
        survey().endDate,
        null, null,
        null,
        false,
        Integer.MAX_VALUE,
        0)
    assertThat surveySessions, hasSize(1)
    assertThat 'Survey session should be cleared', surveySessions.first().answers, hasSize(0)

    page1Again.with {
      assertEquals 'First one', text
      assertEquals 'type=ANSWER&survey_id=1&skip_validation=false&questionId=1&answerId=1',
          options[0].uri
      assertThat options, hasSize(1)
      assertEquals 'O1', options[0].text
    }
  }

  void testRestart() {
    def respondent = { respondentRepository.load(1) }

    testFullCycle()

    assertEquals 'First one',
        answerRepository.getLast(survey(), respondent()).question.title
    assertEquals([2, 1, 1, 1, 1],
        questionRepository.list().sort { it.id }.collect { it.sentCount })

    respondent().with {
      assertEquals 6, it.answersCount
      assertTrue it.finished
    }
    assertThat answerRepository.list(), hasSize(6)

    // Load the landing page again.
    // We expect the same output as the first time, statistics should be reset.

    request([
        (PARAM_MSISDN_DEPRECATED)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ]).with {
      assertEquals 'First one', it.text
    }

    assertNull answerRepository.getLast(survey(), respondent())
    assertEquals([1, 0, 0, 0, 0],
        questionRepository.list().sort { it.id }.collect { it.sentCount })
    respondent().with {
      assertEquals 0, it.answersCount
      assertFalse it.finished
    }
  }

  void testBadCommand() {
    createTestSurvey()
    //noinspection GroovyUnusedAssignment
    def page1 = request([
        (PARAM_SURVEY_ID): 1,
        (PARAM_MSISDN_DEPRECATED)   : '79131234567',
    ]).with {
      assertEquals 'First one', it.text
    }

    request([
        (PARAM_SURVEY_ID)   : 1,
        (PARAM_MESSAGE_TYPE): ANSWER,
        (PARAM_QUESTION_ID) : 1,
        (PARAM_ANSWER_ID)   : 1,
        (PARAM_MSISDN_DEPRECATED)      : '79131234567',
    ]).with {
      assertEquals 'Second one', it.text
    }

    request([
        (PARAM_SURVEY_ID)   : 1,
        (PARAM_MESSAGE_TYPE): ANSWER,
        (PARAM_QUESTION_ID) : 1,
        (PARAM_ANSWER_ID)   : 1,
        (PARAM_MSISDN_DEPRECATED)      : '79131234567',
        (PARAM_BAD_COMMAND) : null
    ]).with {
      assertEquals 'Second one', it.text
    }

  }

  void testRedirect() {

    final s = SurveyBuilder.survey(id: 1, startDate: new Date(), endDate: new Date()) {
      details(title: 'Foo')
      pages {
        extLink(serviceName: 'bar', serviceUrl: 'http://foo.bar')
        question(title: 'Q1') {
          option(answer: 'O1', nextPage: ref(serviceName: 'bar'))
        }
      }
    }

    surveyRepository.save s

    //noinspection GroovyUnusedAssignment
    def page1 = request([
        (PARAM_SURVEY_ID)         : 1,
        (PARAM_MSISDN_DEPRECATED) : '79131234567',
        (PARAM_SKIP_VALIDATION)   : true,
    ]).with {
      assertEquals 'Q1', it.text
    }

    request([
        (PARAM_SURVEY_ID)         : 1,
        (PARAM_MESSAGE_TYPE)      : ANSWER,
        (PARAM_QUESTION_ID)       : 1,
        (PARAM_ANSWER_ID)         : 1,
        (PARAM_MSISDN_DEPRECATED) : '79131234567',
        (PARAM_SKIP_VALIDATION)   : true,
    ]).with { resp ->
      assertTrue resp instanceof UssdResponseModel.RedirectUssdResponseModel
      assertEquals 'http://foo.bar?abonent=79131234567&skip_validation=true', resp.redirectUrl
    }
  }
}
