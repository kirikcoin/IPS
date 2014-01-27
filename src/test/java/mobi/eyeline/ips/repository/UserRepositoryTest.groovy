package mobi.eyeline.ips.repository

import mobi.eyeline.ips.exceptions.LoginException
import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.User
import org.junit.Assert

import javax.validation.ConstraintViolationException

class UserRepositoryTest extends DbTestCase {

    private UserRepository userRepository

    void setUp() {
        super.setUp()
        userRepository = new UserRepository(db)
    }

    void testLoginOk() {
        def user = new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT)

        def savedId = userRepository.save(user)
        def fetched = userRepository.getUser("user", "password")

        Assert.assertEquals savedId, fetched.id
    }

    void testPasswordError() {
        def user = new User(
                login: "user",
                password: "fake",
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.ADMIN)

        userRepository.save(user)

        shouldFail(LoginException) {
            userRepository.getUser("user", "password")
        }

    }

    void testLoginError() {
        def user = new User(
                login: "fake",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.MANAGER)

        userRepository.save(user)

        shouldFail(LoginException) {
            userRepository.getUser("user", "password")
        }
    }

    void testUserModelValidation() {
        def users = [
                new User(
                        login: null, password: "123".pw(), email: "mail@mail.ru", role: Role.CLIENT),
                new User(
                        login: "user", password: "123".pw(), email: "123", role: Role.ADMIN),
                new User(
                        login: "user", password: "123".pw(), email: null, role: Role.MANAGER),
                new User(
                        login: "user", password: null, email: null, role: Role.CLIENT)
        ]

        shouldFail(ConstraintViolationException){
            userRepository.save(users[0])
        }
        shouldFail(ConstraintViolationException){
            userRepository.save(users[1])
        }
        shouldFail(ConstraintViolationException){
            userRepository.save(users[2])
        }
        shouldFail(ConstraintViolationException){
            userRepository.save(users[3])
        }
    }

}
