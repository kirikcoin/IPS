package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.AccessNumber

import static mobi.eyeline.ips.utils.SurveyBuilder.survey
import static org.junit.Assert.assertEquals

@Mixin(RepositoryMock)
class AccessNumberRepositoryTest extends DbTestCase {

    void setUp() {
        super.setUp()
        initRepository(db)
    }

    def assertIds = { expected, numbers -> assertEquals(expected, numbers.collect { it.id }) }

    void fillTestData() {
        def numbers = [
                new AccessNumber(number: '79130000011'),
                new AccessNumber(number: '79130000112'),
                new AccessNumber(number: '79130000013'),
                new AccessNumber(number: '79130000014')
        ].collect { an -> accessNumberRepository.save(an); an }

        [
                survey(id: 1) {
                    statistics(accessNumber: numbers[0])
                },

                survey(id: 2) {
                    statistics(accessNumber: numbers[1])
                },

                survey(id: 3) {
                    statistics(accessNumber: numbers[2])
                },

                survey(id: 4) {
                    statistics(accessNumber: numbers[3])
                }

        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save(s)
        }
    }

    void testFind() {
        fillTestData()

        assertEquals(1, accessNumberRepository.find('79130000011').id)
    }

    void testCount() {
        fillTestData()

        assertEquals(2, accessNumberRepository.count('11'))
    }

    void testList() {
        fillTestData()

        assertIds([1, 2], accessNumberRepository.list('11', null, true, Integer.MAX_VALUE, 0))
        assertIds([2, 1], accessNumberRepository.list('11', 'number', false, Integer.MAX_VALUE, 0))
    }
}
