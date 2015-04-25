package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.service.deliveries.DeliveryWrapper

import static com.google.common.collect.Lists.transform
import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED
import static mobi.eyeline.ips.model.DeliverySubscriber.State.NEW
import static mobi.eyeline.ips.model.DeliverySubscriber.State.SENT
import static mobi.eyeline.ips.model.InvitationDelivery.State.ACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.Type.NI_DIALOG
import static mobi.eyeline.ips.service.deliveries.DeliveryWrapper.Message.AS_PAIR

@Mixin(RepositoryMock)
class DeliverySubscriberRepositoryTest extends DbTestCase {

  List messageList

  void setUp() {
    db = new DB(new Properties())

    initRepository(db)
  }

  void tearDown() {
    db.sessionFactory.close()
  }

  void fillTestData() {

    def msisdns = ['79130000001', '79130000002']

    Survey survey = new Survey(id: 1, startDate: new Date(), endDate: new Date() + 1)
    surveyRepository.save(survey)

    InvitationDelivery delivery = new InvitationDelivery(
        id: 1,
        survey: survey,
        date: new Date(),
        type: NI_DIALOG,
        state: ACTIVE,
        speed: 1,
        inputFile: 'txt.txt')

    invitationDeliveryRepository.saveWithSubscribers(delivery, msisdns)

    messageList = [
        new DeliveryWrapper.Message(1, msisdns[0] as String).setState(NEW),
        new DeliveryWrapper.Message(2, msisdns[1] as String).setState(DELIVERED),
    ]
  }

  void testUpdateState1() {
    fillTestData()

    deliverySubscriberRepository.updateState(transform(messageList, AS_PAIR))
    assertEquals NEW, deliverySubscriberRepository.get(1).state
    assertEquals DELIVERED, deliverySubscriberRepository.get(2).state
  }

  void testUpdateState2() {
    fillTestData()

    deliverySubscriberRepository.updateState(transform(messageList, AS_PAIR), SENT)
    assertEquals NEW, deliverySubscriberRepository.get(1).state
    assertEquals NEW, deliverySubscriberRepository.get(2).state

    deliverySubscriberRepository.updateState(transform(messageList, AS_PAIR), NEW)
    assertEquals NEW, deliverySubscriberRepository.get(1).state
    assertEquals DELIVERED, deliverySubscriberRepository.get(2).state
  }
}
