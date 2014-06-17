package mobi.eyeline.ips.repository

import com.google.common.base.Function
import mobi.eyeline.ips.model.DeliverySubscriber
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.service.CsvParseService
import mobi.eyeline.ips.service.deliveries.DeliveryWrapper
import org.apache.commons.lang3.tuple.Pair

import static com.google.common.collect.Lists.transform
import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED
import static mobi.eyeline.ips.model.DeliverySubscriber.State.NEW
import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT
import static mobi.eyeline.ips.model.InvitationDelivery.State.ACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.State.COMPLETED
import static mobi.eyeline.ips.model.InvitationDelivery.State.INACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.Type.NI_DIALOG
import static mobi.eyeline.ips.model.InvitationDelivery.Type.USSD_PUSH

class DeliverySubscriberRepositoryTest extends DbTestCase {

    InvitationDeliveryRepository invitationDeliveryRepository
    DeliverySubscriberRepository deliverySubscriberRepository
    SurveyRepository surveyRepository
    CsvParseService csvParseService
    Survey survey1, survey2, survey3, survey4
    List<InvitationDelivery> deliveries


    List msisdns

    List messageList
    List<Pair<Integer, DeliverySubscriber.State>> idsAndStates

    void setUp() {
      //  super.setUp()
        db = new DB(new Properties())
        invitationDeliveryRepository = new InvitationDeliveryRepository(db)
        deliverySubscriberRepository = new DeliverySubscriberRepository(db)
        csvParseService = new CsvParseService()
        surveyRepository = new SurveyRepository(db)
    }
    @Override
    void tearDown() {
        db.sessionFactory.close()
    }

    List<String> getLines(String input) {
        csvParseService.parseFile new ByteArrayInputStream(input.getBytes('UTF-8'))
    }

    private final Function<DeliveryWrapper.Message, Pair<Integer, DeliverySubscriber.State>> asPair =
            new Function<DeliveryWrapper.Message, Pair<Integer, DeliverySubscriber.State>>() {
                @Override
                public Pair<Integer, DeliverySubscriber.State> apply(DeliveryWrapper.Message input) {
                    return Pair.of(input.getId(), input.getState())
                }
            }


    void fillTestData() {

        msisdns = getLines '''
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
            79130000020'''

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
                        id: 1, survey: survey1, date: new Date(), type: NI_DIALOG, state: ACTIVE, speed: 1, inputFile: 'txt.txt'),
                new InvitationDelivery(
                        id: 2, survey: survey2, date: new Date() + 1, type: USSD_PUSH, state: COMPLETED, speed: 1, inputFile: 'txt.txt'),
                new InvitationDelivery(
                        id: 3, survey: survey3, date: new Date() + 2, type: NI_DIALOG, state: COMPLETED, speed: 1, inputFile: 'txt.txt'),
                new InvitationDelivery(
                        id: 4, survey: survey4, date: new Date() + 3, type: NI_DIALOG, state: INACTIVE, speed: 1, inputFile: 'txt.txt'),
                new InvitationDelivery(
                        id: 5, survey: survey1, date: new Date() + 5, type: USSD_PUSH, state: ACTIVE, speed: 1, inputFile: 'txt.txt'),
                new InvitationDelivery(
                        id: 6, survey: survey3, date: new Date() + 4, type: NI_DIALOG, state: INACTIVE, speed: 1, inputFile: 'txt.txt'),
                new InvitationDelivery(
                        id: 7, survey: survey3, date: new Date() + 6, type: NI_DIALOG, state: ACTIVE, speed: 1, inputFile: 'txt.txt'),
        ].each {InvitationDelivery s ->
            deliveries.add(s)
            invitationDeliveryRepository.save(s)
        }

        invitationDeliveryRepository.saveWithSubscribers(deliveries[0], msisdns)

        messageList = new ArrayList<DeliveryWrapper.Message>()
        [
                new DeliveryWrapper.Message(1, msisdns[0] as String).setState(NEW),
                new DeliveryWrapper.Message(2, msisdns[1] as String).setState(DELIVERED),

        ].each { DeliveryWrapper.Message  m ->
            messageList.add(m)
        }

    }

    void testUpdateState1() {
        fillTestData()

        deliverySubscriberRepository.updateState(
                transform(messageList, asPair));
        assertEquals(NEW,deliverySubscriberRepository.get(1).state)
        assertEquals(DELIVERED,deliverySubscriberRepository.get(2).state)
    }

    void testUpdateState2() {
        fillTestData()

        deliverySubscriberRepository.updateState(
                transform(messageList, asPair),
                DeliverySubscriber.State.SENT);
        assertEquals(NEW,deliverySubscriberRepository.get(1).state)
        assertEquals(NEW,deliverySubscriberRepository.get(2).state)

        deliverySubscriberRepository.updateState(
                transform(messageList, asPair),
                DeliverySubscriber.State.NEW);

        assertEquals(NEW,deliverySubscriberRepository.get(1).state)
        assertEquals(DELIVERED,deliverySubscriberRepository.get(2).state)
    }


}
