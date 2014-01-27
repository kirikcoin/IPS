package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyAndUserKey
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.model.UserSurveyPermissions

import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.*

class PermissionsRepositoryTest extends DbTestCase{

    private PermissionsRepository permissionsRepository
    private SurveyRepository surveyRepository
    private UserRepository userRepository

    void setUp() {
        super.setUp()
        permissionsRepository = new PermissionsRepository(db)
        surveyRepository = new SurveyRepository(db)
        userRepository = new UserRepository(db)
    }

    void testSaveAndLoad1() {
        def survey = new Survey(startDate: new Date(), endDate: new Date())
        surveyRepository.save survey

        def user = new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.MANAGER)
        userRepository.saveOrUpdate(user)

        def permissions =
                new UserSurveyPermissions(survey: survey, user: user)
        permissionsRepository.save(permissions)

        // Ensure that saved permissions record is indeed persisted.
        assertNotNull permissionsRepository.load(new SurveyAndUserKey(survey, user))
    }

    void testSaveAndLoad2() {
        def survey1, survey2
        (survey1, survey2) = [
                new Survey(startDate: new Date(), endDate: new Date()),
                new Survey(startDate: new Date(), endDate: new Date())
        ].each { surveyRepository.save it }

        def user1 = new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.CLIENT)
        def user2 = new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.MANAGER)
        def user3 = new User(
                login: "user",
                password: "password".pw(),
                email: "username@example.com",
                fullName: "John Doe",
                role: Role.ADMIN)

        [user1, user2, user3].each {userRepository.save it}

        def pk1 = new SurveyAndUserKey(survey: survey1, user: user1)
        def pk2 = new SurveyAndUserKey(survey: survey1, user: user2)
        def pk3 = new SurveyAndUserKey(survey: survey2, user: user1)
        def pk4 = new SurveyAndUserKey(survey: survey2, user: user3)

        [
                new UserSurveyPermissions(
                        survey: survey1,
                        user: user1),

                new UserSurveyPermissions(
                        survey: survey1,
                        user: user2),

                new UserSurveyPermissions(
                        survey: survey2,
                        user: user1),

                new UserSurveyPermissions(
                        survey: survey2,
                        user: user3
                )

        ].each {permissionsRepository.save it}

        assertNotNull permissionsRepository.get(pk1)
        assertThat permissionsRepository.list(), hasSize(4)
        assertNotNull permissionsRepository.get(pk4)
    }
}
