package mobi.eyeline.ips.service.deliveries

import mobi.eyeline.ips.model.InvitationDelivery

import static java.util.concurrent.TimeUnit.MILLISECONDS
import static mobi.eyeline.ips.service.deliveries.DeliveryWrapper.DelayedDeliveryWrapper

class DelayedDeliveryWrapperTest extends GroovyTestCase {

    def wrapper = new DeliveryWrapper(new InvitationDelivery(speed: 10), 10)

    void testDelay() {
        assertTrue DelayedDeliveryWrapper.forSent(wrapper).getDelay(MILLISECONDS) <= 100
        assertTrue DelayedDeliveryWrapper.forDelay(wrapper, 100).getDelay(MILLISECONDS) <= 100
    }

    void testOrder1() {
        def d1 = DelayedDeliveryWrapper.forSent(wrapper)
        def d2 = DelayedDeliveryWrapper.forSent(wrapper)

        assertTrue d1.deliveryWrapper.is(wrapper)
        assertTrue d2.deliveryWrapper.is(wrapper)

        assertTrue d1 <= d2
    }

    void testOrder2() {
        def d1 = DelayedDeliveryWrapper.forSent(wrapper)
        def d2 = DelayedDeliveryWrapper.forDelay(wrapper, 1000)

        assertTrue d1 < d2
    }
}
