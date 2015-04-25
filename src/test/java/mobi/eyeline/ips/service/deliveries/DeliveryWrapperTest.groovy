package mobi.eyeline.ips.service.deliveries

import mobi.eyeline.ips.model.DeliverySubscriber
import mobi.eyeline.ips.model.InvitationDelivery

class DeliveryWrapperTest extends GroovyTestCase {

  void testSpeed() {
    def w = new DeliveryWrapper(new InvitationDelivery(speed: 10), 10)

    assertEquals 100, w.proposedDelayMillis

    w.speed = 20
    assertEquals 50, w.proposedDelayMillis
  }

  void testMessages() {
    def w = new DeliveryWrapper(new InvitationDelivery(speed: 10), 10)

    (0..<10).each { w.put(new DeliveryWrapper.Message(it, "$it")) }
    assertEquals 10, w.size()
    (0..<10).each { w.poll().id == it }
  }

  void testMessageState() {
    def m = new DeliveryWrapper.Message(1, '123')

    m.sent = false
    assertEquals DeliverySubscriber.State.UNDELIVERED, m.state

    m.sent = true
    assertEquals DeliverySubscriber.State.SENT, m.state
  }
}
