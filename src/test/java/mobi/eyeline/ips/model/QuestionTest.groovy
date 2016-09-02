package mobi.eyeline.ips.model

import static mobi.eyeline.ips.utils.SurveyBuilder.question
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

@Mixin(ValidationTestCase)
class QuestionTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()
    init()
  }

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
    def question = question(title: 't' * (Constants.QUESTION_TITLE_LENGTH + 1)) {
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
    assertThat \
      validate(new QuestionOption(answer: 'a' * (Constants.QUESTION_OPTION_LENGTH + 1))),
      hasSize(1)
  }

  void test7() {
    def question = question(title: 'foo') {
      option(answer: 'Foo', deleted: true)
    }

    assertThat validate(question), hasSize(1)
  }

}
