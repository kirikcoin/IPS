package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

class AccessNumberTest extends ValidationTestCase {

  void test1() {
    def violations = validate new AccessNumber(number: null)
    assertThat violations, hasSize(1)
  }

  void test2() {
    def violations = validate new AccessNumber(number: '')
    assertThat violations, hasSize(1)
  }

  void test3() {
    def violations = validate new AccessNumber(number: 'abc')
    assertThat violations, hasSize(1)
  }

  void test4() {
    def violations = validate new AccessNumber(number: '88001234455')
    assertThat violations, empty()
  }
}
