package mobi.eyeline.ips.service

import mobi.eyeline.ips.external.esdp.EsdpServiceManager
import mobi.eyeline.ips.external.esdp.Service
import mobi.eyeline.ips.model.*
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.properties.DefaultMockConfig
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RepositoryMock

import static mobi.eyeline.ips.utils.SurveyBuilder.survey
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

@Mixin(RepositoryMock)
class EsdpServiceTest extends DbTestCase {

  UssdService ussdService

  MockEsdpServiceManager serviceManager
  EsdpService esdpService

  void setUp() {
    super.setUp()

    initRepository db

    ussdService = new UssdService(
        new DefaultMockConfig(),
        null, null, null, null, null, null, null, null, null, accessNumberRepository) {
      @Override
      String getSurveyUrl(Survey survey) { "http://surveys?id=$survey.id" }
    }

    serviceManager = new MockEsdpServiceManager()
    esdpService = new MockEsdpService(
        new DefaultMockConfig(), ussdService, serviceManager, accessNumberRepository)
  }

  void testSave() {
    def survey = survey(id: 42) {
      details(title: 'Survey 1')
      owner(id: 53)
    }

    esdpService.save(new User(), survey)

    assertThat serviceManager.calledMethods, hasSize(1)

    Service service = serviceManager.calledMethods[0][1] as Service

    assertEquals 'ips-0053-0042', service.id
    assertEquals 'ips-0053-0042', service.tag
    assertEquals 'Survey 1', service.title
    assertEquals 'http://surveys?id=42',
        service.properties.entry.find { e -> e.key == 'start-page' }.value
  }

  void testDelete() {
    def survey = survey(id: 42) {
      owner(id: 53, esdpProvider: 'ips')
    }

    esdpService.delete(new User(), survey)

    assertThat serviceManager.calledMethods, hasSize(1)
    assertEquals 'ips.ips-0053-0042', serviceManager.calledMethods[0][1]
  }

  void testUpdate1() {

    serviceManager = new MockEsdpServiceManager() {
      @Override
      Service getService(String arg0) {
        new Service(properties: new Service.Properties())
      }
    }

    final anrMock = new AccessNumberRepository(db) {
      @Override List<AccessNumber> list(Survey survey) { [] }

      @Override protected Class<AccessNumber> getEntityClass() { AccessNumber }
    }

    esdpService = new MockEsdpService(
        new DefaultMockConfig(), ussdService, serviceManager, anrMock)

    final survey = survey(id: 42) {
      details(title: 'Survey 1')
      statistics([:])
      owner(id: 53)
    }

    esdpService.update(new User(), survey)

    assertThat serviceManager.calledMethods, hasSize(1)
    def service = serviceManager.calledMethods[0][1]

    assertEquals '', service.properties.entry.find { e -> e.key == 'sip-number' }.value
    assertEquals 'Survey 1', service.title
  }

  void testUpdate2() {

    serviceManager = new MockEsdpServiceManager() {
      @Override
      Service getService(String arg0) {
        new Service(properties: new Service.Properties())
      }
    }
    esdpService = new MockEsdpService(
        new DefaultMockConfig(), ussdService, serviceManager, accessNumberRepository)

    final uiProfile = new UiProfile()

    final user = new User(
        id: 42,
        password: '123',
        login: 'user',
        fullName: 'user',
        email: 'user@example.com',
        role: Role.MANAGER,
        uiProfile: uiProfile)

    userRepository.save user

    def survey = survey(id: 42, startDate: new Date(), endDate: new Date(), owner: user) {
      details(title: 'Survey 1')
      statistics([:])
    }

    final id = surveyRepository.save(survey)
    survey = surveyRepository.get(id)

    accessNumberRepository.save(new AccessNumber(number: '123', surveyStats: survey.statistics))

    esdpService.update(new User(), survey)

    // One C2S
    assertThat serviceManager.calledMethods, hasSize(1)
    def service = serviceManager.calledMethods[0][1]

    assertEquals '123', service.properties.entry.find { e -> e.key == 'sip-number' }.value
    assertEquals 'Survey 1', service.title

    // Two C2S entries
    accessNumberRepository.save(new AccessNumber(number: '456', surveyStats: survey.statistics))
    esdpService.update(new User(), survey)

    service = serviceManager.calledMethods[1][1]

    assertEquals '123 456', service.properties.entry.find { e -> e.key == 'sip-number' }.value
  }

  static class MockEsdpService extends EsdpService {

    final EsdpServiceManager api

    MockEsdpService(Config config,
                    UssdService ussdService,
                    EsdpServiceManager api,
                    AccessNumberRepository accessNumberRepository) {
      super(config, ussdService, createMockEsdp(), null, accessNumberRepository)
      this.api = api
    }

    private static EsdpServiceSupport createMockEsdp() {
      new EsdpServiceSupport(null) {
        @Override
        String getServiceUrl(Survey survey) { "http://surveys?id=$survey.id" }
      }
    }

    @Override
    EsdpServiceManager getApi(String login, String passwordHash) { api }
  }

  static class MockEsdpServiceManager implements EsdpServiceManager {

    List<List> calledMethods = []

    @Override
    Service getService(String arg0) {
      calledMethods << ['getService', arg0]; null
    }

    @Override
    void createService(Service arg0) {
      calledMethods << ['createService', arg0]
    }

    @Override
    void updateService(Service arg0) {
      calledMethods << ['updateService', arg0]
    }

    @Override
    void activateCustomPackage(String arg0, int arg1, int arg2, int arg3) {
      calledMethods << ['createService', arg0, arg1, arg2, arg3]
    }

    @Override
    void deleteService(String arg0) {
      calledMethods << ['deleteService', arg0]
    }
  }
}
