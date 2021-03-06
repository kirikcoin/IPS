package mobi.eyeline.ips.model

import static mobi.eyeline.ips.model.Constants.SURVEY_END_TEXT_LENGTH
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

@Mixin(ValidationTestCase)
class SurveyDetailsTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()
    init()
  }

  void test1() {
    def violations = validate new SurveyDetails()
    assertThat violations, hasSize(1)
  }

  void test2() {
    def violations = validate new SurveyDetails(title: "")
    assertThat violations, hasSize(1)
  }

  void test3() {
    def violations = validate new SurveyDetails(title: "foo")
    assertThat violations, empty()
  }

  void test4() {
    def violations =
        validate new SurveyDetails(title: 't' * 46, endText: 'e' * (SURVEY_END_TEXT_LENGTH + 1))
    assertThat violations, hasSize(2)
  }

  void test5() {
    def violations =
        validate new SurveyDetails(title: 't' * 46, endText: 'e' * 69)
    assertThat violations, hasSize(1)
  }

  void test6() {
    def violations =
        validate new SurveyDetails(title: 't' * 44, endText: 'e' * 69)
    assertThat violations, hasSize(0)
  }
}
