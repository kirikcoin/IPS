package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

@Mixin(ValidationTestCase)
class AnswerTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()
    init()
  }

  void test1() {
    assertThat validate(new TextAnswer(text: null)), hasSize(1)
    assertThat validate(new OptionAnswer(option: null)), hasSize(1)
  }
}
