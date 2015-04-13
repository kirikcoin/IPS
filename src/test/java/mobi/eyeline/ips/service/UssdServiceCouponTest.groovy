package mobi.eyeline.ips.service

import mobi.eyeline.ips.messages.UssdResponseModel
import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.model.SurveyPattern
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.properties.DefaultMockConfig
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RepositoryMock

import static mobi.eyeline.ips.messages.UssdOption.PARAM_MSISDN
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SURVEY_ID
import static mobi.eyeline.ips.model.SurveyPattern.Mode.DIGITS
import static mobi.eyeline.ips.model.SurveyPattern.Mode.DIGITS_AND_LATIN
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.instanceOf

@SuppressWarnings("UnnecessaryQualifiedReference")
@Mixin([RepositoryMock, UssdServiceUtils])
class UssdServiceCouponTest extends DbTestCase {

  Config config

  // Dependencies

  SurveyService surveyService
  PushService pushService
  MockMailService mailService
  CouponService couponService

  UssdService ussdService

  final String msisdn = '123'
  final String sid = '1'

  void setUp() {
    super.setUp()

    initRepository(db)

    // Configuration
    config = new DefaultMockConfig()

    // Dependencies
    surveyService = new SurveyService(
        surveyRepository, questionRepository, questionOptionRepository,
        surveyInvitationRepository,
        invitationDeliveryRepository)

    pushService = new PushService(config, new EsdpServiceSupport(null) {
      @Override
      String getServiceUrl(Survey survey) { "http://sads/push?id=$survey.id" }
    }) {
      def textSent = []

      @Override
      void scheduleSendSms(Survey survey, String from, String text, String msisdn) {
        textSent << text
      }
    }
    mailService = new MockMailService()
    couponService = new CouponService(surveyPatternRepository, mailService)

    ussdService = new UssdService(
        config,
        surveyService,
        pushService,
        couponService,
        surveyRepository,
        respondentRepository,
        answerRepository,
        questionRepository,
        questionOptionRepository)
  }

  def survey = { surveyRepository.load(sid.toInteger()) }

  void createTestSurvey() {
    def survey = new Survey(startDate: new Date() - 2, endDate: new Date() + 2)
    survey.details = new SurveyDetails(
        survey: survey,
        title: 'Foo',
        endText: 'End text',
        endSmsEnabled: true,
        endSmsText: '[coupon]')
    surveyRepository.save survey

    def pattern =
        new SurveyPattern(survey: survey, length: 4, mode: DIGITS_AND_LATIN, active: true)
    surveyPatternRepository.save(pattern)
  }

  void test1() {
    createTestSurvey()

    // Same respondent gets single coupon.

    request([
        (PARAM_MSISDN)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ]).with {
      assertEquals survey().details.endText, text
      assertThat it, instanceOf(UssdResponseModel.TextUssdResponseModel)

      it
    }
    assertEquals(['CNZZ'], pushService.textSent)

    request([
        (PARAM_MSISDN)   : msisdn,
        (PARAM_SURVEY_ID): sid
    ])

    assertEquals(['CNZZ', 'CNZZ'], pushService.textSent)
  }

  void test2() {
    createTestSurvey()

    // Distinct respondents get different coupons.

    request([
        (PARAM_MSISDN)   : '123',
        (PARAM_SURVEY_ID): sid
    ])
    assertEquals(['CNZZ'], pushService.textSent)

    request([
        (PARAM_MSISDN)   : '456',
        (PARAM_SURVEY_ID): sid
    ])

    assertEquals(['CNZZ', 'CT6O'], pushService.textSent)
  }

  void test3() {
    createTestSurvey()

    // Check if multiple generators present.

    def survey = survey()
    survey.patterns.each { p -> p.active = false }
    surveyRepository.update survey

    survey.patterns <<
        new SurveyPattern(survey: survey, length: 6, mode: DIGITS, active: true)
    surveyRepository.update survey

    request([
        (PARAM_MSISDN)   : '123',
        (PARAM_SURVEY_ID): sid
    ])
    assertEquals(['399999'], pushService.textSent)

    request([
        (PARAM_MSISDN)   : '456',
        (PARAM_SURVEY_ID): sid
    ])

    assertEquals(['399999', '401440'], pushService.textSent)
  }

  void test4() {
    createTestSurvey()

    // Check if multiple generators present

    def survey = survey()
    survey.patterns.clear()
    surveyRepository.update survey

    User manager = new User(login: 'jdoe',
        password: '123'.pw(),
        email: 'username@example.com',
        fullName: 'John Doe',
        role: Role.MANAGER,
        uiProfile: new UiProfile())

    User client = new User(
        id: 1,
        password: '123',
        login: 'client',
        fullName: 'client',
        email: 'client@example.com',
        role: Role.CLIENT,
        manager: manager)

    userRepository.save manager
    userRepository.save client
    survey.client = client

    survey.patterns <<
        new SurveyPattern(
            survey: survey,
            length: 2,
            mode: DIGITS,
            position: 80,
            active: true)
    surveyRepository.update survey

    // Respondent-1

    request([
        (PARAM_MSISDN)   : '123',
        (PARAM_SURVEY_ID): sid
    ])
    assertEquals(['59'], pushService.textSent)

    assertThat mailService.calledMethods, hasSize(1)
    assertEquals 'sendCouponRemaining', mailService.calledMethods[0][0]

    // Respondent-2

    request([
        (PARAM_MSISDN)   : '456',
        (PARAM_SURVEY_ID): sid
    ])
    assertEquals(['59', '30'], pushService.textSent)

    assertThat mailService.calledMethods, hasSize(2)
    assertEquals 'sendCouponRemaining', mailService.calledMethods[1][0]
  }
}
