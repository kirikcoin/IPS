package mobi.eyeline.ips.service

import mobi.eyeline.ips.model.AccessNumber
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

@Mixin([TreeBuilder, RepositoryMock])
class SurveyServiceTest extends DbTestCase {

  SurveyService surveyService

  def newQ = { id -> new Question(id: id) }
  def newO = { id, next -> new QuestionOption(id: id, nextPage: next) }

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
        extLinkPageRepository,
        questionOptionRepository,
        surveyInvitationRepository,
        invitationDeliveryRepository,
        accessNumberRepository)
  }

  void testFindSurvey1() {
    assertNull surveyService.findSurvey(2, true)
    assertNull surveyService.findSurvey(2, false)
  }

  void testDeleteQuestion1() {
    def survey = survey([:]) {
      pages {
        question(id: 0) {
          option(id: 0, nextPage: ref(id: 1))
          option(id: 1, nextPage: ref(id: 1))
        }
        question(id: 1) {
          option(id: 0, nextPage: ref(id: 2))
          option(id: 1, nextPage: ref(id: 2))
        }
        question(id: 2) {
          option(id: 0, nextPage: ref(id: 3))
          option(id: 1, nextPage: ref(id: 3))
        }
        question(id: 3) {
          option(id: 0, nextPage: null)
          option(id: 1, nextPage: null)
        }
      }
    }

    surveyService.deleteQuestion(survey.questions[1])

    def tree = SurveyTreeUtil.asTree(survey, '', '', '', '', '', '', '')
    assertThat tree.describe(), equalToIgnoringWhiteSpace('''
            Root: [0]

            [0] --0--> [-1]
            [0] --1--> [-1]

            [-1] --2--> [2]
            [-1] --3--> [3]
            ''')
  }

  void 'test access numbers are freed after survey deletion'() {
    // 1. Survey lies in the DB and has no numbers.
    def survey =
        survey(id: 1, startDate: new Date(), endDate: new Date()) {
          statistics([:])
        }.with {
          surveyRepository.save it
          surveyRepository.get(1)
        }

    // 2. Persist a few (yet) unbound access numbers.
    final numbers = [
        new AccessNumber(id: 1, number: '79130000011'),
        new AccessNumber(id: 2, number: '79130000112'),
        new AccessNumber(id: 3, number: '79130000013'),
        new AccessNumber(id: 4, number: '79130000014')
    ].collect { an -> accessNumberRepository.save(an); an }

    // 3. Bind `1' and `2' to the survey
    numbers[0].surveyStats = survey.statistics
    accessNumberRepository.update numbers[0]

    numbers[1].surveyStats = survey.statistics
    accessNumberRepository.update numbers[1]

    // 3.1. Okay, those are actually bound
    assertEquals 1, accessNumberRepository.get(1).surveyStats.id
    assertEquals 1, accessNumberRepository.get(2).surveyStats.id

    // 3.2. Others are not
    assertNull accessNumberRepository.get(3).surveyStats
    assertNull accessNumberRepository.get(4).surveyStats

    // 3.3. And we can see those number From the associated survey mapping.
    assertEquals([1, 2], surveyRepository.get(1).statistics.accessNumbers.collect { it.id })

    // 4. Delete the survey
    surveyService.delete(surveyRepository.get(1))

    // 4.1. As a result, access numbers are still alive but become unbound.
    assertNull accessNumberRepository.get(1).surveyStats
    assertNull accessNumberRepository.get(2).surveyStats
  }
}
