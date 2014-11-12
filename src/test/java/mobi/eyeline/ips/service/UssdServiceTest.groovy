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

import static mobi.eyeline.ips.messages.AnswerOption.PARAM_ANSWER_ID
import static mobi.eyeline.ips.messages.AnswerOption.PARAM_QUESTION_ID
import static mobi.eyeline.ips.messages.UssdOption.PARAM_BAD_COMMAND
import static mobi.eyeline.ips.messages.UssdOption.PARAM_MESSAGE_TYPE
import static mobi.eyeline.ips.messages.UssdOption.PARAM_MSISDN
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

    final String msisdn    = '123'
    final String sid       = '1'

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
                surveyRepository, questionRepository, questionOptionRepository,
                surveyInvitationRepository,
                invitationDeliveryRepository)
        pushService = new PushService(config, new EsdpServiceSupport(null) {
            @Override String getServiceUrl(Survey survey) { "http://sads?push$survey.id" }
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
                questionOptionRepository)
    }

    def survey = { surveyRepository.load(sid.toInteger()) }

    def answer(option) {
        def props = [:]
        props.putAll option.properties
        props[UssdOption.PARAM_MSISDN] = msisdn
        request(props)
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
                (PARAM_MESSAGE_TYPE):  ANSWER,
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
                    (PARAM_MESSAGE_TYPE):  ANSWER,
                    (PARAM_QUESTION_ID):   1,
                    (PARAM_ANSWER_ID):     1,
            ])
        }
    }

    void testQuestionPageUnknownRequest3() {
        shouldFail(MissingParameterException) {
            request([
                    (PARAM_MSISDN):        '123',
                    (PARAM_MESSAGE_TYPE):  "$ANSWER",
                    (PARAM_ANSWER_ID):     1,
                    (PARAM_SURVEY_ID):     1
            ])
        }
    }

    void testQuestionPageUnknownRequest4() {
        shouldFail(MissingParameterException) {
            request([
                    (PARAM_MSISDN):        '123',
                    (PARAM_MESSAGE_TYPE):  "$ANSWER",
                    (PARAM_QUESTION_ID):   1,
                    (PARAM_SURVEY_ID):     1
            ])
        }
    }

    void createTestSurvey() {
        def survey =
                new Survey(startDate: new Date() - 2, endDate: new Date() + 2)
        survey.details = new SurveyDetails(survey: survey, title: 'Foo', endText: 'End text')
        surveyRepository.save survey

        def q1 = new Question(survey: survey, title: 'First one')
        def q2 = new Question(survey: survey, title: 'Second one')
        def q3 = new Question(survey: survey, title: 'With options')

        q1.options << new QuestionOption(answer: 'O1', question: q1, nextQuestion: q2)
        q2.options << new QuestionOption(answer: 'O2', question: q2, nextQuestion: q3)
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
            nextQuestion = null
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

        // Accessing start page again.
        def page1Again = request([
                (PARAM_MSISDN):    msisdn,
                (PARAM_SURVEY_ID): sid
        ])

        assertEquals 0, respondentRepository.countFinishedBySurvey(survey())
        assertNull 'Stats should be cleared',
                answerRepository.getLast(survey(), respondentRepository.load(1))

        //noinspection GroovyAccessibility
        assertThat 'Stats should be cleared',
                answerRepository.list(respondentRepository.load(1)), hasSize(0)

        def surveySessions = answerRepository.list(
                survey(),
                survey().startDate,
                survey().endDate,
                null,
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

    void testBadCommand() {
        createTestSurvey()
        //noinspection GroovyUnusedAssignment
        def page1 = request([
                (PARAM_SURVEY_ID):      1,
                (PARAM_MSISDN):         '79131234567',
        ]).with {
            assertEquals 'First one', it.text
        }

        request([
                (PARAM_SURVEY_ID):      1,
                (PARAM_MESSAGE_TYPE):   ANSWER,
                (PARAM_QUESTION_ID):    1,
                (PARAM_ANSWER_ID):      1,
                (PARAM_MSISDN):         '79131234567',
        ]).with {
            assertEquals 'Second one', it.text
        }

        request([
                (PARAM_SURVEY_ID):      1,
                (PARAM_MESSAGE_TYPE):   ANSWER,
                (PARAM_QUESTION_ID):    1,
                (PARAM_ANSWER_ID):      1,
                (PARAM_MSISDN):         '79131234567',
                (PARAM_BAD_COMMAND):    null
        ]).with {
            assertEquals 'Second one', it.text
        }

    }
}
