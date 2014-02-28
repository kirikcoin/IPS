package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.messages.MessageHandler
import mobi.eyeline.ips.messages.MissingParameterException
import mobi.eyeline.ips.messages.UssdModel
import mobi.eyeline.ips.messages.UssdOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.messages.UssdOption.UssdOptionType;
import mobi.eyeline.ips.messages.UssdOption
import mobi.eyeline.ips.repository.QuestionOptionRepository
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.repository.SurveyInvitationRepository
import mobi.eyeline.ips.repository.SurveyRepository;

class UssdServiceTest extends DbTestCase {
    private UssdService ussdService
    private SurveyRepository surveyRepository = new SurveyRepository(db)
    private SurveyInvitationRepository surveyInvitationRepository = new SurveyInvitationRepository(db)

    private SurveyService surveyService = new SurveyService(surveyRepository,surveyInvitationRepository)
    private RespondentRepository respondentRepository = new RespondentRepository(db)
    private AnswerRepository answerRepository = new AnswerRepository(db)
    private QuestionRepository questionRepository = new QuestionRepository(db)
    QuestionOptionRepository questionOptionRepository = new QuestionOptionRepository(db)
    def configClass
    Config config
    String loginUrl = 'http://localhost:39932'

    class UssdOptionImpl extends UssdOption{
        protected UssdOptionImpl(int key, String text, boolean skipValidation, int surveyId, UssdOptionType type) {
            super(key, text, skipValidation, surveyId, type)
        }

        @Override
        UssdModel handle(String msisdn, MessageHandler handler) {
            return null
        }
    }

    void setUp(){
        super.setUp()
        configClass = new MockFor(Config).with {
            demand.getLoginUrl() {loginUrl}
            it
        }
        config = configClass.proxyDelegateInstance() as Config

        ussdService = new UssdService(config,
                                    surveyService,
                                    respondentRepository,
                                    answerRepository,
                                    questionRepository,
                                    questionOptionRepository)
    }

    void tearDown() {
        configClass.verify config
    }

    void testSurveyUrl() {
        def survey = new Survey(id: 1, startDate: new Date(), endDate: new Date())
        assertEquals(ussdService.getSurveyUrl(survey), ("$loginUrl/ussd/index.jsp?survey_id=${survey.id}"))
    }

    void testSurvetUrl_NullSurvey() {
        shouldFail(NullPointerException) {
            ussdService.getSurveyUrl(null)
        }
    }

    void testHandle1() {
        UssdOption ussdOption = new UssdOptionImpl(1,"",true,1,UssdOptionType.ANSWER)
        shouldFail(AssertionError){
            ussdService.handle("89131234567", ussdOption)
        }
    }



    void testHandle_NullAbonent() {
        Map<String, String[]> parameters = new HashMap<String,String[]>()
        parameters.put("abonent",null)
        shouldFail(MissingParameterException){
            ussdService.handle(parameters)
        }
    }

    void testHandle_IncorrectType() {
        Map<String, String[]> parameters = new HashMap<String,String[]>()
        parameters.put("abonent","89131234567")
        parameters.put("type","")
        shouldFail(MissingParameterException){
            ussdService.handle(parameters)
        }
      //parse,findSurvey,
    }


}
