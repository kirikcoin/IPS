package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.*

import static mobi.eyeline.ips.model.QuestionKind.ListRadio
import static mobi.eyeline.ips.model.QuestionKind.LongFreeText
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
        surveyRepository.save survey

        def details = new SurveyDetails(survey: survey, title: 'Foo')
        survey.details = details
        surveyRepository.update(survey)

        def q1 = new Question(survey: survey, kind: LongFreeText,  title: "First one")
        def q2 = new Question(survey: survey, kind: LongFreeText,  title: "Second one")
        def q3 = new Question(survey: survey, kind: ListRadio,     title: "With options")

        (q1, q2, q3) = [q1, q2, q3].each {questionRepository.save it}

        survey.questions.addAll([q1, q2, q3])
        surveyRepository.update(survey)

        [
            new QuestionOption(code: "O1", answer: "Option 1"),
            new QuestionOption(code: "O2", answer: "Option 2"),
            new QuestionOption(code: "O3", answer: "Option 3")
        ].each {q3.options.add it; it.id = q3.id; it.question = q3; questionOptionRepository.save it}

        assertThat surveyRepository.load(1).questions, hasSize(3)
        assertThat questionRepository.list(), hasSize(3)
        assertThat questionRepository.load(3).options, hasSize(3)
    }

    void testQuestionOrdering() {
        def survey =
                new Survey(startDate: new Date(), endDate: new Date())
        def sid = surveyRepository.save survey

        def loadSurvey = {surveyRepository.load sid}

        def newQuestion = {title -> new Question(survey: survey, kind: LongFreeText, title: title)}

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
}
