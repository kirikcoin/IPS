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
          statistics([:])
        },

        survey(id: 2) {
          statistics([:])
        },

        survey(id: 3) {
          statistics([:])
        },

        survey(id: 4) {
          statistics([:])
        }

    ].each { s ->
      s.startDate = new Date()
      s.endDate = new Date()
      surveyRepository.save(s)
    }

    numbers.eachWithIndex { num, i ->
      num.surveyStats = surveyRepository.get(i + 1).statistics
      accessNumberRepository.update(num)
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
