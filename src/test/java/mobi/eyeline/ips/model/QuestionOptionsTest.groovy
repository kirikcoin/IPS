package mobi.eyeline.ips.model

import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RepositoryMock

import static mobi.eyeline.ips.utils.SurveyBuilder.survey

@Mixin(RepositoryMock)
class QuestionOptionsTest extends DbTestCase {

  @Override
  void setUp() {
    super.setUp()

    initRepository(db)
  }

  void test1() {
    final s = survey(startDate: new Date(), endDate: new Date()) {
      details(title: 'Foo')

      questions {
        question(title: 'First one') {
          option(answer: 'O1')
        }
      }
    }

    surveyRepository.save s
  }
}
