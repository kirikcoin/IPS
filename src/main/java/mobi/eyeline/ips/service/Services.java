package mobi.eyeline.ips.service;


import mobi.eyeline.ips.external.MadvSoapApi;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.AnswerRepository;
import mobi.eyeline.ips.repository.DB;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.repository.InvitationDeliveryRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyInvitationRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import mobi.eyeline.ips.repository.UserRepository;
import mobi.eyeline.ips.service.deliveries.DeliveryPushService;
import mobi.eyeline.ips.service.deliveries.DeliveryService;

/**
 * Service lookup.
 */
@SuppressWarnings("FieldCanBeLocal")
public class Services {

    private static Services instance;

    private final DB db;

    private final UserRepository userRepository;
    private final RespondentRepository respondentRepository;
    private final SurveyStatsRepository surveyStatsRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyInvitationRepository surveyInvitationRepository;
    private final InvitationDeliveryRepository invitationDeliveryRepository;
    private final DeliverySubscriberRepository deliverySubscriberRepository;

    private final SurveyService surveyService;
    private final TemplateService templateService;
    private final MailService mailService;
    private final UserService userService;
    private final UssdService ussdService;
    private final MadvSoapApi madvSoapApi;
    private final MadvUpdateService madvUpdateService;
    private final PushService pushService;
    private final SegmentationService segmentationService;
    private final ResultsExportService resultsExportService;
    private final SkinService skinService;
    private final DeliveryPushService deliveryPushService;
    private final DeliveryService deliveryService;

    private Services(Config config) {
        db = new DB(config.getDatabaseProperties());

        userRepository = new UserRepository(db);
        respondentRepository = new RespondentRepository(db);
        questionRepository = new QuestionRepository(db);
        surveyStatsRepository = new SurveyStatsRepository(db);
        surveyRepository = new SurveyRepository(db);
        questionOptionRepository = new QuestionOptionRepository(db);
        answerRepository = new AnswerRepository(db);
        surveyInvitationRepository = new SurveyInvitationRepository(db);
        invitationDeliveryRepository = new InvitationDeliveryRepository(db);
        deliverySubscriberRepository = new DeliverySubscriberRepository(db);

        surveyService = new SurveyService(
                surveyRepository,
                surveyInvitationRepository);
        templateService = new TemplateService(config);

        mailService = new MailService(templateService,
                new SmtpSender(
                        config.getSmtpHost(),
                        config.getSmtpPort(),
                        config.getSmtpUsername(),
                        config.getSmtpPassword(),
                        config.getMailFrom()));

        userService = new UserService(userRepository, mailService);
        ussdService = new UssdService(
                config,
                surveyService,
                surveyRepository,
                respondentRepository,
                answerRepository,
                questionRepository,
                questionOptionRepository);
        madvSoapApi = new MadvSoapApi();

        madvUpdateService = new MadvUpdateService(
                config,
                madvSoapApi,
                surveyStatsRepository,
                surveyRepository);
        pushService = new PushService(config);
        segmentationService = new SegmentationService();
        resultsExportService = new ResultsExportService(answerRepository, 100);
        skinService = new SkinService(config.getSkinDefault());

        deliveryPushService = new DeliveryPushService(config);
        deliveryService = new DeliveryService(
                invitationDeliveryRepository,
                deliverySubscriberRepository,
                deliveryPushService,
                config
        );
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

    public SurveyService getSurveyService() {
        return surveyService;
    }

    public MailService getMailService() {
        return mailService;
    }

    public UserService getUserService() {
        return userService;
    }

    public UssdService getUssdService() {
        return ussdService;
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

    public SkinService getSkinService() {
        return skinService;
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
}