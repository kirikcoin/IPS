package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.Respondent

import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.*

class RespondentRepositoryTest extends DbTestCase {
    private RespondentRepository respondentService

    void setUp() {
        super.setUp()
        respondentService = new RespondentRepository(db)
    }

    void testIsRegistered() {
        fillTestData()

        assertTrue respondentService.isRegisteredForSurvey("79130000001", 1)
        assertFalse respondentService.isRegisteredForSurvey("79130000002", 2)
        assertFalse respondentService.isRegisteredForSurvey("79130000001", 2)
    }

    void testListBySurvey() {
        fillTestData()

        assertThat respondentService.listBySurvey(1), hasSize(2)
        assertThat respondentService.listBySurvey(2), hasSize(1)
        assertThat respondentService.listBySurvey(3), empty()
    }

    void testFindBySurveyAndPhone() {
        fillTestData()

        assertNotNull respondentService.findBySurveyAndPhone("79130000001", 1)
        assertNull respondentService.findBySurveyAndPhone("79130000001", 4)
        assertNull respondentService.findBySurveyAndPhone("79130000004", 2)
    }

    void testUpdateAnswerCount() {
        fillTestData()

        // Ensure the the flag gets the value
        respondentService.updateAnswerCount("79130000001", 1)

        def testRespondent =
                respondentService.findBySurveyAndPhone("79130000001", 1)
        assertTrue testRespondent.answered

        // Ensure that subsequent calls have no effect
        respondentService.updateAnswerCount("79130000001", 1)

        testRespondent = respondentService.findBySurveyAndPhone("79130000001", 1)
        assertTrue testRespondent.answered
    }

    private void fillTestData() {
        [
                new Respondent(msisdn: "79130000001", sid: 1, aid: 1),
                new Respondent(msisdn: "79130000002", sid: 1, aid: 2),
                new Respondent(msisdn: "79130000003", sid: 2, aid: 3)
        ].each {r -> respondentService.save r}
    }

}
