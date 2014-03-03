package mobi.eyeline.ips.model

import org.apache.commons.lang3.RandomStringUtils

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

class QuestionTest extends ValidationTestCase {

    void test1() {
        assertThat validate(new Question()), hasSize(2)
        assertThat validate(new Question(title: "",)), hasSize(2)
    }

    void test2() {
        assertThat validate(new Question(title: "foo")), hasSize(1)
    }

    void test3() {
        def question =
                new Question(title: "foo", options: [new QuestionOption(answer: 'Foo')])
        assertThat validate(question), hasSize(0)
    }

    void test4(){
        def title = RandomStringUtils.randomAlphabetic(71)
        def question =
                new Question(title: title, options: [new QuestionOption(answer: 'Foo')])
        def violations = validate question
        assertThat validate(question), hasSize(1)
        assertEquals "title", violations[0].propertyPath.first().name
    }

    void testMoveUpSkipped() {

        // Prepare
        def add = {s, q -> s.questions.add(q); q.survey = s}
        def newQ = {_ -> new Question(title: _)}

        Survey survey = new Survey()

        Question q1 = newQ('1')
        Question q2 = newQ('2')
        Question q3 = newQ('3')
        Question q4 = newQ('4')

        [q1, q2, q3, q4].each {add(survey, it)}

        survey.prepareIndex()

        assertEquals([q1, q2, q3, q4], survey.questions)

        //

        survey.moveUp q1
        assertEquals([q2, q1, q3, q4], survey.questions)

        q1.active = false
        survey.moveUp q2
        assertEquals([q1, q3, q2, q4], survey.questions)

        survey.moveUp q2
        assertEquals([q1, q3, q4, q2], survey.questions)

        survey.moveUp q2
        assertEquals([q1, q3, q4, q2], survey.questions)

        survey.moveUp q3
        assertEquals([q1, q4, q3, q2], survey.questions)

        q2.active = false
        survey.moveUp q3
        assertEquals([q1, q4, q2, q3], survey.questions)

        survey.moveUp q4
        assertEquals([q1, q2, q3, q4], survey.questions)
    }

    void testMoveDownSkipped() {

        // Prepare
        def add = {s, q -> s.questions.add(q); q.survey = s}
        def newQ = {_ -> new Question(title: _)}

        Survey survey = new Survey()

        Question q1 = newQ('1')
        Question q2 = newQ('2')
        Question q3 = newQ('3')
        Question q4 = newQ('4')

        [q1, q2, q3, q4].each {add(survey, it)}

        survey.prepareIndex()

        assertEquals([q1, q2, q3, q4], survey.questions)

        //

        survey.moveDown q4
        assertEquals([q1, q2, q4, q3], survey.questions)

        q2.active = false
        survey.moveDown q4
        assertEquals([q4, q1, q2, q3], survey.questions)

        survey.moveDown q1
        assertEquals([q1, q4, q2, q3], survey.questions)

        survey.moveDown q1
        assertEquals([q1, q4, q2, q3], survey.questions)

        survey.moveDown q3
        assertEquals([q1, q3, q4, q2], survey.questions)

        q3.active = false
        survey.moveDown q4
        assertEquals([q4, q1, q3, q2], survey.questions)

        survey.moveDown q1
        assertEquals([q1, q4, q3, q2], survey.questions)
    }

}
