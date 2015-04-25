package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

@Mixin(ValidationTestCase)
class DeliverySubscriberTest extends GroovyTestCase {

  @Override
  protected void setUp() {
    super.setUp()
    init()
  }

  void test1() {
    assertThat validate(new DeliverySubscriber(msisdn: null, state: null)), hasSize(2)
  }

  void test2() {
    def subscriber = new DeliverySubscriber(msisdn: 'some text')
    def violations = validate subscriber
    assertEquals 'msisdn', violations[0].propertyPath.first().name
  }

}
