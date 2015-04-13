package mobi.eyeline.ips.service

import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.QuestionOptionRepository
import mobi.eyeline.ips.repository.QuestionRepository
import mobi.eyeline.ips.repository.RepositoryMock
import mobi.eyeline.ips.util.SurveyTreeUtil
import mobi.eyeline.ips.utils.TreeBuilder

import static mobi.eyeline.ips.utils.SurveyBuilder.survey
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace

@Mixin([RepositoryMock, TreeBuilder])
class SurveyServiceTest extends DbTestCase {

  SurveyService surveyService

  def newQ = { id -> new Question(id: id) }
  def newO = { id, next -> new QuestionOption(id: id, nextQuestion: next) }

  void setUp() {
    super.setUp()

    //noinspection UnnecessaryQualifiedReference
    TreeBuilder.init()
    initRepository(db)

    questionOptionRepository = new QuestionOptionRepository(db) {
      @Override
      void update(QuestionOption _) {}

      @Override
      Class<QuestionOption> getEntityClass() { QuestionOption }
    }

    questionRepository = new QuestionRepository(db) {
      @Override
      void update(Question _) {}

      @Override
      Class<Question> getEntityClass() { Question }
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
    def survey = survey([:]) {
      questions {
        question(id: 0) {
          option(id: 0, nextQuestion: ref(id: 1))
          option(id: 1, nextQuestion: ref(id: 1))
        }
        question(id: 1) {
          option(id: 0, nextQuestion: ref(id: 2))
          option(id: 1, nextQuestion: ref(id: 2))
        }
        question(id: 2) {
          option(id: 0, nextQuestion: ref(id: 3))
          option(id: 1, nextQuestion: ref(id: 3))
        }
        question(id: 3) {
          option(id: 0, nextQuestion: null)
          option(id: 1, nextQuestion: null)
        }
      }
    }

    surveyService.deleteQuestion(survey.questions[1])

    def tree = SurveyTreeUtil.asTree(survey, '', '', '', '', '')
    assertThat tree.describe(), equalToIgnoringWhiteSpace('''
            Root: [0]

            [0] --0--> [-1]
            [0] --1--> [-1]

            [-1] --2--> [2]
            [-1] --3--> [3]
            ''')
  }
}
