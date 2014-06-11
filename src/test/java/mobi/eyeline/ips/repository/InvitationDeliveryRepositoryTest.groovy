package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.InvitationDelivery

import static mobi.eyeline.ips.model.InvitationDelivery.State.*

@Mixin(InvitationDeliveryRepositoryTestBase)
class InvitationDeliveryRepositoryTest extends DbTestCase {

    void setUp() {
        db = new DB(new Properties())

        initServices(db)
    }

    @Override
    void tearDown() {
        // Avoid committing transaction in super()
        db.sessionFactory.close()
    }

    void testSave() {
        fillTestData()
        invitationDeliveryRepository.saveWithSubscribers(deliveries[0], msisdns)
        def subscriber = deliverySubscriberRepository.get(1)
        assertEquals([deliveries[0].id, '79130000001'], [subscriber.invitationDelivery.id, subscriber.msisdn])
    }

    void testList2() {
        fillTestData()
        assertIds([1, 5, 7], invitationDeliveryRepository.list(ACTIVE))
        assertIds([4, 6], invitationDeliveryRepository.list(INACTIVE))
        assertIds([2, 3], invitationDeliveryRepository.list(COMPLETED))
    }

    void testFetchNext() {
        fillTestData()
        deliveries.each { InvitationDelivery s -> invitationDeliveryRepository.saveWithSubscribers(s, msisdns) }

        def chunk = invitationDeliveryRepository.fetchNext(deliveries[0], 5)
        assertEquals(msisdns[0..4], chunk*.msisdn)
    }

}
