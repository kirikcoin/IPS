package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.external.MadvSoapApi
import mobi.eyeline.ips.external.madv.BannerInfo
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl
import mobi.eyeline.ips.external.madv.DeliveryInfo
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.model.SurveyStats
import mobi.eyeline.ips.properties.Config
import mobi.eyeline.ips.repository.DbTestCase
import mobi.eyeline.ips.repository.InvitationDeliveryRepository
import mobi.eyeline.ips.repository.SurveyInvitationRepository
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.repository.SurveyStatsRepository

import javax.xml.rpc.ServiceException
import java.rmi.RemoteException

import static mobi.eyeline.ips.model.InvitationUpdateStatus.*

class MadvUpdateServiceTest extends DbTestCase {

    // Configuration
    def configClass
    Config config

    SurveyRepository surveyRepository
    SurveyStatsRepository surveyStatsRepository
    SurveyInvitationRepository surveyInvitationRepository
    InvitationDeliveryRepository invitationDeliveryRepository
    SurveyService surveyService

    void setUp() {
        super.setUp()

        // Configuration
        configClass = new MockFor(Config).with {
            demand.getMadvUrl() { '' }
            demand.getMadvUserLogin() { '' }
            demand.getMadvUserPassword() { '' }
            it
        }
        config = configClass.proxyDelegateInstance() as Config

        surveyRepository = new SurveyRepository(db)
        surveyStatsRepository = new SurveyStatsRepository(db)
        surveyInvitationRepository = new SurveyInvitationRepository(db)
        invitationDeliveryRepository = new InvitationDeliveryRepository(db)
        surveyService = new SurveyService(surveyRepository, surveyInvitationRepository, invitationDeliveryRepository)
    }

    MadvUpdateService createService(Config config, MadvSoapApi api) {
        new MadvUpdateService(config, api, surveyStatsRepository, surveyRepository) {
            protected Timer createTimer() { new Timer() {
                void schedule(TimerTask task, long delay, long period) { task.run() }
                void schedule(TimerTask task, long delay) { task.run() }
            } }
        }
    }

    void tearDown() {
        super.tearDown()
    }

    void createSurvey(String campaign) {
        new Survey(startDate: new Date(), endDate: new Date()).with {
            details = new SurveyDetails(survey: it, title: 'Foo')
            statistics = new SurveyStats(survey: it, campaign: campaign)
            surveyRepository.save(it)
        }
    }

    void test1() {
        def api = new MadvSoapApi() {
            CampaignsSoapImpl get(String url, String login, String password) {
                throw new ServiceException()
            }
        }
        def madvUpdateService = createService(config, api)

        createSurvey('123')

        madvUpdateService.runNow()
        assertEquals SERVER_IS_NOT_AVAILABLE, surveyRepository.load(1).statistics.updateStatus
    }

    void test2() {
        def api = new MadvSoapApi() {
            CampaignsSoapImpl get(String url, String login, String password) {
                throw new RemoteException()
            }
        }
        def madvUpdateService = createService(config, api)

        createSurvey('123')

        madvUpdateService.runNow()
        assertEquals CAMPAIGN_NOT_FOUND, surveyRepository.load(1).statistics.updateStatus
    }

    void test3() {
        def api = new MadvSoapApi() {
            CampaignsSoapImpl get(String url, String login, String password) {
                throw new RemoteException()
            }
        }
        def madvUpdateService = createService(config, api)

        createSurvey(null)

        madvUpdateService.runNow(1)
        assertEquals UNDEFINED, surveyRepository.load(1).statistics.updateStatus
    }

    void testOk() {
        def deliveries  = (0..3).collect {new DeliveryInfo(impressionsCount: it)} as DeliveryInfo[]
        def banners     = (0..3).collect {new BannerInfo(impressionsCount: it)} as BannerInfo[]

        def implClass = new MockFor(CampaignsSoapImpl).with {
            demand.listDeliveries() { id -> assertEquals 42, id; deliveries }
            demand.listBanners() { id -> assertEquals 42, id; banners }
            it
        }
        def impl = implClass.proxyDelegateInstance() as CampaignsSoapImpl

        def api = new MadvSoapApi() {
            CampaignsSoapImpl get(String url, String login, String password) { impl }
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
