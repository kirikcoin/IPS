package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.messages.MissingParameterException
import mobi.eyeline.ips.messages.UssdOption
import mobi.eyeline.ips.messages.UssdResponseModel
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.*

import static mobi.eyeline.ips.messages.AnswerOption.PARAM_ANSWER_ID
import static mobi.eyeline.ips.messages.AnswerOption.PARAM_QUESTION_ID
import static mobi.eyeline.ips.messages.UssdOption.*
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

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
                ["$PARAM_SURVEY_ID": '100500', "$PARAM_MSISDN": '123'])

        assertTrue model.options.empty
        assertTrue(model instanceof UssdResponseModel.NoSurveyResponseModel)
    }

    void testStartPageRequiredParam() {
        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap(["$PARAM_SURVEY_ID": '100500'])
        }

        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap(["$PARAM_MSISDN": '123'])
        }
    }

    void testStartPageUnknownRequest() {
        def model = ussdService.handle asMultimap(
                ["$PARAM_MSISDN": '123', "$PARAM_MESSAGE_TYPE": 'foo'])

        assertTrue(model instanceof UssdResponseModel.ErrorResponseModel)
    }

    void testQuestionPageUnknownRequest1() {
        def model = ussdService.handle asMultimap([
                "$PARAM_MSISDN": '123',
                "$PARAM_MESSAGE_TYPE": "$UssdOption.UssdOptionType.ANSWER",
                "$PARAM_QUESTION_ID": "1",
                "$PARAM_ANSWER_ID": "1",
                "$PARAM_SURVEY_ID": "1"   // Missing
        ])

        assertTrue(model instanceof UssdResponseModel.NoSurveyResponseModel)
    }

    void testQuestionPageUnknownRequest2() {
        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap([
                    "$PARAM_MSISDN": '123',
                    "$PARAM_MESSAGE_TYPE": "$UssdOption.UssdOptionType.ANSWER",
                    "$PARAM_QUESTION_ID": "1",
                    "$PARAM_ANSWER_ID": "1",
            ])
        }
    }

    void testQuestionPageUnknownRequest3() {
        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap([
                    "$PARAM_MSISDN": '123',
                    "$PARAM_MESSAGE_TYPE": "$UssdOption.UssdOptionType.ANSWER",
                    "$PARAM_ANSWER_ID": "1",
                    "$PARAM_SURVEY_ID": "1"
            ])
        }
    }

    void testQuestionPageUnknownRequest4() {
        shouldFail(MissingParameterException) {
            ussdService.handle asMultimap([
                    "$PARAM_MSISDN": '123',
                    "$PARAM_MESSAGE_TYPE": "$UssdOption.UssdOptionType.ANSWER",
                    "$PARAM_QUESTION_ID": "1",
                    "$PARAM_SURVEY_ID": "1"
            ])
        }
    }

    void createTestSurvey() {
        def survey =
                new Survey(startDate: new Date().minus(2), endDate: new Date().plus(2))

        def details = new SurveyDetails(survey: survey, title: 'Foo')
        survey.details = details
        surveyRepository.save survey

        def q1 = new Question(survey: survey, title: "First one")
        q1.options.add(new QuestionOption(answer: 'O1', question: q1))

        def q2 = new Question(survey: survey, title: "Second one")
        q2.options.add(new QuestionOption(answer: 'O2', question: q2))

        def q3 = new Question(survey: survey, title: "With options")
        q3.options.add(new QuestionOption(answer: 'O3', question: q3))

        survey.questions.addAll([q1, q2, q3])

        surveyRepository.update(survey)

        q3 = questionRepository.load(3)

        [
                new QuestionOption(answer: "Option 1", question: q3),
                new QuestionOption(answer: "Option 2", question: q3),
                new QuestionOption(answer: "Option 3", question: q3)
        ].each {q3.options.add it}

        questionRepository.update(q3)
    }

    void test1() {
        createTestSurvey()

        def model1 = ussdService.handle asMultimap([
                "$PARAM_MSISDN": '123',
                "$PARAM_SURVEY_ID": "1"
        ])

        // Ensure `Respondent' gets created.
        assertNotNull respondentRepository.load(1)
        assertEquals 1, respondentRepository.load(1).survey.id

        // Ensure that the link is correct
        model1.with {
            assertEquals 'First one', text
            assertThat options, hasSize(1)

            assertEquals 'O1', options.first().text
            assertEquals 1, options.first().answerId
            assertEquals 1, options.first().questionId
            assertEquals 1, options.first().surveyId
        }

        assertEquals 1, questionRepository.load(1).sentCount

        // Follow the link.
        def model2 = ussdService.handle asMultimap(
                model1.options.first().getProperties().with { put("$PARAM_MSISDN", '123'); it })
        model2.with {
            assertEquals 'Second one', text
            assertThat options, hasSize(1)

            assertEquals 'O2', options.first().text
            assertEquals 2, options.first().answerId
            assertEquals 2, options.first().questionId
            assertEquals 1, options.first().surveyId
        }

        assertEquals 1, respondentRepository.load(1).answersCount
        assertEquals 1, questionRepository.load(2).sentCount

        // Follow the link again.
        def model3 = ussdService.handle asMultimap(
                model2.options.first().getProperties().with { put("$PARAM_MSISDN", '123'); it })
        model3.with {
            assertEquals 'With options', text
            assertThat options, hasSize(4)
        }

        assertEquals 2, respondentRepository.load(1).answersCount
        assertEquals 2, respondentRepository.load(1).answersCount

    }
}
