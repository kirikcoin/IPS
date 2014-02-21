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
        String formatUserActivation(User u) { fail() }
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
            String formatUserDeactivation(User u) { "" }
            String formatUserActivation(User u) { "" }
        }
        mailService = new MailService(templateService, senderProxy)
        userService = new UserService(userRepository, mailService)
    }

    void testRestorePassword() {

        user =  new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT)

        userRepository.save(user)
        userService.resetPassword("username@example.com")
        user = userRepository.getByEmail("username@example.com")
        assertFalse user.password.equals("password".pw())


    }

    void testDeActivate() {
        user =  new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                blocked: false,
                role: Role.CLIENT)

        userRepository.save(user)
        userService.deActivate(user)
        assertTrue(user.blocked)
    }

    void testActivate() {
        user =  new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                blocked: true,
                role: Role.CLIENT)

        userRepository.save(user)
        userService.activate(user)
        assertFalse(user.blocked)
    }

    void testLoginEmailAllowed() {
        User user1 =  new User(
                login: "user1",
                password: "password".pw(),
                email: "username1@example.com",
                fullName: "John Doe1",
                blocked: false,
                role: Role.CLIENT)

        User user2 =  new User(
                login: "user2",
                password: "password".pw(),
                email: "username2@example.com",
                fullName: "John Doe2",
                blocked: false,
                role: Role.CLIENT)

        User user3 =  new User(
                login: "user1",
                password: "password".pw(),
                email: "username1@example.com",
                fullName: "John Doe2",
                blocked: false,
                role: Role.CLIENT)

        userRepository.save(user1)

        assertTrue(userService.isLoginAllowed(user1))
        assertTrue(userService.isEmailAllowed(user1))

        assertTrue(userService.isLoginAllowed(user2))
        assertTrue(userService.isEmailAllowed(user2))

        assertFalse(userService.isLoginAllowed(user3))
        assertFalse(userService.isEmailAllowed(user3))

    }

    void testCheckPassword() {
        User user =  new User(
                login: "user1",
                password: "password".pw(),
                email: "username1@example.com",
                fullName: "John Doe1",
                blocked: false,
                role: Role.CLIENT)

        userRepository.save(user)

        assertTrue(userService.checkPassword(user,"password"))
        assertFalse(userService.checkPassword(user,"wrongPassword"))

    }



}
