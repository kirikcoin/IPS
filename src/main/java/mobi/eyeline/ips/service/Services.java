package mobi.eyeline.ips.service;


import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.repository.DB;
import mobi.eyeline.ips.repository.PermissionsRepository;
import mobi.eyeline.ips.repository.QuestionOptionRepository;
import mobi.eyeline.ips.repository.QuestionRepository;
import mobi.eyeline.ips.repository.QuestionStatsRepository;
import mobi.eyeline.ips.repository.RespondentRepository;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.SurveyStatsRepository;
import mobi.eyeline.ips.repository.UserRepository;

/**
 * Service lookup.
 */
public class Services {

    private static Services instance;

    private final DB db;

    private final UserRepository userRepository;
    private final RespondentRepository respondentRepository;
    private final QuestionStatsRepository questionStatsRepository;
    private final SurveyStatsRepository surveyStatsRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final PermissionsRepository permissionsRepository;

    private final SurveyService surveyService;
    private final TemplateService templateService;
    private final MailService mailService;
    private final UserService userService;

    public Services(Config config) {
        db = new DB(config.getDatabaseProperties());

        userRepository = new UserRepository(db);
        respondentRepository = new RespondentRepository(db);
        questionStatsRepository = new QuestionStatsRepository(db);
        questionRepository = new QuestionRepository(db);
        surveyStatsRepository = new SurveyStatsRepository(db);
        surveyRepository = new SurveyRepository(db);
        questionOptionRepository = new QuestionOptionRepository(db);
        permissionsRepository =  new PermissionsRepository(db);

        surveyService = new SurveyService(
                db,
                surveyRepository,
                permissionsRepository,
                respondentRepository,
                surveyStatsRepository,
                questionStatsRepository);
        templateService = new TemplateService(config);

        mailService = new MailService(templateService,
                new SmtpSender(
                        config.getSmtpHost(),
                        config.getSmtpPort(),
                        config.getSmtpUsername(),
                        config.getSmtpPassword(),
                        config.getMailFrom()));

        userService = new UserService(userRepository, mailService);
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

    public QuestionStatsRepository getQuestionStatsRepository() {
        return questionStatsRepository;
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

    public PermissionsRepository getPermissionsRepository() {
        return permissionsRepository;
    }

    public SurveyService getSurveyService() {
        return surveyService;
    }

    public MailService getMailService() {
        return mailService;
    }
}