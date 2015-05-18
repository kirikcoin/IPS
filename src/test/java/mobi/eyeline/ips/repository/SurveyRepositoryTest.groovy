package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.*

import static mobi.eyeline.ips.model.Role.MANAGER
import static mobi.eyeline.ips.utils.SurveyBuilder.survey
import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat

@Mixin(RepositoryMock)
class SurveyRepositoryTest extends DbTestCase {

  User user1, user2, user3, user4, user5, user6

  void setUp() {
    super.setUp()

    initRepository(db)
  }

  void testSaveAndLoad() {
    def s = survey(startDate: new Date(), endDate: new Date()) {
      details(title: 'Foo')

      pages {
        question(title: 'First one') {
          option(answer: 'O1')
        }

        question(title: 'Second one') {
          option(answer: 'O2')
        }

        question(title: 'With options') {
          option(answer: 'O3')
        }
      }
    }

    surveyRepository.save s

    // Add a few more options to check update operations.
    def q3 = questionRepository.load(3)

    survey {
      pages {
        question(q3) {
          option(answer: 'Option 1')
          option(answer: 'Option 2')
          option(answer: 'Option 3')
        }
      }
    }

    questionRepository.update(q3)

    assertThat surveyRepository.load(1).questions, hasSize(3)
    assertThat questionRepository.list(), hasSize(3)
    assertThat questionRepository.load(3).options, hasSize(4)
  }

  void testOptionOrdering() {
    def s = survey(startDate: new Date(), endDate: new Date()) {
      details(title: 'Foo')
      pages {
        question(title: 'First one') {
          option(answer: "${enclosing.title}-option")
        }
        question(title: 'Second one') {
          option(answer: "${enclosing.title}-option")
        }
      }
    }

    surveyRepository.save(s)

    s = surveyRepository.load(1)

    def q = new Question(survey: s, title: 'With options')
    q.options << new QuestionOption(answer: 'Foo', question: q)

    s.pages << q
    surveyRepository.update(s)

    // For ordering
    surveyRepository.refresh(s)

    q = s.questions.last()

    survey {
      pages {
        question(q) {
          option(answer: 'Option1')
          option(answer: 'Option2')
          option(answer: 'Option3')
        }
      }
    }

    questionRepository.update(q)

    assertThat s.questions, hasSize(3)
    assertThat questionRepository.list(), hasSize(3)
    assertThat questionRepository.load(3).options, hasSize(4)
  }

  void testList() {
    fillTestData()

    def assertIds = { expected, surveys -> assertEquals(expected, surveys.collect { it.id }) }

    def list = surveyRepository.&list

    assertIds([1, 2, 3, 4], list(null, null, '', null, null, false, Integer.MAX_VALUE, 0))
    assertIds([1, 2, 4], list(null, null, 'A', null, null, false, Integer.MAX_VALUE, 0))
    assertIds([1, 4, 2], list(null, null, 'A', null, 'title', true, Integer.MAX_VALUE, 0))
    assertIds([4, 2, 1], list(null, null, 'A', null, 'id', false, Integer.MAX_VALUE, 0))
    assertIds([4, 2, 3, 1], list(null, null, '', null, 'state', false, Integer.MAX_VALUE, 0))

    assertIds([1, 3], list(null, null, 'F', true, null, false, Integer.MAX_VALUE, 0))
    assertIds([], list(null, null, '7', true, null, false, Integer.MAX_VALUE, 0))
    assertIds([], list(null, null, '07', true, null, false, Integer.MAX_VALUE, 0))
    assertIds([2], list(user2, null, '', true, null, false, Integer.MAX_VALUE, 0))
  }

  void testListWithSymbols() {
    fillTestData()

    def assertIds = { expected, surveys -> assertEquals(expected, surveys.collect { it.id }) }

    def list = surveyRepository.&list

    assertIds([2, 3], list(null, null, '_', null, null, false, Integer.MAX_VALUE, 0))
    assertIds([1, 4], list(null, null, '%', null, null, false, Integer.MAX_VALUE, 0))
    assertIds([1, 2], list(null, null, '\\', null, null, false, Integer.MAX_VALUE, 0))
  }

  void testListOwners() {
    fillTestData()

    def assertIds = { expected, surveys -> assertEquals(expected, surveys.collect { it.id }) }

    def list = surveyRepository.&list

    assertIds([1, 3], list(null, user5, '', true, null, false, Integer.MAX_VALUE, 0))
    assertIds([1, 2], list(null, user6, '', true, null, false, Integer.MAX_VALUE, 0))
  }

  void testCount() {
    fillTestData();

    assertEquals(3, surveyRepository.count(null, null, 'A', null))
    assertEquals(3, surveyRepository.count(null, null, 'F', null))
    assertEquals(0, surveyRepository.count(null, null, '7913', null))

    assertEquals(2, surveyRepository.count(null, null, 'A', true))
    assertEquals(0, surveyRepository.count(null, null, '7913', true))
    assertEquals(1, surveyRepository.count(null, null, '4', null))
    assertEquals(1, surveyRepository.count(user1, null, '', null))
  }

  private void fillTestData() {
    def manager = new User(
        login: 'testManager',
        password: "testManagerPassw".pw(),
        email: 'manager@example.com',
        fullName: 'John Doe',
        role: MANAGER,
        uiProfile: new UiProfile())

    userRepository.save(manager)
    [
        user1 = new User(login: 'user1', fullName: 'F B', email: 'mail@mail.ru'),
        user2 = new User(login: 'user2', fullName: 'D C', email: 'mail2@mail.ru'),
        user3 = new User(login: 'user3', fullName: 'D F', email: 'mail3@mail.ru'),
        user4 = new User(login: 'user4', fullName: 'F C', email: 'mail4@mail.ru'),
    ].each { u ->
      u.role = Role.CLIENT
      u.manager = manager
      u.password = '123'.pw()
      userRepository.save u
    }

    final accessNumbers = [
        new AccessNumber(number: '79130000005'),
        new AccessNumber(number: '79130000006'),
        new AccessNumber(number: '79130000007'),
        new AccessNumber(number: '79130000008')
    ].collect {
      accessNumberRepository.save(it)
      it
    }

    userRepository.save user5 = new User(
        login: 'user5', fullName: 'F C', email: 'mail5@mail.ru',
        role: MANAGER, password: '123'.pw(), uiProfile: new UiProfile())

    userRepository.save user6 = new User(
        login: 'user6', fullName: 'F C', email: 'mail6@mail.ru',
        role: MANAGER, password: '123'.pw(), uiProfile: new UiProfile(), onlyOwnSurveysVisible: true)

    [
        survey(id: 1, client: user1, startDate: new Date() + 2, endDate: new Date() + 4) {
          details(title: 'A A%\\')
          statistics([:])
        },

        survey(id: 2, client: user2, owner: user6,
            startDate: new Date() - 4, endDate: new Date() + 1) {
          details(title: 'B A_\\')
          statistics([:])
        },

        survey(id: 3, client: user3, owner: user5,
            startDate: new Date() - 4, endDate: new Date() + 2) {
          details(title: 'D C _')
          statistics([:])
        },

        survey(id: 4, client: user4, active: false,
            startDate: new Date() - 4, endDate: new Date() - 2) {
          details(title: 'A C%')
          statistics([:])
        }

    ].each { s ->
      surveyRepository.save s

      accessNumbers[s.id - 1].surveyStats = s.statistics
      accessNumberRepository.update(accessNumbers[s.id - 1])
    }
  }

}
