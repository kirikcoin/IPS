package mobi.eyeline.ips.service.deliveries

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.properties.Config

class DeliveryPushServiceTest extends GroovyTestCase {

    // Configuration
    def configClass
    Config config

    DeliveryPushService deliveryPushService

    void setUp() {
        super.setUp()

        // Configuration
        configClass = new MockFor(Config).with {
            demand.getSadsMaxSessions() { 2 }
            demand.getDeliveryUssdPushUrl() { 'http://foo/push-ussd' }
            demand.getDeliveryNIPushUrl() { 'http://foo/push-srv' }
            it
        }
        config = configClass.proxyDelegateInstance() as Config

        deliveryPushService = new DeliveryPushService(config) {
            def requested

            @Override
            protected void doRequest(URI uri) throws IOException {
                requested = uri
            }
        }
    }

    void testPushUssd() {
        deliveryPushService.with {
            pushUssd(1, '79131112233', 'Hello there')
            assertEquals \
             'http://foo/push-ussd?subscriber=79131112233&message=Hello+there&resource_id=1',
                    requested.toString()
        }

        deliveryPushService.with {
            pushUssd(10, '123', 'Ки рил ли ца')
            assertEquals \
             'http://foo/push-ussd?subscriber=123&message=%D0%9A%D0%B8+%D1%80%D0%B8%D0%BB+%D0%BB%D0%B8+%D1%86%D0%B0&resource_id=10',
                    requested.toString()
        }
    }

    void testPushNi() {
        deliveryPushService.with {
            niDialog(1, '123', 12345)
            assertEquals \
             'http://foo/push-srv?subscriber=123&survey_id=12345&resource_id=1',
                    requested.toString()
        }
    }

}
