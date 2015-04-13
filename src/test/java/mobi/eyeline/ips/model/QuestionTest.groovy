package mobi.eyeline.ips.model

import static mobi.eyeline.ips.utils.SurveyBuilder.question
import static mobi.eyeline.ips.utils.SurveyBuilder.survey
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

class QuestionTest extends ValidationTestCase {

  void test1() {
    assertThat validate(new Question()), hasSize(2)
    assertThat validate(new Question(title: '')), hasSize(2)
  }

  void test2() {
    assertThat validate(new Question(title: 'foo')), hasSize(1)
  }

  void test3() {
    def question = question(title: 'foo') {
      option(answer: 'Foo')
    }

    assertThat validate(question), empty()
  }

  void test4() {
    def question = question(title: 't' * 71) {
      option(answer: 'Foo')
    }

    def violations = validate question
    assertThat validate(question), hasSize(1)
    assertEquals 'title', violations[0].propertyPath.first().name
  }

  void test5() {
    assertThat validate(new QuestionOption()), hasSize(1)
  }

  void test6() {
    assertThat validate(new QuestionOption(answer: 'a' * 71)), hasSize(1)
  }

  void test7() {
    def question = question(title: 'foo') {
      option(answer: 'Foo', active: false)
    }

    assertThat validate(question), hasSize(1)
  }

  void testMoveUpSkipped() {

    // Prepare
    Question q1, q2, q3, q4
    Survey survey = survey([:]) {
      questions {
        q1 = question(title: '1')
        q2 = question(title: '2')
        q3 = question(title: '3')
        q4 = question(title: '4')
      }
    }

    survey.prepareIndex()

    //noinspection GroovyVariableNotAssigned
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
    Question q1, q2, q3, q4
    Survey survey = survey([:]) {
      questions {
        q1 = question(title: '1')
        q2 = question(title: '2')
        q3 = question(title: '3')
        q4 = question(title: '4')
      }
    }

    survey.prepareIndex()

    //noinspection GroovyVariableNotAssigned
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
