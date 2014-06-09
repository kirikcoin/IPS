package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.properties.Config

class ServiceTest extends GroovyTestCase {

    void testDoubleInitialization() {
        def newConfig = {
            def configClass = new MockFor(Config)
            configClass.demand.getDatabaseProperties() { new Properties() }
            configClass.demand.getSmtpHost() { '' }
            configClass.demand.getSmtpPort() { 0 }
            configClass.demand.getSmtpUsername() { '' }
            configClass.demand.getSmtpPassword() { '' }
            configClass.demand.getMailFrom() { '' }
            configClass.demand.getLoginUrl() { '' }
            configClass.demand.getSadsMaxSessions() { 1 }
            configClass.demand.getSkinDefault() { '' }
            configClass.demand.getSadsMaxSessions() { 1 }
            configClass.demand.getDeliveryUssdPushUrl() { '' }
            configClass.demand.getDeliveryNIPushUrl() { '' }
            configClass.demand.getMessageQueueBaseline() { 10 }
            configClass.demand.getPushThreadsNumber() { 2 }
            configClass.demand.getRetryAttempts() { 3 }
            configClass.demand.getRetryAttempts() { 3 }
            configClass.demand.getStateUpdateBatchSize() { 10 }
            configClass.demand.getExpirationDelaySeconds() { 600 }

            configClass.proxyDelegateInstance() as Config
        }

        Services.initialize(newConfig())

        shouldFail(AssertionError) {
            Services.initialize(newConfig())
        }
    }

    void testUninitialized() {
        //noinspection GroovyAccessibility
        Services.instance = null
        shouldFail(AssertionError) {
            Services.instance()
        }
    }
}
