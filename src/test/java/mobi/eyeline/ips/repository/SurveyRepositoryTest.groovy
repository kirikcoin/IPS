package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.*

import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertThat

class SurveyRepositoryTest extends DbTestCase {
    private SurveyRepository surveyRepository
    private QuestionRepository questionRepository
    private QuestionOptionRepository questionOptionRepository
    private UserRepository userRepository

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
        def q2 = new Question(survey: survey, title: "Second one")
        def q3 = new Question(survey: survey, title: "With options")

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
        assertThat questionRepository.load(3).options, hasSize(3)
    }

    void testQuestionOrdering() {
        def survey =
                new Survey(startDate: new Date(), endDate: new Date())
        def sid = surveyRepository.save survey

        def loadSurvey = {surveyRepository.load sid}

        def newQuestion = {title -> new Question(survey: survey, title: title)}

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

            survey.questions.addAll([
                    new Question(survey: survey, title: "First one"),
                    new Question(survey: survey, title: "Second one")])

            surveyRepository.update(survey)
        }

        prepare()

        def survey = surveyRepository.load(1)

        def question = new Question(survey: survey, title: "With options")
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
        assertThat questionRepository.load(3).options, hasSize(3)
    }
}
