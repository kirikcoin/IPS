package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.external.MadvSoapApi
import mobi.eyeline.ips.external.madv.BannerInfo
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl
import mobi.eyeline.ips.external.madv.DeliveryInfo
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.properties.DefaultMockConfig
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.RepositoryMock

import javax.xml.ws.WebServiceException

import static mobi.eyeline.ips.model.InvitationUpdateStatus.CAMPAIGN_NOT_FOUND
import static mobi.eyeline.ips.model.InvitationUpdateStatus.SERVER_IS_NOT_AVAILABLE
import static mobi.eyeline.ips.model.InvitationUpdateStatus.SUCCESSFUL
import static mobi.eyeline.ips.model.InvitationUpdateStatus.UNDEFINED
import static mobi.eyeline.ips.utils.SurveyBuilder.survey

@Mixin(RepositoryMock)
class MadvUpdateServiceTest extends DbTestCase {

  // Configuration
  def configClass
  Config config

  SurveyService surveyService

  void setUp() {
    super.setUp()

    initRepository(db)

    // Configuration
    configClass = new MockFor(Config).with {
      demand.getMadvUrl() { '' }
      demand.getMadvUserLogin() { '' }
      demand.getMadvUserPassword() { '' }
      it
    }
    config = configClass.proxyDelegateInstance() as Config

    surveyService = new SurveyService(
        surveyRepository,
        questionRepository,
        questionOptionRepository,
        surveyInvitationRepository,
        invitationDeliveryRepository)
  }

  MadvUpdateService createService(Config config, MadvSoapApi api) {
    new MadvUpdateService(config, api, new MadvService(), surveyStatsRepository, surveyRepository) {
      protected Timer createTimer() {
        new Timer() {
          void schedule(TimerTask task, long delay, long period) { task.run() }
          void schedule(TimerTask task, long delay) { task.run() }
        }
      }
    }
  }

  void createSurvey(String campaign) {
    surveyRepository.save(survey(startDate: new Date(), endDate: new Date()) {
      details(title: 'Foo')
      statistics(campaign: campaign)
    })
  }

  void test1() {
    def api = new MadvSoapApi(new DefaultMockConfig()) {
      @Override
      CampaignsSoapImpl getSoapApi() { throw new WebServiceException() }
    }
    def madvUpdateService = createService(config, api)

    createSurvey('123')

    madvUpdateService.runNow()
    assertEquals SERVER_IS_NOT_AVAILABLE, surveyRepository.load(1).statistics.updateStatus
  }

  void test2() {
    def impl = { throw new WebServiceException() } as CampaignsSoapImpl
    def api = new MadvSoapApi(new DefaultMockConfig()) {
      @Override
      CampaignsSoapImpl getSoapApi() { impl }
    }

    def madvUpdateService = createService(config, api)

    createSurvey(42.toString())

    madvUpdateService.runNow()
    assertEquals CAMPAIGN_NOT_FOUND, surveyRepository.load(1).statistics.updateStatus
  }

  void test3() {
    def api = new MadvSoapApi(new DefaultMockConfig()) {
      @Override
      CampaignsSoapImpl getSoapApi() { throw new WebServiceException() }
    }
    def madvUpdateService = createService(config, api)

    createSurvey(null)

    madvUpdateService.runNow(1)
    assertEquals UNDEFINED, surveyRepository.load(1).statistics.updateStatus
  }

  void testOk() {
    def deliveries = (0..3).collect { new DeliveryInfo(impressionsCount: it) }
    def banners = (0..3).collect { new BannerInfo(impressionsCount: it) }

    def implClass = new MockFor(CampaignsSoapImpl).with {
      demand.listDeliveries() { id -> assertEquals 42, id; deliveries }
      demand.listBanners() { id -> assertEquals 42, id; banners }
      it
    }
    def impl = implClass.proxyDelegateInstance() as CampaignsSoapImpl

    def api = new MadvSoapApi(new DefaultMockConfig()) {
      @Override
      CampaignsSoapImpl getSoapApi() { impl }
    }

    def madvUpdateService = createService(config, api)

    createSurvey(42.toString())

    madvUpdateService.runNow(1)

    surveyRepository.load(1).with {
      assertEquals SUCCESSFUL, statistics.updateStatus
      assertEquals 12, statistics.sentCount
      assertEquals 12, surveyService.countInvitations(it)
    }
  }
}
