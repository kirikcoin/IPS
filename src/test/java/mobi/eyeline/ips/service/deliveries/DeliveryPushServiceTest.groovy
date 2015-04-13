package mobi.eyeline.ips.service.deliveries

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.service.BasePushService
import mobi.eyeline.ips.service.EsdpServiceSupport

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
      it
    }
    config = configClass.proxyDelegateInstance() as Config

    deliveryPushService = new DeliveryPushService(config, new EsdpServiceSupport(null) {
      @Override
      String getServiceUrl(Survey survey) { "http://foo/push?id=$survey.id" }
    }) {
      def requested

      @Override
      protected void doRequest(URI uri, BasePushService.RequestExecutionListener listener) {
        requested = uri
      }
    }
  }

  void testPushUssd() {
    deliveryPushService.with {
      pushUssd(1, new Survey(id: 42), '79131112233', 'Hello there', null)
      assertEquals  \
              'http://foo/push?id=42&scenario=push-inform&protocol=ussd&subscriber=79131112233&message=Hello+there&resource_id=1',
          requested.toString()
    }

    deliveryPushService.with {
      pushUssd(10, new Survey(id: 42), '123', 'Ки рил ли ца', null)
      assertEquals  \
              'http://foo/push?id=42&scenario=push-inform&protocol=ussd&subscriber=123&message=%D0%9A%D0%B8+%D1%80%D0%B8%D0%BB+%D0%BB%D0%B8+%D1%86%D0%B0&resource_id=10',
          requested.toString()
    }
  }


  void testPushSms() {
    deliveryPushService.with {
      pushSms(1, new Survey(id: 42), '79131112233', 'Hello there', null)
      assertEquals  \
              'http://foo/push?id=42&scenario=push-inform&protocol=sms&subscriber=79131112233&message=Hello+there&resource_id=1',
          requested.toString()
    }

    deliveryPushService.with {
      pushSms(10, new Survey(id: 42), '123', 'Ки рил ли ца', null)
      assertEquals  \
              'http://foo/push?id=42&scenario=push-inform&protocol=sms&subscriber=123&message=%D0%9A%D0%B8+%D1%80%D0%B8%D0%BB+%D0%BB%D0%B8+%D1%86%D0%B0&resource_id=10',
          requested.toString()
    }
  }

  void testPushNi() {
    deliveryPushService.with {
      niDialog(1, new Survey(id: 42), '123', null)
      assertEquals  \
              'http://foo/push?id=42&scenario=default-inform&subscriber=123&survey_id=42&resource_id=1',
          requested.toString()
    }
  }

}
