package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.DeliverySubscriber
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.service.CsvParseService

import static mobi.eyeline.ips.model.InvitationDelivery.State.ACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.State.COMPLETED
import static mobi.eyeline.ips.model.InvitationDelivery.State.INACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.Type.NI_DIALOG
import static mobi.eyeline.ips.model.InvitationDelivery.Type.USSD_PUSH

class InvitationDeliveryRepositoryTest extends DbTestCase {
    private InvitationDeliveryRepository invitationDeliveryRepository
    private DeliverySubscriberRepository deliverySubscriberRepository
    private SurveyRepository surveyRepository
    private CsvParseService csvParseService

    Survey survey1, survey2, survey3, survey4
    List<InvitationDelivery> deliveries

    List msisdns

    void setUp() {
        db = new DB(new Properties())

        invitationDeliveryRepository = new InvitationDeliveryRepository(db)
        deliverySubscriberRepository = new DeliverySubscriberRepository(db)
        csvParseService = new CsvParseService()
        surveyRepository = new SurveyRepository(db)
    }

    @Override
    void tearDown() {
        // Avoid committing transaction in super()
        db.sessionFactory.close()
    }
    def assertIds = { expected, deliveries -> assertEquals(expected, deliveries*.id) }

    private List<String> getLines(String input) {
        csvParseService.parseFile new ByteArrayInputStream(input.getBytes('UTF-8'))
    }

    void fillTestData() {

        msisdns = getLines """
            79130000001
            79130000002
            79130000003
            79130000004
            79130000005
            79130000006
            79130000007
            79130000008
            79130000009
            79130000011
            79130000012
            79130000013
            79130000014
            79130000015
            79130000016
            79130000017
            79130000018
            79130000019
            79130000020"""

        [
                survey1 = new Survey(id: 1),
                survey2 = new Survey(id: 2),
                survey3 = new Survey(id: 3),
                survey4 = new Survey(id: 4),

        ].each { s ->
            s.startDate = new Date()
            s.endDate = new Date()
            surveyRepository.save(s)
        }

        deliveries = new ArrayList<InvitationDelivery>()
        [
                new InvitationDelivery(
                        id: 1, survey: survey1, date: new Date(), type: NI_DIALOG, state: ACTIVE, speed: 1, inputFile: "txt.txt"),
                new InvitationDelivery(
                        id: 2, survey: survey2, date: new Date() + 1, type: USSD_PUSH, state: COMPLETED, speed: 1, inputFile: "txt.txt"),
                new InvitationDelivery(
                        id: 3, survey: survey3, date: new Date() + 2, type: NI_DIALOG, state: COMPLETED, speed: 1, inputFile: "txt.txt"),
                new InvitationDelivery(
                        id: 4, survey: survey4, date: new Date() + 3, type: NI_DIALOG, state: INACTIVE, speed: 1, inputFile: "txt.txt"),
                new InvitationDelivery(
                        id: 5, survey: survey1, date: new Date() + 5, type: USSD_PUSH, state: ACTIVE, speed: 1, inputFile: "txt.txt"),
                new InvitationDelivery(
                        id: 6, survey: survey3, date: new Date() + 4, type: NI_DIALOG, state: INACTIVE, speed: 1, inputFile: "txt.txt"),
                new InvitationDelivery(
                        id: 7, survey: survey3, date: new Date() + 6, type: NI_DIALOG, state: ACTIVE, speed: 1, inputFile: "txt.txt"),
        ].each {InvitationDelivery s ->
            deliveries.add(s)
            invitationDeliveryRepository.save(s)
        }
    }

    void testSave(){
        fillTestData()
        invitationDeliveryRepository.saveWithSubscribers(deliveries[0], msisdns)
        def subscriber = deliverySubscriberRepository.get(1)
        assertEquals([deliveries[0].id,'79130000001'],[subscriber.invitationDelivery.id, subscriber.msisdn])

    }

    void testList() {
        fillTestData()
        assertIds([1, 5], invitationDeliveryRepository.list(survey1, null, false, Integer.MAX_VALUE, 0))
        assertIds([5, 1], invitationDeliveryRepository.list(survey1, 'type', false, Integer.MAX_VALUE, 0))
        assertIds([3, 6, 7], invitationDeliveryRepository.list(survey3, 'date', true, Integer.MAX_VALUE, 0))
    }

    void testList2(){
        fillTestData()
        assertIds([1, 5, 7], invitationDeliveryRepository.list(ACTIVE))
        assertIds([4, 6], invitationDeliveryRepository.list(INACTIVE))
        assertIds([2, 3], invitationDeliveryRepository.list(COMPLETED))
    }

    void testCount(){
        fillTestData()
        assertEquals(2,invitationDeliveryRepository.count(survey1))
        assertEquals(3,invitationDeliveryRepository.count(survey3))
    }

    void testFetchNext(){
        fillTestData()
        deliveries.each {InvitationDelivery s-> invitationDeliveryRepository.saveWithSubscribers(s, msisdns)}
        ArrayList<DeliverySubscriber> list1 = invitationDeliveryRepository.fetchNext(deliveries[0], 5)
        assertEquals (msisdns[0..4], list1*.msisdn)
    }


}
