package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Respondent
import mobi.eyeline.ips.model.Survey

import static mobi.eyeline.ips.utils.SurveyBuilder.survey

@Mixin(RepositoryMock)
class RespondentRepositoryTest extends DbTestCase {

  Survey survey1, survey2, survey3

  void setUp() {
    super.setUp()

    initRepository(db)

    fillTestData()
  }

  void testCountBySurvey() {
    assertEquals 1, respondentRepository.countBySurvey(survey1)
    assertEquals 2, respondentRepository.countBySurvey(survey2)
    assertEquals 3, respondentRepository.countBySurvey(survey3)
  }

  void testCountFinishedBySurvey() {
    assertEquals 0, respondentRepository.countFinishedBySurvey(survey1)
    assertEquals 1, respondentRepository.countFinishedBySurvey(survey2)
    assertEquals 3, respondentRepository.countFinishedBySurvey(survey3)

  }

  void testFindOrCreate() {
    respondentRepository.findOrCreate('79130000001', survey1, null)
    assertEquals 6, respondentRepository.list().size()

    respondentRepository.findOrCreate('79130000001', survey1, '123')
    assertEquals 7, respondentRepository.list().size()

    respondentRepository.findOrCreate('79130000002', survey1, null)
    assertEquals 8, respondentRepository.list().size()
  }

  private void fillTestData() {

    survey1 =
        survey(startDate: new Date(), endDate: new Date())
    survey2 =
        survey(startDate: new Date(), endDate: new Date())
    survey3 =
        survey(startDate: new Date(), endDate: new Date())

    [survey1, survey2, survey3].each { s -> surveyRepository.save s }

    [
        new Respondent(id: 1, msisdn: "79130000001", survey: survey1, finished: false),
        new Respondent(id: 2, msisdn: "79130000002", survey: survey2, finished: true),
        new Respondent(id: 3, msisdn: "79130000004", survey: survey2, finished: false),
        new Respondent(id: 4, msisdn: "79130000005", survey: survey3, finished: true),
        new Respondent(id: 5, msisdn: "79130000006", survey: survey3, finished: true),
        new Respondent(id: 6, msisdn: "79130000007", survey: survey3, finished: true)
    ].each { r -> respondentRepository.save r }
  }

}
