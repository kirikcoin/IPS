package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.*

import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat

class SurveyRepositoryTest extends DbTestCase {
    private SurveyRepository surveyRepository
    private QuestionRepository questionRepository
    private QuestionOptionRepository questionOptionRepository
    private UserRepository userRepository

    def user1, user2, user3, user4, user5

    void setUp() {
        super.setUp()
        surveyRepository = new SurveyRepository(db)
        questionRepository = new QuestionRepository(db)
        questionOptionRepository = new QuestionOptionRepository(db)
        userRepository = new UserRepository(db)
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

        assertIds([1, 2, 3, 4], surveyRepository.list(null,'', null, null,false,Integer.MAX_VALUE, 0))
        assertIds([1, 2, 4], surveyRepository.list(null,'A', null, null,false,Integer.MAX_VALUE, 0))
        assertIds([1, 4, 2], surveyRepository.list(null,'A', null, 'title',true,Integer.MAX_VALUE, 0))
        assertIds([4, 2, 1], surveyRepository.list(null,'A', null, 'id',false,Integer.MAX_VALUE, 0))
        assertIds([3, 2, 1], surveyRepository.list(null,'', true, 'accessNumber',false,Integer.MAX_VALUE, 0))

        assertIds([1, 3], surveyRepository.list(null,'F', true, null,false,Integer.MAX_VALUE, 0))
        assertIds([1, 2, 3], surveyRepository.list(null,'7', true, null,false,Integer.MAX_VALUE, 0))
        assertIds([3], surveyRepository.list(null,'07', true, null,false,Integer.MAX_VALUE, 0))
        assertIds([2], surveyRepository.list(user2,'', true, null,false,Integer.MAX_VALUE, 0))

    }

    void testCount() {
        fillTestData();

        assertEquals(3, surveyRepository.count(null,'A',null))
        assertEquals(3, surveyRepository.count(null,'F',null))
        assertEquals(4, surveyRepository.count(null,'7913',null))

        assertEquals(2, surveyRepository.count(null,'A',true))
        assertEquals(3, surveyRepository.count(null,'7913',true))
        assertEquals(1, surveyRepository.count(null,'4',null))
        assertEquals(1, surveyRepository.count(user1,'',null))

    }


    private void fillTestData() {
        [
                user1 = new User(
                        login: "user1", fullName: "F B", password: "123".pw(), email: "mail@mail.ru", role: Role.CLIENT),
                user2 = new User(
                        login: "user2", fullName: "D C", password: "123".pw(), email: "mail2@mail.ru", role: Role.CLIENT),
                user3 = new User(
                        login: "user3", fullName: "D F", password: "123".pw(), email: "mail3@mail.ru", role: Role.CLIENT),
                user4 = new User(
                        login: "user4", fullName: "F C", password: "123".pw(), email: "mail4@mail.ru", role: Role.CLIENT)
        ].each {u -> userRepository.save u}

        def survey1 =
                new Survey(id: 1, startDate: new Date(), endDate: new Date(), client: user1)
        def survey2 =
                new Survey(id: 2, startDate: new Date(), endDate: new Date(), client: user2)
        def survey3 =
                new Survey(id: 3, startDate: new Date(), endDate: new Date(), client: user3)
        def survey4 =
                new Survey(id: 4, active: false, startDate: new Date(), endDate: new Date(), client: user4)

        def details1 = new SurveyDetails(survey: survey1, title: 'A A')
        def details2 = new SurveyDetails(survey: survey2, title: 'B A')
        def details3 = new SurveyDetails(survey: survey3, title: 'D C')
        def details4 = new SurveyDetails(survey: survey4, title: 'A C')

        def stats1 = new SurveyStats(survey: survey1, accessNumber: "79130000005")
        def stats2 = new SurveyStats(survey: survey2, accessNumber: "79130000006")
        def stats3 = new SurveyStats(survey: survey3, accessNumber: "79130000007")
        def stats4 = new SurveyStats(survey: survey4, accessNumber: "79130000008")

        survey1.with {details = details1
                        statistics = stats1}
        survey2.with {details = details2
                        statistics = stats2}
        survey3.with {details = details3
                        statistics = stats3}
        survey4.with {details = details4
                        statistics = stats4}

        [survey1, survey2, survey3, survey4].each {s-> surveyRepository.save s}
    }

}
