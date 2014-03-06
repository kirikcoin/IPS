package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.*

import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat

/**
 * Created by dizan on 06.03.14.
 */
class AnswerRepositoryTest extends DbTestCase {
    private AnswerRepository answerRepository
    private SurveyRepository surveyRepository
    private RespondentRepository respondentRepository
    private QuestionRepository questionRepository
    private QuestionOptionRepository questionOptionRepository

    private final Date now = new Date()

    void setUp() {
        super.setUp()

        answerRepository = new AnswerRepository(db)
        surveyRepository = new SurveyRepository(db)
        respondentRepository = new RespondentRepository(db)
        questionRepository = new QuestionRepository(db)
        questionOptionRepository = new QuestionOptionRepository(db)
    }

    def assertIds =
            { expected, answers -> assertEquals(expected, answers.collect { it.id }) }

    def survey = { int id -> surveyRepository.load(id) as Survey}
    def respondent = { int id -> respondentRepository.load(id) as Respondent }

    void testCount1() {
        fillTestData()
        assertEquals([3, 2, 2], questionRepository.list().collect {answerRepository.count it})
    }

    void testCount2() {
        fillTestData()

        assertEquals([2, 1, 0, 1, 0, 1, 0, 1, 1],
                questionOptionRepository.list().collect {answerRepository.count it})
    }

    void testCount3() {
        fillTestData()

        assertEquals(1, answerRepository.count(survey(1), now, now + 1, ''))
        assertEquals(1, answerRepository.count(survey(1), now, now + 2, '02'))
    }

    void testList() {
        fillTestData()

        def results =
                answerRepository.list(survey(1), now, now + 4,'', null, false, Integer.MAX_VALUE, 0)

        assertThat results, hasSize(2)

        results[0].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(2).id, respondent.id
            assertIds([2, 5, 7], answers)
        }

        results[0].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(1).id, respondent.id
            assertIds([1, 4, 6], answers)
        }


    }

    void testGetLast() {
        fillTestData()

        assertEquals(4, answerRepository.getLast(survey(1), respondent(1)).id)
    }

    void testClear() {
        fillTestData()

        def thisSurvey = survey(1)
        def thisRespondent = respondent(1)

        answerRepository.clear(thisSurvey, thisRespondent)

        def notRemoved = answerRepository
                .list()
                .grep {
                    it.question.survey.id == thisSurvey.id &&
                    it.respondent.id == thisRespondent.id }

        assertThat notRemoved, empty()
        assertEquals([4, 4], survey(1).questions.collect { it.sentCount })
    }

    private void fillTestData() {
        Survey survey1, survey2, survey3, survey4

        [
                survey1 = new Survey(id: 1),
                survey2 = new Survey(id: 2),
                survey3 = new Survey(id: 3),
                survey4 = new Survey(id: 4, active: false)
        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save s
        }

        [
                new Respondent(id: 1, startDate: now + 1, survey: survey1, finished: false),
                new Respondent(id: 2, startDate: now + 2, survey: survey1, finished: false),
                new Respondent(id: 3, startDate: now + 3, survey: survey2, finished: true),
                new Respondent(id: 4, startDate: now + 4, survey: survey2, finished: false),
        ].each {r ->
            r.msisdn = "7913000000${r.id}"
            respondentRepository.save r
        }

        survey1.questions.addAll([
            new Question(survey: survey1, title: "First one", sentCount: 5).with {
                options << new QuestionOption(answer: "Option 1", question: it)
                options << new QuestionOption(answer: "Option 2", question: it)
                options << new QuestionOption(answer: "Option 3", question: it)
                it
            },

            new Question(survey: survey1, title: "Second one", sentCount: 5).with {
                options << new QuestionOption(answer: "Option 1", question: it)
                options << new QuestionOption(answer: "Option 2", question: it)
                options << new QuestionOption(answer: "Option 3", question: it)
                it
            }])
        surveyRepository.update(survey1)

        survey2.questions << new Question(survey: survey2, title: "Third one").with {
            options << new QuestionOption(answer: "Option 1", question: it)
            options << new QuestionOption(answer: "Option 2", question: it)
            options << new QuestionOption(answer: "Option 3", question: it)
            it
        }
        surveyRepository.update(survey2)

        [
                new Answer(
                        date: now + 1,
                        option: survey(1).questions[0].options[0],
                        question: survey(1).questions[0],
                        respondent: respondent(1)),

                new Answer(
                        date: now + 2,
                        option: survey(1).questions[0].options[0],
                        question: survey(1).questions[0],
                        respondent: respondent(2)),

                new Answer(
                        date: now + 3,
                        option: survey(1).questions[0].options[1],
                        question: survey(1).questions[0],
                        respondent: respondent(3)),

                new Answer(
                        date: now + 4,
                        option: survey(1).questions[1].options[0],
                        question: survey(1).questions[1],
                        respondent: respondent(1)),

                new Answer(
                        date: now + 5,
                        option: survey(1).questions[1].options[2],
                        question: survey(1).questions[1],
                        respondent: respondent(2)),

                new Answer(
                        date: now + 6,
                        option: survey(2).questions[0].options[2],
                        question: survey(2).questions[0],
                        respondent: respondent(1)),

                new Answer(
                        date: now + 7,
                        option: survey(2).questions[0].options[1],
                        question: survey(2).questions[0],
                        respondent: respondent(2))
        ].each {a -> answerRepository.save a}
    }
}
