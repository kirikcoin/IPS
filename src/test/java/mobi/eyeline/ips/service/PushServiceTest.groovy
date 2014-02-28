package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.properties.Config

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.isEmptyString
import static org.hamcrest.Matchers.not

class PushServiceTest extends GroovyTestCase {

    def configClass
    Config config

    void setUp() {
        configClass = new MockFor(Config).with {
            demand.getSadsMaxSessions() { 2 }
            demand.getSadsPushUrl() { 'http://sads.com/push' }
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
            super(config)
            this.expectedUri = expectedUri
        }

        // Run in the same thread
        void scheduleSend(Survey survey, String msisdn) { send(survey, msisdn) }
        protected void doRequest(URI uri) { assertEquals expectedUri, uri.toString() }
    }

    void test1() {
        def pushService = new MockPushService(
                config,
                'http://sads.com/push?survey_id=1&subscriber=123&skip_validation=true');
        pushService.scheduleSend new Survey(id: 1), '123'
    }
}


