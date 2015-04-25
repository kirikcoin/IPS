package mobi.eyeline.ips.model

import static mobi.eyeline.ips.utils.SurveyBuilder.survey
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.not

@Mixin(ValidationTestCase)
class SurveyTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()
    init()
  }

  void test1() {
    assertThat validate(survey()), not(empty())
  }

  void test2() {
    def survey = survey(
        startDate: Date.parse('HH:mm', '01:00'),
        endDate: Date.parse('HH:mm', '00:00'))

    assertThat validate(survey), not(empty())
  }

  void test3() {
    assertThat validate(survey()), not(empty())
  }

  void testValid1() {
    def survey = survey(
        startDate: Date.parse('HH:mm', '00:00'),
        endDate: Date.parse('HH:mm', '01:00'))

    assertThat validate(survey), empty()
  }

  void testValid2() {
    def survey = survey(
        startDate: Date.parse('HH:mm', '00:00'),
        endDate: Date.parse('HH:mm', '00:00'))

    assertThat validate(survey), empty()
  }
}
