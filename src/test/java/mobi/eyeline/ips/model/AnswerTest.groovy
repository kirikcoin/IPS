package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

class AnswerTest extends ValidationTestCase {
  void test1() {
    assertThat validate(new TextAnswer(text: null)), hasSize(1)
    assertThat validate(new OptionAnswer(option: null)), hasSize(1)
  }
}
