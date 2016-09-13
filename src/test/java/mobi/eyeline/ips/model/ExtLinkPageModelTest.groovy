package mobi.eyeline.ips.model

import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertThat

@Mixin(ValidationTestCase)
class ExtLinkPageModelTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()

    init()
  }

  void testOk() {
    final violations =
        validate new ExtLinkPage(serviceName: 'myService', serviceUrl: 'http://example.com')
    assertThat violations, hasSize(0)
  }

  void test1() {
    final violations = validate new ExtLinkPage(serviceName: 'myService')
    assertThat violations, hasSize(1)
  }

  void test2() {
    final violations = validate new ExtLinkPage(serviceUrl: 'http://example.com')
    assertThat violations, hasSize(1)
  }

  void test3() {
    final violations =
        validate new ExtLinkPage(serviceName: 'myService', serviceUrl: 'totally not an URL')
    assertThat violations, hasSize(1)
  }

  void test4() {
    final violations =
        validate new ExtLinkPage(serviceName: 'myService', serviceUrl: 'http://10.151.0.44:38982/start?sid=18')
    assertThat violations, hasSize(0)
  }
}
