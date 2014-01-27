package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.properties.Config

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.isEmptyString

class MailServiceTest extends GroovyTestCase {

    def configClass
    def config

    def senderProxy

    def user

    @SuppressWarnings("GroovyMissingReturnStatement")
    static class StubTemplateService extends TemplateService {

        StubTemplateService(Config properties) {
            super(properties)
        }

        String formatUserRegistration(User u, String s) { fail() }
        String formatUserDeactivation(User u) { fail() }
        String formatPasswordRestore(User u, String s) { fail() }
    }

    void setUp() {
        configClass = new MockFor(Config)
        config = configClass.proxyDelegateInstance() as Config

        senderProxy = new SmtpSender("-", 0, "-", "-", "-") {
            void send(MailService.Message message) {
                assertEquals "username@example.com", message.targetEmail
                assertThat message.subject, not(isEmptyString())
            }
        }

        user = new User(
                login: "admin",
                password: "ignored",
                email: "username@example.com",
                fullName: "John Doe")
    }

    void tearDown() {
        configClass.verify config
    }

    void testSendUserRegistration() {
        def templateService = new StubTemplateService(config) {
            String formatUserRegistration(User u, String s) { "" }
        }

        new MailService(templateService, senderProxy)
                .sendUserRegistration(user, "pw\$!jFo22/=")
    }

    void testSendUserDeactivation() {
        def templateService = new StubTemplateService(config) {
            String formatUserDeactivation(User u) { "" }
        }

        new MailService(templateService, senderProxy)
                .sendUserDeactivation(user)
    }

    void testSendPasswordRestore() {
        def templateService = new StubTemplateService(config) {
            String formatPasswordRestore(User u, String s) { "" }
        }

        new MailService(templateService, senderProxy)
                .sendPasswordRestore(user, "-")
    }

}
