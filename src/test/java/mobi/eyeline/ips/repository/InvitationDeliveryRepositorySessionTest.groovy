package mobi.eyeline.ips.repository

@Mixin(InvitationDeliveryRepositoryTestBase)
class InvitationDeliveryRepositorySessionTest extends DbTestCase {

  void setUp() {
    super.setUp()
    init(db)
  }

  void testList() {
    fillTestData()
    assertIds([1, 5], invitationDeliveryRepository.list(survey1, null, false, Integer.MAX_VALUE, 0))
    assertIds([5, 1], invitationDeliveryRepository.list(survey1, 'type', false, Integer.MAX_VALUE, 0))
    assertIds([3, 6, 7], invitationDeliveryRepository.list(survey3, 'date', true, Integer.MAX_VALUE, 0))
  }

  void testCount() {
    fillTestData()
    assertEquals(2, invitationDeliveryRepository.count(survey1))
    assertEquals(3, invitationDeliveryRepository.count(survey3))
  }

}
