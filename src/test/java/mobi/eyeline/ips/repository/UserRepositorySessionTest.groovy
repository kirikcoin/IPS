package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.UiProfile
import mobi.eyeline.ips.model.User

import static mobi.eyeline.ips.model.Role.ADMIN
import static mobi.eyeline.ips.model.Role.CLIENT
import static mobi.eyeline.ips.model.Role.MANAGER

@Mixin(RepositoryMock)
class UserRepositorySessionTest extends DbTestCase {
    
    User manager1, manager2
    
    void setUp() {
        super.setUp()

        initRepository(db)
    }

    void testListByRole() {

        def manager = new User(
                login: 'testManager',
                password: 'testManagerPassw'.pw(),
                email: 'manager@example.com',
                fullName: 'John Doe',
                role: MANAGER,
                uiProfile: new UiProfile())

        userRepository.save(manager)

        [
                new User(login: 'user1', email: 'mail@mail.ru', role: CLIENT, manager: manager),
                new User(login: 'user2', email: 'mail2@mail.ru', role: ADMIN, manager: manager),
                new User(login: 'user3', email: 'mail3@mail.ru', uiProfile: new UiProfile(), role: MANAGER),
                new User(login: 'user4', email: 'mail4@mail.ru', uiProfile: new UiProfile(), role: MANAGER),
                new User(login: 'user5', email: 'mail5@mail.ru', role: CLIENT, manager: manager),
                new User(login: 'user6', email: 'mail6@mail.ru', role: CLIENT, manager: manager)
        ].each { u ->
            u.fullName = 'User'
            u.password = 'bagel'.pw()
            userRepository.save u
        }

        assertEquals 3, userRepository.listByRole(CLIENT).size()
        assertEquals 3, userRepository.listByRole(MANAGER).size()
        assertEquals 1, userRepository.listByRole(ADMIN).size()
    }

    void fillTestData() {
        [
                new User(
                        login: 'manager1',
                        fullName: 'John Doe',
                        role: MANAGER,
                        uiProfile: new UiProfile()),
                new User(
                        login: 'manager2',
                        fullName: 'John Doe Jr.',
                        role: MANAGER,
                        showAllClients: true,
                        uiProfile: new UiProfile())
        ].each {
            it.email = "$it.login@example.com"
            it.password = 'bagel'.pw()
            userRepository.save it
        }

        manager1 = userRepository.load 1
        manager2 = userRepository.load 2

        [
                new User(login: 'a', email: 'b@a.com', fullName: 'A B_',    company: 'D\\%', manager: manager1),
                new User(login: 'b', email: 'c@a.com', fullName: 'D C',     company: 'A%', manager: manager2),
                new User(login: 'c', email: 'd@a.com', fullName: 'D A',     company: 'B_', manager: manager1),
                new User(login: 'd', email: 'a@a.com', fullName: 'A C_',    company: 'C\\', blocked: true, manager: manager1)
        ].each { user ->
            user.password = '12'.pw();
            user.role = CLIENT;
            user.manager = manager1;
            userRepository.save user
        }
    }

    void testList() {
        fillTestData()

        def assertIds = { expected, users -> assertEquals(expected, users.collect { it.id }) }

        assertIds([3, 4, 5, 6], userRepository.list(null, 'a', null, true, Integer.MAX_VALUE, 0))
        assertIds([3, 4, 5], userRepository.list(null, 'b', null, true, Integer.MAX_VALUE, 0))
        assertIds([3, 5, 4], userRepository.list(null, 'b', 'fullName', true, Integer.MAX_VALUE, 0))
        assertIds([3, 6, 5], userRepository.list(null, 'a.com', 'company', false, 3, 0))
        assertIds([6, 3, 4, 5], userRepository.list(null, 'c', 'status', false, Integer.MAX_VALUE, 0))
    }

    void testListWithSymbols(){
        fillTestData()

        def assertIds = { expected, users -> assertEquals(expected, users.collect { it.id }) }

        def list = userRepository.&list

        assertIds([3, 5, 6], list(null, '_', null, true, Integer.MAX_VALUE, 0))
        assertIds([3, 4], list(manager1, '%', null, true, Integer.MAX_VALUE, 0))
        assertIds([], list(manager2, '\\', null, true, Integer.MAX_VALUE, 0))
    }

    void testCount() {
        fillTestData()

        assertEquals(4, userRepository.count(null, 'a'))
        assertEquals(4, userRepository.count(null, 'c'))
        assertEquals(3, userRepository.count(manager1, 'b'))
        assertEquals(4, userRepository.count(null, ''))
    }
}
