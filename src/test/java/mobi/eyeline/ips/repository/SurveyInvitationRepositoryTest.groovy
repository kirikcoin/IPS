package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyInvitation

import static mobi.eyeline.ips.utils.SurveyBuilder.surveys
import static org.junit.Assert.assertEquals

@Mixin(RepositoryMock)
class SurveyInvitationRepositoryTest extends DbTestCase {

  List<Survey> persistedSurveys

  void setUp() {
    super.setUp()

    initRepository(db)
  }

  def assertIds =
      { expected, invitations -> assertEquals(expected, invitations.collect { it.id }) }

  void testList1() {
    fillTestData()

    assertIds([6, 5],
        surveyInvitationRepository.list(persistedSurveys[2], 'value', false, Integer.MAX_VALUE, 0))
    assertIds([4, 3, 2],
        surveyInvitationRepository.list(persistedSurveys[1], 'value', false, Integer.MAX_VALUE, 0))
  }

  void testList2() {
    fillTestData()

    assertIds([5, 6], surveyInvitationRepository.list(persistedSurveys[2]))
  }

  void testCount() {
    fillTestData()
    assertEquals(3, surveyInvitationRepository.count(persistedSurveys[1]))
  }

  private void fillTestData() {
    List<SurveyInvitation> invitations = []

    persistedSurveys = surveys(startDate: new Date(), endDate: new Date()) {
      survey(id: 1) {
        invitations << invitation(id: 1, date: new Date(), value: 1)
      }

      survey(id: 2) {
        invitations << invitation(id: 2, date: new Date(), value: 2)
        invitations << invitation(id: 3, date: new Date(), value: 3)
        invitations << invitation(id: 4, date: new Date(), value: 4)
      }

      survey(id: 3) {
        invitations << invitation(id: 5, date: new Date(), value: 5)
        invitations << invitation(id: 6, date: new Date(), value: 6)
      }

      survey(id: 4, active: false) {}
    }.collect { surveyRepository.save it; it }

    invitations.each { surveyInvitationRepository.save it }
  }
}
