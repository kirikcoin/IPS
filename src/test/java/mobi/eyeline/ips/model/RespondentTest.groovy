package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

@Mixin(ValidationTestCase)
class RespondentTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()
    init()
  }

  void test1() {
    assertThat validate(new Respondent()), hasSize(1)
  }

  void test2() {
    assertThat validate(new Respondent(msisdn: "foo")), hasSize(0)
    assertThat validate(new Respondent(msisdn: "123")), hasSize(0)

  }
}
