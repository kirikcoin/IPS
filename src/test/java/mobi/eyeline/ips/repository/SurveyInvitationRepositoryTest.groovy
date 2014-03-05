package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyInvitation

import static org.junit.Assert.assertEquals

class SurveyInvitationRepositoryTest extends DbTestCase {
    private SurveyRepository surveyRepository
    private SurveyInvitationRepository surveyInvitationRepository

    Survey survey1, survey2, survey3, survey4

    void setUp() {
        super.setUp()
        surveyRepository = new SurveyRepository(db)
        surveyInvitationRepository = new SurveyInvitationRepository(db)
    }

    def assertIds =
            { expected, invitations -> assertEquals(expected, invitations.collect { it.id }) }

    void testList1() {
        fillTestData()

        assertIds([6, 5],
                surveyInvitationRepository.list(survey3, 'value', false, Integer.MAX_VALUE, 0))
        assertIds([4, 3, 2],
                surveyInvitationRepository.list(survey2, 'value', false, Integer.MAX_VALUE, 0))
    }

    void testList2() {
        fillTestData()

        assertIds([5,6], surveyInvitationRepository.list(survey3))
    }

    void testCount() {
        fillTestData()
        assertEquals(3, surveyInvitationRepository.count(survey2))
    }

    private void fillTestData() {
        [
            survey1 = new Survey(id: 1),
            survey2 = new Survey(id: 2),
            survey3 = new Survey(id: 3),
            survey4 = new Survey(id: 4, active: false)
        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save s
        }

        [
            new SurveyInvitation(id: 1, survey: survey1, date: new Date(), value: 1),
            new SurveyInvitation(id: 2, survey: survey2, date: new Date(), value: 2),
            new SurveyInvitation(id: 3, survey: survey2, date: new Date(), value: 3),
            new SurveyInvitation(id: 4, survey: survey2, date: new Date(), value: 4),
            new SurveyInvitation(id: 5, survey: survey3, date: new Date(), value: 5),
            new SurveyInvitation(id: 6, survey: survey3, date: new Date(), value: 6)
        ].each {s -> surveyInvitationRepository.save s}
    }
}
