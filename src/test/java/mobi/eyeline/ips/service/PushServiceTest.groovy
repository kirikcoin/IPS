package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.properties.Config

class PushServiceTest extends GroovyTestCase {

    def configClass
    Config config

    void setUp() {
        configClass = new MockFor(Config).with {
            demand.getSadsMaxSessions() { 2 }
            it
        }

        config = configClass.proxyDelegateInstance() as Config
    }

    void tearDown() {
        configClass.verify config
    }

    static class MockPushService extends PushService {
        private final String expectedUri

        MockPushService(Config config, String expectedUri) {
            super(config, createMockEsdp())
            this.expectedUri = expectedUri
        }

        private static EsdpServiceSupport createMockEsdp() {
            new EsdpServiceSupport(null) {
                @Override
                String getServiceUrl(Survey survey) { "http://sads.com/push?survey_id=$survey.id" }
            }
        }

        // Run in the same thread
        void scheduleSend(Survey survey, String msisdn) { send(survey, msisdn) }
        protected void doRequest(URI uri) { assertEquals expectedUri, uri.toString() }
    }

    void test1() {
        def pushService = new MockPushService(
                config,
                'http://sads.com/push?survey_id=1&subscriber=123&skip_validation=true&scenario=default-noinform')
        pushService.scheduleSend new Survey(id: 1), '123'
    }
}


