package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User

@Mixin(RepositoryMock)
class UserRepositorySessionTest extends DbTestCase {
    def manager
    void setUp() {
        super.setUp()

        initRepository(db)
    }

    void testListByRole() {

        manager = new User(
                login: 'testManager',
                password: "testManagerPassw".pw(),
                email: 'manager@example.com',
                fullName: 'John Doe',
                role: Role.MANAGER,
                uiProfile: new UiProfile())

        userRepository.save(manager)

        [
                new User(
                        login: "user1", fullName: "User", password: "123".pw(), email: "mail@mail.ru", role: Role.CLIENT, manager: manager),
                new User(
                        login: "user2", fullName: "User", password: "123".pw(), email: "mail2@mail.ru", role: Role.ADMIN, manager: manager),
                new User(
                        login: "user3", fullName: "User", password: "123".pw(), email: "mail3@mail.ru",uiProfile: new UiProfile(), role: Role.MANAGER),
                new User(
                        login: "user4", fullName: "User", password: "123".pw(), email: "mail4@mail.ru", uiProfile: new UiProfile(), role: Role.MANAGER),
                new User(
                        login: "user5", fullName: "User", password: "123".pw(), email: "mail5@mail.ru", role: Role.CLIENT, manager: manager),
                new User(
                        login: "user6", fullName: "User", password: "123".pw(), email: "mail6@mail.ru", role: Role.CLIENT, manager: manager)
        ].each {u -> userRepository.save u}

        assertEquals 3, userRepository.listByRole(Role.CLIENT).size()
        assertEquals 3, userRepository.listByRole(Role.MANAGER).size()
        assertEquals 1, userRepository.listByRole(Role.ADMIN).size()
    }

    void fillTestData() {

        manager = new User(
                login: 'testManager',
                password: "testManagerPassw".pw(),
                email: 'manager@example.com',
                fullName: 'John Doe',
                role: Role.MANAGER,
                uiProfile: new UiProfile())

        userRepository.save(manager)

        [
                new User(login: 'a', email: 'b@a.com', fullName: 'A B_', company: 'D\\%'),
                new User(login: 'b', email: 'c@a.com', fullName: 'D C', company: 'A%'),
                new User(login: 'c', email: 'd@a.com', fullName: 'D A', company: 'B_'),
                new User(login: 'd', email: 'a@a.com', fullName: 'A C_', company: 'C\\', blocked: true)
        ].each { user ->
            user.password = '12'.pw();
            user.role = Role.CLIENT;
            user.manager = manager;
            userRepository.save user
        }
    }

    void testList() {
        fillTestData()

        def assertIds = { expected, users -> assertEquals(expected, users.collect { it.id }) }

        assertIds([2, 3, 4, 5], userRepository.list('a', null, true, Integer.MAX_VALUE, 0))
        assertIds([2, 3, 4], userRepository.list('b', null, true, Integer.MAX_VALUE, 0))
        assertIds([2, 4, 3], userRepository.list('b', 'fullName', true, Integer.MAX_VALUE, 0))
        assertIds([2, 5, 4], userRepository.list('a.com', 'company', false, 3, 0))
        assertIds([5, 2, 3, 4], userRepository.list('c', 'status', false, Integer.MAX_VALUE, 0))
    }

    void testListWithSymbols(){
        fillTestData()

        def assertIds = { expected, users -> assertEquals(expected, users.collect { it.id }) }

        def list = userRepository.&list

        assertIds([2, 4, 5], list('_', null, true, Integer.MAX_VALUE, 0))
        assertIds([2, 3], list('%', null, true, Integer.MAX_VALUE, 0))
        assertIds([2, 5], list('\\', null, true, Integer.MAX_VALUE, 0))
    }

    void testCount() {
        fillTestData()

        assertEquals(4, userRepository.count('a'))
        assertEquals(4, userRepository.count('c'))
        assertEquals(3, userRepository.count('b'))
        assertEquals(4, userRepository.count(''))
    }
}
