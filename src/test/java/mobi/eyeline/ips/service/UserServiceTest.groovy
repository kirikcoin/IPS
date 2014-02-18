package mobi.eyeline.ips.service


import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.UserRepository

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.isEmptyString

class UserServiceTest extends DbTestCase {
    private UserRepository userRepository
    private MailService mailService
    private UserService userService

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
        super.setUp()

        configClass = new MockFor(Config)
        config = configClass.proxyDelegateInstance() as Config
        senderProxy = new SmtpSender("-", 0, "-", "-", "-") {
            void send(MailService.Message message) {
                assertEquals "username@example.com", message.targetEmail
                assertThat message.subject, not(isEmptyString())
            }
        }
        userRepository = new UserRepository(db)

        def templateService = new StubTemplateService(config) {
            String formatPasswordRestore(User u, String s) { "" }
        }
        mailService = new MailService(templateService, senderProxy)
        userService = new UserService(userRepository, mailService)
    }

    void testRestorePassword() {
        def tx = db.currentSession.beginTransaction()

        user =  new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT)

        userRepository.save(user)
        userRepository.getByEmail("username@example.com")
        userService.resetPassword("username@example.com")
        user = userRepository.getByEmail("username@example.com")
        assertFalse user.password.equals("password".pw())

        tx.commit()
    }

}
