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
        assertEquals 'email', violations[0].propertyPath.toString()
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

    void test8() {

        def violations = validate new User(
                login: 'l'*71,
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: 'f'*71,
                role: Role.CLIENT,
                company: 'c'*71
        )
        assertThat violations, hasSize(3)
    }

    void test9() {
        def violations = validate new User(
                login: 'l'*71,
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: 'f'*71,
                role: Role.CLIENT,
                company: "company"
        )
        assertThat violations, hasSize(2)
    }

    void test10() {
        def violations = validate new User(
                login: 'l'*71,
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
                company: "company"
        )
        assertThat violations, hasSize(1)
        assertEquals "login", violations[0].propertyPath.first().name
    }

    void test11() {
        def violations = validate new User(
                login: "login",
                password:  "123".pw(),
                email:  'e'*71,
                fullName: "John Doe",
                role: Role.CLIENT,
                company: "company"
        )
        assertThat violations, hasSize(2)
        assertEquals "email", violations[0].propertyPath.first().name
        assertEquals "email", violations[1].propertyPath.first().name
    }

    void test12() {
        def violations = validate new User(
                login: "login",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
                company: "company",
                phoneNumber:'p'*31
        )
        assertThat violations, hasSize(2)
        assertEquals "phoneNumber", violations[0].propertyPath.first().name
        assertEquals "phoneNumber", violations[1].propertyPath.first().name
    }
    void test13() {
        def violations = validate new User(
                login: "login",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
                company: "company",
                phoneNumber:'p'*9
        )
        assertThat violations, hasSize(1)
        assertEquals "phoneNumber", violations[0].propertyPath.first().name
    }

    void test14() {
        def violations = validate new User(
                login: "login",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
                company: "company",
                phoneNumber:'8'*31
        )
        assertThat violations, hasSize(1)
        assertEquals "phoneNumber", violations[0].propertyPath.first().name
    }

    void test15() {
        def violations = validate new User(
                login: "login",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
                company: "company",
                phoneNumber:'8'*29
        )
        assertThat violations, empty()
    }

    void test16() {
        def violations = validate new User(
                login: "login@+|]{[",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
        )
        assertThat violations, hasSize(1)
        assertEquals "login", violations[0].propertyPath.first().name
    }

    void test17() {
        def violations = validate new User(
                login: "l.-l",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT,
        )
        assertThat violations, empty()
    }

    void test18() {
        def violations1 = validate new User(
                login: "login1",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "symbols: %",
                role: Role.CLIENT,
        )
        def violations2 = validate new User(
                login: "login1",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "symbols: '",
                role: Role.CLIENT,
        )
        def violations3 = validate new User(
                login: "login1",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "symbols: \"",
                role: Role.CLIENT,
        )
        def violations4 = validate new User(
                login: "login1",
                password:  "123".pw(),
                email:  "username@example.com",
                fullName: "symbols: \\",
                role: Role.CLIENT,
        )
        assertThat violations1, hasSize(1)
        assertThat violations2, hasSize(1)
        assertThat violations3, hasSize(1)
        assertThat violations4, hasSize(1)

        assertEquals "fullName", violations1[0].propertyPath.first().name
        assertEquals "fullName", violations2[0].propertyPath.first().name
        assertEquals "fullName", violations3[0].propertyPath.first().name
        assertEquals "fullName", violations4[0].propertyPath.first().name
    }

}
