package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.User

@Mixin(RepositoryMock)
class UserRepositorySessionTest extends DbTestCase {

    void setUp() {
        super.setUp()

        initRepository(db)
    }

    void testListByRole() {
        [
                new User(
                        login: "user1", fullName: "User", password: "123".pw(), email: "mail@mail.ru", role: Role.CLIENT),
                new User(
                        login: "user2", fullName: "User", password: "123".pw(), email: "mail2@mail.ru", role: Role.ADMIN),
                new User(
                        login: "user3", fullName: "User", password: "123".pw(), email: "mail3@mail.ru", role: Role.MANAGER),
                new User(
                        login: "user4", fullName: "User", password: "123".pw(), email: "mail4@mail.ru", role: Role.MANAGER),
                new User(
                        login: "user5", fullName: "User", password: "123".pw(), email: "mail5@mail.ru", role: Role.CLIENT),
                new User(
                        login: "user6", fullName: "User", password: "123".pw(), email: "mail6@mail.ru", role: Role.CLIENT)
        ].each {u -> userRepository.save u}

        assertEquals 3, userRepository.listByRole(Role.CLIENT).size()
        assertEquals 2, userRepository.listByRole(Role.MANAGER).size()
        assertEquals 1, userRepository.listByRole(Role.ADMIN).size()
    }

    void fillTestData() {
        [
                new User(login: 'a', email: 'b@a.com', fullName: 'A B_', company: 'D\\%'),
                new User(login: 'b', email: 'c@a.com', fullName: 'D C', company: 'A%'),
                new User(login: 'c', email: 'd@a.com', fullName: 'D A', company: 'B_'),
                new User(login: 'd', email: 'a@a.com', fullName: 'A C_', company: 'C\\', blocked: true)
        ].each { user ->
            user.password = '12'.pw();
            user.role = Role.CLIENT;
            userRepository.save user
        }
    }

    void testList() {
        fillTestData()

        def assertIds = { expected, users -> assertEquals(expected, users.collect { it.id }) }

        assertIds([1, 2, 3, 4], userRepository.list('a', null, true, Integer.MAX_VALUE, 0))
        assertIds([1, 2, 3], userRepository.list('b', null, true, Integer.MAX_VALUE, 0))
        assertIds([1, 3, 2], userRepository.list('b', 'fullName', true, Integer.MAX_VALUE, 0))
        assertIds([1, 4, 3], userRepository.list('a.com', 'company', false, 3, 0))
        assertIds([4, 1, 2, 3], userRepository.list('c', 'status', false, Integer.MAX_VALUE, 0))
    }

    void testListWithSymbols(){
        fillTestData()

        def assertIds = { expected, users -> assertEquals(expected, users.collect { it.id }) }

        def list = userRepository.&list

        assertIds([1, 3, 4], list('_', null, true, Integer.MAX_VALUE, 0))
        assertIds([1, 2], list('%', null, true, Integer.MAX_VALUE, 0))
        assertIds([1, 4], list('\\', null, true, Integer.MAX_VALUE, 0))
    }

    void testCount() {
        fillTestData()

        assertEquals(4, userRepository.count('a'))
        assertEquals(4, userRepository.count('c'))
        assertEquals(3, userRepository.count('b'))
        assertEquals(4, userRepository.count(''))
    }
}
