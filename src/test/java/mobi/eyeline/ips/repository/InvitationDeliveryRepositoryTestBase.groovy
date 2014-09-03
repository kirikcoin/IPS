package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.service.CsvParseService

import static mobi.eyeline.ips.model.InvitationDelivery.State.*
import static mobi.eyeline.ips.model.InvitationDelivery.Type.NI_DIALOG
import static mobi.eyeline.ips.model.InvitationDelivery.Type.USSD_PUSH
import static mobi.eyeline.ips.utils.ModelBuilder.survey

class InvitationDeliveryRepositoryTestBase {

    InvitationDeliveryRepository invitationDeliveryRepository
    DeliverySubscriberRepository deliverySubscriberRepository
    SurveyRepository surveyRepository
    CsvParseService csvParseService

    Survey survey1, survey2, survey3, survey4
    List<InvitationDelivery> deliveries

    List msisdns

    @SuppressWarnings('GrMethodMayBeStatic')
    void assertIds(List expected, List deliveries) {
        GroovyTestCase.assertEquals(expected, deliveries*.id) 
    }

    void initServices(db) {
        invitationDeliveryRepository = new InvitationDeliveryRepository(db)
        deliverySubscriberRepository = new DeliverySubscriberRepository(db)
        csvParseService = new CsvParseService()
        surveyRepository = new SurveyRepository(db)
    }

    List<String> getLines(String input) {
        csvParseService.parseFile new ByteArrayInputStream(input.getBytes('UTF-8'))
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
                survey1 = survey(id: 1),
                survey2 = survey(id: 2),
                survey3 = survey(id: 3),
                survey4 = survey(id: 4),

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
    }
}
