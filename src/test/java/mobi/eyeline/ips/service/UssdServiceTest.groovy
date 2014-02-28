package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.messages.MissingParameterException
import mobi.eyeline.ips.messages.UssdOption
import mobi.eyeline.ips.messages.UssdResponseModel
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.*

class UssdServiceTest extends DbTestCase {

    UssdService ussdService

    String baseUrl = 'http://localhost:39932'
    def configClass
    Config config

    SurveyRepository surveyRepository
    SurveyInvitationRepository surveyInvitationRepository
    SurveyService surveyService
    RespondentRepository respondentRepository
    AnswerRepository answerRepository
    QuestionRepository questionRepository
    QuestionOptionRepository questionOptionRepository

    void setUp(){
        super.setUp()

        // Configuration
        configClass = new MockFor(Config)
        configClass.demand.getLoginUrl() { baseUrl }
        config = configClass.proxyDelegateInstance() as Config

        // Dependencies
        surveyRepository = new SurveyRepository(db)
        surveyInvitationRepository = new SurveyInvitationRepository(db)
        surveyService = new SurveyService(surveyRepository, surveyInvitationRepository)
        respondentRepository = new RespondentRepository(db)
        answerRepository = new AnswerRepository(db)
        questionRepository = new QuestionRepository(db)
        questionOptionRepository = new QuestionOptionRepository(db)

        ussdService = new UssdService(
                config,
                surveyService,
                respondentRepository,
                answerRepository,
                questionRepository,
                questionOptionRepository)
    }

    void tearDown() {
        configClass.verify config
        super.tearDown()
    }

    void testNullSurveyUrl() {
        shouldFail(NullPointerException) { ussdService.getSurveyUrl(null) }
    }

    Map<String, String[]> asMultimap(Map map) {
        [:].with { map.entrySet().each { put(it.key.toString(), [it.value.toString()] as String[]) }; it }
    }

    void testStartPageMissingSurvey() {
        def model = ussdService.handle asMultimap(
                ["$UssdOption.PARAM_SURVEY_ID": '100500', "$UssdOption.PARAM_MSISDN": '123'])

        assertTrue model.options.empty
        assertTrue(model instanceof UssdResponseModel.NoSurveyResponseModel)
    }

    void testStartPageRequiredParam() {
        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap(["$UssdOption.PARAM_SURVEY_ID": '100500'])
        }

        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap(["$UssdOption.PARAM_MSISDN": '123'])
        }
    }

    void testStartPageUnknownRequest() {
        def model = ussdService.handle asMultimap(
                ["$UssdOption.PARAM_MSISDN": '123', "$UssdOption.PARAM_MESSAGE_TYPE": 'foo'])

        assertTrue model.options.empty
    }



/*
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
*/

    void createTestSurvey() {

    }
}
