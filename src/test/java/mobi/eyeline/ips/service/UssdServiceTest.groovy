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
import static org.hamcrest.Matchers.instanceOf

@SuppressWarnings("UnnecessaryQualifiedReference")
class UssdServiceTest extends DbTestCase {

    // Configuration
    def configClass
    Config config

    // Dependencies
    SurveyRepository surveyRepository
    SurveyInvitationRepository surveyInvitationRepository
    InvitationDeliveryRepository invitationDeliveryRepository
    SurveyService surveyService
    RespondentRepository respondentRepository
    AnswerRepository answerRepository
    QuestionRepository questionRepository
    QuestionOptionRepository questionOptionRepository

    UssdService ussdService

    final String msisdn    = '123'
    final String sid       = '1'

    void setUp(){
        super.setUp()

        // Configuration
        configClass = new MockFor(Config)
        configClass.demand.getBaseSurveyUrl() { 'http://localhost:39932' }
        config = configClass.proxyDelegateInstance() as Config

        // Dependencies
        surveyRepository = new SurveyRepository(db)
        surveyInvitationRepository = new SurveyInvitationRepository(db)
        invitationDeliveryRepository = new InvitationDeliveryRepository(db)
        surveyService = new SurveyService(
                surveyRepository,
                surveyInvitationRepository,
                invitationDeliveryRepository)
        respondentRepository = new RespondentRepository(db)
        answerRepository = new AnswerRepository(db)
        questionRepository = new QuestionRepository(db)
        questionOptionRepository = new QuestionOptionRepository(db)

        ussdService = new UssdService(
                config,
                surveyService,
                surveyRepository,
                respondentRepository,
                answerRepository,
                questionRepository,
                questionOptionRepository)
    }

    void tearDown() {
        configClass.verify config
        super.tearDown()
    }

    def survey = { surveyRepository.load(sid.toInteger()) }
    def request = { params -> ussdService.handle asMultimap(params) }

    def answer(option) {
        def props = [:]
        props.putAll option.properties
        props[UssdOption.PARAM_MSISDN] = msisdn
        request(props)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    Map<String, String[]> asMultimap(Map map) {
        map.collectEntries {k, v -> [(k.toString()): [v.toString()] as String[]]} as Map<String, String[]>
    }

    void testNullSurveyUrl() {
        shouldFail(NullPointerException) { ussdService.getSurveyUrl(null) }
    }

    void testStartPageMissingSurvey() {
        request([
                (PARAM_SURVEY_ID): '100500', (PARAM_MSISDN): '123'
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
            request([(PARAM_MSISDN): '123'])
        }
    }

    void testStartPageUnknownRequest() {
        request([
                (PARAM_MSISDN):        '123',
                (PARAM_MESSAGE_TYPE):  'foo'
        ]).with {
            assertThat it, instanceOf(UssdResponseModel.ErrorResponseModel)
        }
    }

    void testQuestionPageUnknownRequest1() {
        request([
                (PARAM_MSISDN):        '123',
                (PARAM_MESSAGE_TYPE):  UssdOptionType.ANSWER,
                (PARAM_QUESTION_ID):   1,
                (PARAM_ANSWER_ID):     1,
                (PARAM_SURVEY_ID):     1   // Missing
        ]).with {
            assertThat it, instanceOf(UssdResponseModel.NoSurveyResponseModel)
        }
    }

    void testQuestionPageUnknownRequest2() {
        shouldFail(MissingParameterException) {
            request([
                    (PARAM_MSISDN):        '123',
                    (PARAM_MESSAGE_TYPE):  UssdOptionType.ANSWER,
                    (PARAM_QUESTION_ID):   1,
                    (PARAM_ANSWER_ID):     1,
            ])
        }
    }

    void testQuestionPageUnknownRequest3() {
        shouldFail(MissingParameterException) {
            request([
                    (PARAM_MSISDN):        '123',
                    (PARAM_MESSAGE_TYPE):  "$UssdOptionType.ANSWER",
                    (PARAM_ANSWER_ID):     1,
                    (PARAM_SURVEY_ID):     1
            ])
        }
    }

    void testQuestionPageUnknownRequest4() {
        shouldFail(MissingParameterException) {
            request([
                    (PARAM_MSISDN):        '123',
                    (PARAM_MESSAGE_TYPE):  "$UssdOptionType.ANSWER",
                    (PARAM_QUESTION_ID):   1,
                    (PARAM_SURVEY_ID):     1
            ])
        }
    }

    void createTestSurvey() {
        def survey =
                new Survey(startDate: new Date().minus(2), endDate: new Date().plus(2))
        survey.details = new SurveyDetails(survey: survey, title: 'Foo', endText: 'End text')
        surveyRepository.save survey

        def q1 = new Question(survey: survey, title: 'First one')
        q1.options << new QuestionOption(answer: 'O1', question: q1)

        def q2 = new Question(survey: survey, title: 'Second one')
        q2.options << new QuestionOption(answer: 'O2', question: q2)

        def q3 = new Question(survey: survey, title: 'With options')
        q3.options << new QuestionOption(answer: 'O3', question: q3)

        survey.questions.addAll([q1, q2, q3])

        surveyRepository.update(survey)

        q3 = questionRepository.load(3)

        [
                new QuestionOption(answer: 'Option 1', question: q3),
                new QuestionOption(answer: 'Option 2', question: q3),
                new QuestionOption(answer: 'Option 3', question: q3)
        ].each {q3.options.add it}

        questionRepository.update(q3)
    }

    void testFullCycle() {
        createTestSurvey()

        def respondent = { respondentRepository.load(1) }

        //
        // Start page.
        //

        def page1 = request([
                (PARAM_MSISDN):    msisdn,
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
            assertEquals 0, respondentRepository.countFinishedBySurvey(it)
            assertEquals 1, respondentRepository.countBySurvey(it)
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
            assertEquals 0, respondentRepository.countFinishedBySurvey(it)
            assertEquals 1, respondentRepository.countBySurvey(it)
        }

        //
        // Answer #3.
        //

        def page4 = answer page3.options[2]
        page4.with {
            assertEquals survey().details.endText, text
            assertThat it, instanceOf(UssdResponseModel.TextUssdResponseModel)
        }

        // Check final stats.

        respondent().with {
            assertEquals 3, answersCount
            assertTrue finished
        }

        survey().with {
            assertEquals 1, respondentRepository.countFinishedBySurvey(it)
            assertEquals 1, respondentRepository.countBySurvey(it)
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
                (PARAM_MSISDN):    msisdn,
                (PARAM_SURVEY_ID): sid
        ]).with {
            assertThat it, instanceOf(UssdResponseModel.NoSurveyResponseModel)
        }

        request([
                (PARAM_MSISDN):             msisdn,
                (PARAM_SURVEY_ID):          sid,
                (PARAM_SKIP_VALIDATION):    true
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
                (PARAM_MSISDN):    msisdn,
                (PARAM_SURVEY_ID): sid
        ]).with {
            assertEquals survey.details.endText, it.text
        }
    }

    void testTerminal() {
        createTestSurvey()
        survey().questions[0].options[0].with {
            terminal = true
            questionOptionRepository.update it
        }

        def page1 = request([
                (PARAM_MSISDN):    msisdn,
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
                (PARAM_MSISDN):    msisdn,
                (PARAM_SURVEY_ID): sid
        ])
        answer page1.options.first()

        def page1Again = request([
                (PARAM_MSISDN):    msisdn,
                (PARAM_SURVEY_ID): sid
        ])

        page1Again.with {
            assertEquals 'Second one', text
            assertEquals 'questionId=2&answerId=2&skip_validation=false&type=ANSWER&survey_id=1',
                    options[0].uri
            assertEquals 'O2', options[0].text
        }
    }

    void testRestart() {
        def respondent = { respondentRepository.load(1) }

        testFullCycle()

        assertEquals 'With options',
                answerRepository.getLast(survey(), respondent()).question.title
        assertEquals([1, 1, 1],
                questionRepository.list().sort { it.id }.collect { it.sentCount })

        respondent().with {
            assertEquals 3, it.answersCount
            assertTrue it.finished
        }
        assertThat answerRepository.list(), hasSize(3)

        // Load the landing page again.
        // We expect the same output as the first time, statistics should be reset.

        request([
                (PARAM_MSISDN):    msisdn,
                (PARAM_SURVEY_ID): sid
        ]).with {
            assertEquals 'First one', it.text
        }

        assertNull answerRepository.getLast(survey(), respondent())
        assertEquals([1, 0, 0],
                questionRepository.list().sort { it.id }.collect { it.sentCount })
        respondent().with {
            assertEquals 0, it.answersCount
            assertFalse it.finished
        }
    }
}
