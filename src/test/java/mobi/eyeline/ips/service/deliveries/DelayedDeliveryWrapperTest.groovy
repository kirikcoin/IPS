package mobi.eyeline.ips.service.deliveries

import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.util.TimeSource

import static java.util.concurrent.TimeUnit.MILLISECONDS
import static mobi.eyeline.ips.service.deliveries.DeliveryWrapper.DelayedDeliveryWrapper

class DelayedDeliveryWrapperTest extends GroovyTestCase {

  def wrapper = new DeliveryWrapper(new InvitationDelivery(speed: 10), 10)

  @Override
  void setUp() {
    super.setUp()
  }

  void testDelay() {
    def timeSource = new TimeSource() {
      @Override
      long currentTimeMillis() { 1_000_000 }
    }

    assertEquals 100,
        DelayedDeliveryWrapper.forSent(timeSource, wrapper).getDelay(MILLISECONDS)
    assertEquals 1000,
        DelayedDeliveryWrapper.forDelay(timeSource, wrapper, 1000).getDelay(MILLISECONDS)
  }

  void testOrder1() {
    def t1 = new MockTimeSource(now: 1000)
    def t2 = new MockTimeSource(now: 1010)

    def d1 = DelayedDeliveryWrapper.forSent(t1, wrapper)    // t1 + fixed delay
    def d2 = DelayedDeliveryWrapper.forSent(t2, wrapper)    // t2 + fixed delay

    assertTrue d1.deliveryWrapper.is(wrapper)
    assertTrue d2.deliveryWrapper.is(wrapper)

    t1.now = t2.now = 1050

    assertEquals 50, d1.getDelay(MILLISECONDS)
    assertEquals 60, d2.getDelay(MILLISECONDS)

    assertTrue d1 < d2
  }

  void testOrder2() {
    def t1 = new MockTimeSource(now: 1000)

    def d1 = DelayedDeliveryWrapper.forSent(t1, wrapper)
    def d2 = DelayedDeliveryWrapper.forDelay(t1, wrapper, 1000)

    assertTrue d1 < d2
  }

  static class MockTimeSource extends TimeSource {
    long now

    @Override
    long currentTimeMillis() { now }
  }
}
