package mobi.eyeline.ips.service;


import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.AccessNumberRepository;
import mobi.eyeline.ips.repository.AnswerRepository;
import mobi.eyeline.ips.repository.DB;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.repository.ExtLinkPageRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.repository.PageRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyInvitationRepository;
import mobi.eyeline.ips.repository.SurveyPatternRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import mobi.eyeline.ips.repository.UserRepository;
import mobi.eyeline.ips.service.deliveries.DeliveryPushService;
import mobi.eyeline.ips.service.deliveries.DeliveryService;
import mobi.eyeline.ips.service.deliveries.NotificationService;
import mobi.eyeline.ips.util.TimeSource;

/**
 * Service lookup.
 */
@SuppressWarnings("FieldCanBeLocal")
public class Services {

  private static Services instance;

  private final DB db;

  private final TimeSource timeSource;

  private final UserRepository userRepository;
  private final RespondentRepository respondentRepository;
  private final SurveyStatsRepository surveyStatsRepository;
  private final SurveyRepository surveyRepository;
  private final QuestionRepository questionRepository;
  private final ExtLinkPageRepository extLinkPageRepository;
  private final PageRepository pageRepository;
  private final QuestionOptionRepository questionOptionRepository;
  private final AnswerRepository answerRepository;
  private final SurveyInvitationRepository surveyInvitationRepository;
  private final InvitationDeliveryRepository invitationDeliveryRepository;
  private final DeliverySubscriberRepository deliverySubscriberRepository;
  private final SurveyPatternRepository surveyPatternRepository;
  private final AccessNumberRepository accessNumberRepository;

  private final MadvSoapApi madvSoapApi;
  private final MadvService madvService;
  private final SurveyService surveyService;
  private final TemplateService templateService;
  private final MailService mailService;
  private final EsdpServiceSupport esdpServiceSupport;
  private final PushService pushService;
  private final UserService userService;
  private final CouponService couponService;
  private final UssdService ussdService;
  private final EsdpService esdpService;
  private final MadvUpdateService madvUpdateService;
  private final SegmentationService segmentationService;
  private final ResultsExportService resultsExportService;
  private final DeliveryPushService deliveryPushService;
  private final DeliveryService deliveryService;
  private final NotificationService notificationService;

  private final CsvParseService csvParseService;
  private final TimeZoneService timeZoneService;

  private Services(Config config) {
    db = new DB(config.getDatabaseProperties());

    timeSource = new TimeSource();

    userRepository = new UserRepository(db);
    respondentRepository = new RespondentRepository(db);
    questionRepository = new QuestionRepository(db);
    extLinkPageRepository = new ExtLinkPageRepository(db);
    pageRepository = new PageRepository(db);
    surveyStatsRepository = new SurveyStatsRepository(db);
    surveyRepository = new SurveyRepository(db);
    questionOptionRepository = new QuestionOptionRepository(db);
    answerRepository = new AnswerRepository(db);
    surveyInvitationRepository = new SurveyInvitationRepository(db);
    invitationDeliveryRepository = new InvitationDeliveryRepository(db);
    deliverySubscriberRepository = new DeliverySubscriberRepository(db);
    surveyPatternRepository = new SurveyPatternRepository(db);
    accessNumberRepository = new AccessNumberRepository(db);

    madvSoapApi = new MadvSoapApi(config);
    madvService = new MadvService();

    surveyService = new SurveyService(
        surveyRepository,
        questionRepository,
        extLinkPageRepository,
        questionOptionRepository,
        surveyInvitationRepository,
        invitationDeliveryRepository);

    templateService = new TemplateService(config.getLoginUrl());
    mailService = new MailService(templateService,
        new SmtpSender(
            config.getSmtpHost(),
            config.getSmtpPort(),
            config.getSmtpUsername(),
            config.getSmtpPassword(),
            config.getMailFrom()));
    esdpServiceSupport = new EsdpServiceSupport(config);

    pushService = new PushService(config, esdpServiceSupport);

    userService = new UserService(userRepository, mailService);
    couponService = new CouponService(surveyPatternRepository, mailService);
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
        extLinkPageRepository);
    esdpService = new EsdpService(config, ussdService, esdpServiceSupport);

    madvUpdateService = new MadvUpdateService(
        config,
        madvSoapApi,
        madvService,
        surveyStatsRepository,
        surveyRepository);
    segmentationService = new SegmentationService();
    resultsExportService = new ResultsExportService(answerRepository, 100);

    deliveryPushService = new DeliveryPushService(config, esdpServiceSupport);
    deliveryService = new DeliveryService(
        timeSource,
        invitationDeliveryRepository,
        deliverySubscriberRepository,
        deliveryPushService,
        config);
    notificationService = new NotificationService(timeSource, deliverySubscriberRepository, deliveryService);

    csvParseService = new CsvParseService();
    timeZoneService = new TimeZoneService();
  }

  public static synchronized void initialize(Config properties) {
    if (instance != null) {
      throw new AssertionError("Instance is already initialized");
    }

    instance = new Services(properties);
  }

  public static Services instance() {
    if (instance == null) {
      throw new AssertionError("Instance is not initialized");
    }

    return instance;
  }

  public DB getDb() {
    return db;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public RespondentRepository getRespondentRepository() {
    return respondentRepository;
  }

  public SurveyStatsRepository getSurveyStatsRepository() {
    return surveyStatsRepository;
  }

  public SurveyRepository getSurveyRepository() {
    return surveyRepository;
  }

  public QuestionRepository getQuestionRepository() {
    return questionRepository;
  }

  public ExtLinkPageRepository getExtLinkPageRepository() {
    return extLinkPageRepository;
  }

  public PageRepository getPageRepository() {
    return pageRepository;
  }

  public QuestionOptionRepository getQuestionOptionRepository() {
    return questionOptionRepository;
  }

  public AnswerRepository getAnswerRepository() {
    return answerRepository;
  }

  public SurveyInvitationRepository getSurveyInvitationRepository() {
    return surveyInvitationRepository;
  }

  public InvitationDeliveryRepository getInvitationDeliveryRepository() {
    return invitationDeliveryRepository;
  }

  public DeliverySubscriberRepository getDeliverySubscriberRepository() {
    return deliverySubscriberRepository;
  }

  public SurveyPatternRepository getSurveyPatternRepository() {
    return surveyPatternRepository;
  }

  public AccessNumberRepository getAccessNumberRepository() {
    return accessNumberRepository;
  }

  public MadvSoapApi getMadvSoapApi() {
    return madvSoapApi;
  }

  public MadvService getMadvService() {
    return madvService;
  }

  public SurveyService getSurveyService() {
    return surveyService;
  }

  public MailService getMailService() {
    return mailService;
  }

  public EsdpServiceSupport getEsdpServiceSupport() {
    return esdpServiceSupport;
  }

  public UserService getUserService() {
    return userService;
  }

  public CouponService getCouponService() {
    return couponService;
  }

  public UssdService getUssdService() {
    return ussdService;
  }

  public EsdpService getEsdpService() {
    return esdpService;
  }

  public MadvUpdateService getMadvUpdateService() {
    return madvUpdateService;
  }

  public PushService getPushService() {
    return pushService;
  }

  public SegmentationService getSegmentationService() {
    return segmentationService;
  }

  public ResultsExportService getResultsExportService() {
    return resultsExportService;
  }

  public DeliveryService getDeliveryService() {
    return deliveryService;
  }

  public NotificationService getNotificationService() {
    return notificationService;
  }

  public CsvParseService getCsvParseService() {
    return csvParseService;
  }

  public TimeZoneService getTimeZoneService() {
    return timeZoneService;
  }
}