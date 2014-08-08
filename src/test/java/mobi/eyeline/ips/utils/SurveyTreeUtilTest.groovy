package mobi.eyeline.ips.utils

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.util.SurveyTreeUtil

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace

@Mixin(TreeBuilder)
class SurveyTreeUtilTest extends GroovyTestCase {

    void setUp() {
        super.setUp()
        //noinspection UnnecessaryQualifiedReference
        TreeBuilder.init()
    }

    def newQ = {id -> new Question(id: id)}
    def newO = {id, next -> new QuestionOption(id: id, nextQuestion: next)}

    @SuppressWarnings("GrMethodMayBeStatic")
    private Survey newSurvey(List<Question> questions) {
        new Survey(questions: questions).with { s ->
            questions.each { q -> q.survey = s; q.options.each { it.question = q } }
            //noinspection GroovyAccessibility
            prepareIndex()
            s
        }
    }

    void test1() {
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

        def tree = SurveyTreeUtil.asTree(survey, '', '')
        assertThat tree.describe(), equalToIgnoringWhiteSpace('''
            Root: [0]

            [0] --0--> [1]
            [0] --1--> [1]

            [1] --0--> [2]
            [1] --1--> [2]

            [2] --0--> [3]
            [2] --1--> [3]

            [3] --0--> [-1]
            [3] --1--> [-1]
            ''')
    }

    void testLoop() {
        def questions = (0..<2).collect { newQ(it) }

        questions[0].options =
                [newO(0, questions[1]), newO(1, questions[1]), newO(2, null), newO(3, questions[0])]
        questions[1].options = [newO(0, questions[0]), newO(1, questions[1]), newO(2, null)]

        def tree = SurveyTreeUtil.asTree(newSurvey(questions), '', '')
        assertThat tree.describe(), equalToIgnoringWhiteSpace('''
            Root: [0]

            [0] --0--> [1]
            [0] --1--> [1]
            [0] --2--> [-1]
            [0] --3--> [0]

            [1] --0--> [0]
            [1] --1--> [1]
            [1] --2--> [-1]
            ''')
    }
}
