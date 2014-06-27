package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyStats

import static org.junit.Assert.assertEquals

@Mixin(RepositoryMock)
class AccessNumberRepositoryTest extends DbTestCase {

    void setUp() {
        super.setUp()
        initRepository(db)
    }

    def assertIds = { expected, numbers -> assertEquals(expected, numbers.collect { it.id }) }

    void fillTestData() {
        AccessNumber number1, number2, number3, number4

        [
                number1 = new AccessNumber(number: "79130000011"),
                number2 = new AccessNumber(number: "79130000112"),
                number3 = new AccessNumber(number: "79130000013"),
                number4 = new AccessNumber(number: "79130000014"),
        ].each { an ->
            accessNumberRepository.save(an)
        }

        [
                new Survey(id: 1).with {
                    statistics = new SurveyStats(survey: it, accessNumber: number1)
                    it
                },
                new Survey(id: 2).with {
                    statistics = new SurveyStats(survey: it, accessNumber: number2)
                    it
                },
                new Survey(id: 3).with {
                    statistics = new SurveyStats(survey: it, accessNumber: number3)
                    it
                },
                new Survey(id: 4).with {
                    statistics = new SurveyStats(survey: it, accessNumber: number4)
                    it
                },
        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save(s)
        }
    }

    void testFind() {
        fillTestData()

        assertEquals(1, accessNumberRepository.find("79130000011").id)
    }

    void testCount() {
        fillTestData()

        assertEquals(2, accessNumberRepository.count("11"))
    }

    void testList() {
        fillTestData()

        assertIds([1,2], accessNumberRepository.list("11",null,true,Integer.MAX_VALUE,0))
        assertIds([2,1], accessNumberRepository.list("11","number",false,Integer.MAX_VALUE,0))
    }
}
