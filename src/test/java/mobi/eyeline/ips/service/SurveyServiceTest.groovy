package mobi.eyeline.ips.service

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.QuestionOptionRepository
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.RepositoryMock
import mobi.eyeline.ips.util.SurveyTreeUtil
import mobi.eyeline.ips.utils.TreeBuilder

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace

@Mixin([RepositoryMock, TreeBuilder])
class SurveyServiceTest extends DbTestCase {

    SurveyService surveyService

    def newQ = {id -> new Question(id: id)}
    def newO = {id, next -> new QuestionOption(id: id, nextQuestion: next)}

    void setUp() {
        super.setUp()

        //noinspection UnnecessaryQualifiedReference
        TreeBuilder.init()
        initRepository(db)

        questionOptionRepository = new QuestionOptionRepository(db) {
            @Override void update(QuestionOption _) {}
            @Override Class<QuestionOption> getEntityClass() { QuestionOption }
        }

        questionRepository = new QuestionRepository(db) {
            @Override void update(Question _) {}
            @Override Class<Question> getEntityClass() { Question }
        }

        surveyService = new SurveyService(
                surveyRepository,
                questionRepository,
                questionOptionRepository,
                surveyInvitationRepository,
                invitationDeliveryRepository)
    }

    void testFindSurvey1() {
        assertNull surveyService.findSurvey(2, true)
        assertNull surveyService.findSurvey(2, false)
    }

    void testDeleteQuestion1() {
        def questions = (0..<4).collect { newQ(it) }

        questions[0].options = [newO(0, questions[1]), newO(1, questions[1])]
        questions[1].options = [newO(0, questions[2]), newO(1, questions[2])]
        questions[2].options = [newO(0, questions[3]), newO(1, questions[3])]
        questions[3].options = [newO(0, null), newO(1, null)]

        def survey = new Survey(questions: questions).with { s ->
            questions.each { q-> q.survey = s; q.options.each { it.question = q } }
            //noinspection GroovyAccessibility
            prepareIndex()
            s
        }

        surveyService.deleteQuestion(questions[1])

        def tree = SurveyTreeUtil.asTree(survey, '', '', '')
        assertThat tree.describe(), equalToIgnoringWhiteSpace('''
            Root: [0]

            [0] --0--> [-1]
            [0] --1--> [-1]

            [-1] ---2--> [2]
            [-1] ---3--> [3]
            ''')
    }
}
