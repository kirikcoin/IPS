package mobi.eyeline.ips.repository

import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat

@Mixin([RepositoryMock, SampleAnswers])
class AnswerRepositoryTest extends DbTestCase {

    void setUp() {
        super.setUp()

        initRepository(db)
    }

    def assertIds =
            { expected, answers -> assertEquals(expected, answers.collect { it.id }) }

    void testCount1() {
        fillTestData()
        assertEquals([3, 3, 3], questionRepository.list().collect {answerRepository.count it})
    }

    void testCount2() {
        fillTestData()

        assertEquals([2, 1, 0, 1, 0, 1, 0, 1, 1],
                questionOptionRepository.list().collect {answerRepository.count it})
    }

    void testCount3() {
        fillTestData()

        assertEquals(1, answerRepository.count(survey(1), now, now + 1, '', null))
        assertEquals(1, answerRepository.count(survey(1), now, now + 2, '02', null))
    }

    void testCount4() {
        fillTestData()

        assertEquals([0, 1, 1], questionRepository.list().collect {answerRepository.countTextAnswers it})
    }

    void testList1() {
        fillTestData()

        def results = answerRepository.list(
                survey(1), now, now + 4, '', null, false, Integer.MAX_VALUE, 0)

        assertThat results, hasSize(2)

        results[0].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(2).id, respondent.id
            assertIds([2, 5, 6, 8, 9], answers)
        }

        results[1].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(1).id, respondent.id
            assertIds([1, 4, 7], answers)
        }
    }

    void testList2() {
        fillTestData()

        def results = answerRepository.list(
                survey(1), now, now + 4, '02', null, false, Integer.MAX_VALUE, 0)

        assertThat results, hasSize(1)

        results[0].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(2).id, respondent.id
            assertIds([2, 5, 6, 8, 9], answers)
        }
    }

    void testList3() {
        fillTestData()

        def results = answerRepository.list(
                survey(1), now, now + 4,'', 'respondent', true, Integer.MAX_VALUE, 0)

        assertThat results, hasSize(2)

        results[0].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(1).id, respondent.id
            assertIds([1, 4, 7], answers)
        }

        results[1].with {
            assertEquals this.survey(1).id, survey.id
            assertEquals this.respondent(2).id, respondent.id
            assertIds([2, 5, 6, 8, 9], answers)
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
}
