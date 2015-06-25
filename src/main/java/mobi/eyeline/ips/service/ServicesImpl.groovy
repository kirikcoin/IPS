package mobi.eyeline.ips.service

import groovy.transform.CompileStatic
import mobi.eyeline.ips.external.MadvSoapApi
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.*
import mobi.eyeline.ips.service.deliveries.DeliveryPushService
import mobi.eyeline.ips.service.deliveries.DeliveryService
import mobi.eyeline.ips.service.deliveries.NotificationService
import mobi.eyeline.ips.util.TimeSource

@SuppressWarnings("FieldCanBeLocal")
@CompileStatic
class ServicesImpl {

  final DB db

  final TimeSource timeSource

  final UserRepository userRepository
  final RespondentRepository respondentRepository
  final SurveyStatsRepository surveyStatsRepository
  final SurveyRepository surveyRepository
  final QuestionRepository questionRepository
  final ExtLinkPageRepository extLinkPageRepository
  final PageRepository pageRepository
  final QuestionOptionRepository questionOptionRepository
  final AnswerRepository answerRepository
  final SurveyInvitationRepository surveyInvitationRepository
  final InvitationDeliveryRepository invitationDeliveryRepository
  final DeliverySubscriberRepository deliverySubscriberRepository
  final SurveyPatternRepository surveyPatternRepository
  final AccessNumberRepository accessNumberRepository

  final MadvSoapApi madvSoapApi
  final MadvService madvService
  final SurveyService surveyService
  final TemplateService templateService
  final MailService mailService
  final EsdpServiceSupport esdpServiceSupport
  final PushService pushService
  final UserService userService
  final CouponService couponService
  final UssdService ussdService
  final EsdpService esdpService
  final AccessNumbersService accessNumbersService
  final MadvUpdateService madvUpdateService
  final SegmentationService segmentationService
  final ResultsExportService resultsExportService
  final DeliveryPushService deliveryPushService
  final DeliveryService deliveryService
  final NotificationService notificationService

  final CsvParseService csvParseService
  final TimeZoneService timeZoneService

  ServicesImpl(Config config) {
    db = new DB(config.databaseProperties)

    timeSource = new TimeSource()

    userRepository = new UserRepository(db)
    respondentRepository = new RespondentRepository(db)
    questionRepository = new QuestionRepository(db)
    extLinkPageRepository = new ExtLinkPageRepository(db)
    pageRepository = new PageRepository(db)
    surveyStatsRepository = new SurveyStatsRepository(db)
    surveyRepository = new SurveyRepository(db)
    questionOptionRepository = new QuestionOptionRepository(db)
    accessNumberRepository = new AccessNumberRepository(db)
    answerRepository = new AnswerRepository(db, accessNumberRepository)
    surveyInvitationRepository = new SurveyInvitationRepository(db)
    invitationDeliveryRepository = new InvitationDeliveryRepository(db)
    deliverySubscriberRepository = new DeliverySubscriberRepository(db)
    surveyPatternRepository = new SurveyPatternRepository(db)

    madvSoapApi = new MadvSoapApi(config)
    madvService = new MadvService()

    surveyService = new SurveyService(
        surveyRepository,
        questionRepository,
        extLinkPageRepository,
        questionOptionRepository,
        surveyInvitationRepository,
        invitationDeliveryRepository,
        accessNumberRepository)

    templateService = new TemplateService(config.loginUrl)
    mailService = new MailService(templateService,
        new SmtpSender(
            config.smtpHost,
            config.smtpPort,
            config.smtpUsername,
            config.smtpPassword,
            config.mailFrom))
    esdpServiceSupport = new EsdpServiceSupport(config)

    pushService = new PushService(config, esdpServiceSupport)

    userService = new UserService(userRepository, mailService)
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
        questionOptionRepository,
        extLinkPageRepository,
        accessNumberRepository)
    esdpService = new EsdpService(config, ussdService, esdpServiceSupport, surveyRepository, accessNumberRepository)
    accessNumbersService = new AccessNumbersService(accessNumberRepository, esdpService)

    madvUpdateService = new MadvUpdateService(
        config,
        madvSoapApi,
        madvService,
        surveyStatsRepository,
        surveyRepository)
    segmentationService = new SegmentationService()
    resultsExportService = new ResultsExportService(answerRepository, 100)

    deliveryPushService = new DeliveryPushService(config, esdpServiceSupport)
    deliveryService = new DeliveryService(
        timeSource,
        invitationDeliveryRepository,
        deliverySubscriberRepository,
        deliveryPushService,
        config)
    notificationService = new NotificationService(timeSource, deliverySubscriberRepository, deliveryService)

    csvParseService = new CsvParseService()
    timeZoneService = new TimeZoneService()
  }

  void start() {
    madvUpdateService.start()
    deliveryService.start()
    notificationService.start()
  }

  void shutdown() {
    notificationService.stop()
    deliveryService.stop()
    pushService.stop()
  }
}