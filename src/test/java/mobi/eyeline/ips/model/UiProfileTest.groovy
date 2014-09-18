package mobi.eyeline.ips.model

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

class UiProfileTest extends ValidationTestCase {

    void test1() {
        
        def user = new User(
                login: 'jdoe',
                password: '123'.pw(),
                email: 'username@example.com',
                fullName: 'John Doe',
                role: Role.CLIENT,
                uiProfile: new UiProfile()
        )
        
        def violations = validate user
        assertThat violations, hasSize(1)
    }

    void test2() {

        def user = new User(
                login: 'jdoe',
                password: '123'.pw(),
                email: 'username@example.com',
                fullName: 'John Doe',
                role: Role.MANAGER,
                uiProfile: null
        )

        def violations = validate user
        assertThat violations, hasSize(1)
    }

    void test3() {

        def user = new User(
                login: 'jdoe',
                password: '123'.pw(),
                email: 'username@example.com',
                fullName: 'John Doe',
                role: Role.MANAGER,
                uiProfile: new UiProfile(
                        skin: null
                )
        )

        def violations = validate user.uiProfile
        assertThat violations, hasSize(1)
    }

}
