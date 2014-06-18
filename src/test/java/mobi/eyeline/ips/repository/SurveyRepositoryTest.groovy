package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.*

import static mobi.eyeline.ips.model.Role.MANAGER
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
        def survey =
                new Survey(startDate: new Date(), endDate: new Date())

        def details = new SurveyDetails(survey: survey, title: 'Foo')
        survey.details = details
        surveyRepository.save survey

        def q1 = new Question(survey: survey, title: "First one")
        q1.options.add(new QuestionOption(answer: 'O1', question: q1))

        def q2 = new Question(survey: survey, title: "Second one")
        q2.options.add(new QuestionOption(answer: 'O2', question: q2))

        def q3 = new Question(survey: survey, title: "With options")
        q3.options.add(new QuestionOption(answer: 'O3', question: q3))

        survey.questions.addAll([q1, q2, q3])

        surveyRepository.update(survey)

        q3 = questionRepository.load(3)

        [
            new QuestionOption(answer: "Option 1", question: q3),
            new QuestionOption(answer: "Option 2", question: q3),
            new QuestionOption(answer: "Option 3", question: q3)
        ].each {q3.options.add it}

        questionRepository.update(q3)

        assertThat surveyRepository.load(1).questions, hasSize(3)
        assertThat questionRepository.list(), hasSize(3)
        assertThat questionRepository.load(3).options, hasSize(4)
    }

    void testQuestionOrdering() {
        def survey =
                new Survey(startDate: new Date(), endDate: new Date())
        def sid = surveyRepository.save survey

        def loadSurvey = {surveyRepository.load sid}

        def newQuestion = {title ->
            def q = new Question(survey: survey, title: title);
            q.options.add(new QuestionOption(answer: "${q.title}-option", question: q))
            q
        }

        def q1 = newQuestion 'Q1'
        def q2 = newQuestion 'Q2'
        def q3 = newQuestion 'Q3'
        def q4 = newQuestion 'Q4'

        survey.questions.addAll([q1, q2, q3, q4])
        surveyRepository.update(survey)

        assertEquals([q1, q2, q3, q4], loadSurvey().questions)

        survey.moveUp q1
        surveyRepository.update(survey)

        assertEquals([q2, q1, q3, q4], loadSurvey().questions)

        survey.moveUp q1
        surveyRepository.update(survey)

        assertEquals([q2, q3, q1, q4], loadSurvey().questions)

        survey.moveDown q1
        surveyRepository.update(survey)

        assertEquals([q2, q1, q3, q4], loadSurvey().questions)
    }

    void testOptionOrdering() {

        def prepare = {
            def survey =
                    new Survey(startDate: new Date(), endDate: new Date())

            def details = new SurveyDetails(survey: survey, title: 'Foo')
            survey.details = details
            surveyRepository.save survey

            def newQuestion = {title ->
                def q = new Question(survey: survey, title: title);
                q.options.add(new QuestionOption(answer: "${q.title}-option", question: q))
                q
            }

            survey.questions.addAll([
                    newQuestion("First one"),
                    newQuestion("Second one")])

            surveyRepository.update(survey)
        }

        prepare()

        def survey = surveyRepository.load(1)

        def question = new Question(survey: survey, title: "With options")
        question.options.add(new QuestionOption(answer: 'Foo', question: question))

        survey.questions.add(question)
        surveyRepository.update(survey)

        // For ordering
        surveyRepository.refresh(survey)
        question = survey.questions.last()

        [
                new QuestionOption(answer: "Option 1", question: question),
                new QuestionOption(answer: "Option 2", question: question),
                new QuestionOption(answer: "Option 3", question: question)
        ].each {question.options.add it}

        questionRepository.update(question)

        assertThat survey.questions, hasSize(3)
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
        assertIds([3, 2, 1], list(null, null, '', true, 'accessNumber', false, Integer.MAX_VALUE, 0))

        assertIds([1, 3], list(null, null, 'F', true, null, false, Integer.MAX_VALUE, 0))
        assertIds([1, 2, 3], list(null, null, '7', true, null, false, Integer.MAX_VALUE, 0))
        assertIds([3], list(null, null, '07', true, null, false, Integer.MAX_VALUE, 0))
        assertIds([2], list(user2, null, '', true, null, false, Integer.MAX_VALUE, 0))


    }

    void testListWithSymbols(){
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
        assertEquals(4, surveyRepository.count(null, null, '7913', null))

        assertEquals(2, surveyRepository.count(null, null, 'A', true))
        assertEquals(3, surveyRepository.count(null, null, '7913', true))
        assertEquals(1, surveyRepository.count(null, null, '4', null))
        assertEquals(1, surveyRepository.count(user1, null, '', null))
    }

    private void fillTestData() {
        [
            user1 = new User(login: 'user1', fullName: 'F B', email: 'mail@mail.ru'),
            user2 = new User(login: 'user2', fullName: 'D C', email: 'mail2@mail.ru'),
            user3 = new User(login: 'user3', fullName: 'D F', email: 'mail3@mail.ru'),
            user4 = new User(login: 'user4', fullName: 'F C', email: 'mail4@mail.ru'),
        ].each { u ->
            u.role = Role.CLIENT
            u.password = '123'.pw()
            userRepository.save u
        }

        userRepository.save user5 = new User(
                login: 'user5', fullName: 'F C', email: 'mail5@mail.ru',
                role: MANAGER, password: '123'.pw())

        userRepository.save user6 = new User(
                login: 'user6', fullName: 'F C', email: 'mail6@mail.ru',
                role: MANAGER, password: '123'.pw(), onlyOwnSurveysVisible: true)

        [
            new Survey(id: 1, client: user1).with {
                details = new SurveyDetails(survey: it, title: 'A A%\\')
                statistics = new SurveyStats(survey: it, accessNumber: "79130000005")
                it
            },

            new Survey(id: 2, client: user2, owner: user6).with {
                details = new SurveyDetails(survey: it, title: 'B A_\\')
                statistics = new SurveyStats(survey: it, accessNumber: "79130000006")
                it
            },

            new Survey(id: 3, client: user3, owner: user5).with {
                details = new SurveyDetails(survey: it, title: 'D C _')
                statistics = new SurveyStats(survey: it, accessNumber: "79130000007")
                it
            },

            new Survey(id: 4, client: user4, active: false).with {
                details = new SurveyDetails(survey: it, title: 'A C%')
                statistics = new SurveyStats(survey: it, accessNumber: "79130000008")
                it
            }
        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save s 
        }
    }

}
