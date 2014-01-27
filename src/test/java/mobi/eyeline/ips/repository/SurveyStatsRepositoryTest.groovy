package mobi.eyeline.ips.repository

import mobi.eyeline.ips.model.DistributionChannel
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails


class SurveyStatsRepositoryTest extends DbTestCase {
    private SurveyStatsRepository surveyStatsService
    private SurveyRepository surveyRepository
    private SurveyDetailsRepository surveyDetailsRepository

    void setUp() {
        super.setUp()

        surveyStatsService = new SurveyStatsRepository(db)
        surveyRepository = new SurveyRepository(db)
        surveyDetailsRepository = new SurveyDetailsRepository(db)
    }

    void testUpdate() {
        def survey = new Survey(
                startDate: new Date(),
                endDate: new Date())
        surveyRepository.save survey

        def details = new SurveyDetails(survey: survey, title: "Test survey")
        surveyDetailsRepository.save details

        surveyStatsService.update(1, 2, 1, null, null, null, "123")
        assertNotNull surveyStatsService.get(1)

        surveyStatsService.update(1, 3, 1, 1, DistributionChannel.ADVERT_ON_BALANCE, "lala", "456")

        def surveyStats = surveyStatsService.get(1)
        assertEquals "456", surveyStats.accessNumber
        assertEquals 5, surveyStats.registeredRespondentsCount
        assertEquals "lala", surveyStats.campaign
        assertEquals DistributionChannel.ADVERT_ON_BALANCE, surveyStats.channel

        surveyStatsService.update(1, 0, 0, 0, null, null,"123")
        assertEquals "lala", surveyStats.campaign
        assertEquals DistributionChannel.ADVERT_ON_BALANCE, surveyStats.channel
        assertEquals 5, surveyStats.registeredRespondentsCount
    }

}
