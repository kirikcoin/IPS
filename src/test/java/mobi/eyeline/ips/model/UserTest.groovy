package mobi.eyeline.ips.model

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

class UserTest extends ValidationTestCase {

    void test1() {
        def violations = validate new User(
                login: null,
                password: "123".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT)

        assertThat violations, hasSize(1)
        assertEquals "login", violations[0].propertyPath.first().name
    }

    void test2() {
        def violations = validate new User(
                login: "user",
                password: "123".pw(),
                email: "123",
                fullName: "John Doe",
                role: Role.CLIENT)

        assertThat violations, hasSize(1)
        assertEquals "not a well-formed email address", violations[0].message
    }

    void test3() {
        def violations = validate new User(
                login: "user",
                password: "123".pw(),
                email: null,
                fullName: "John Doe",
                role: Role.CLIENT)

        assertThat violations, hasSize(1)
        assertEquals "email", violations[0].propertyPath.first().name
    }

    void test4() {
        def violations = validate new User(
                login: "user",
                password: null,
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT)

        assertThat violations, hasSize(1)
        assertEquals "password", violations[0].propertyPath.first().name
    }

    void test5() {
        def violations = validate new User(
                login: "user",
                password: "123".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.ADMIN)

        assertThat violations, empty()
    }

    void test6() {
        def violations = validate new User(
                login: "user", password: "123".pw(), email: "username@example.com", role: Role.CLIENT)

        assertThat violations, not(empty())
    }

    void test7() {
        def violations = validate new User(
                login: "user", password: "123".pw(), email: "username@example.com", fullName: 'J Doe')

        assertThat violations, not(empty())
    }
}
