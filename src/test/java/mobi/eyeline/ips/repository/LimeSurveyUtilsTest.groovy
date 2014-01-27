package mobi.eyeline.ips.repository

class LimeSurveyUtilsTest extends GroovyTestCase {

    void testSurveyOldTable() {
        assertEquals "lime_old_survey_123_20110517133750", LimeSurveyUtils.surveyOldTable(123, "20110517133750")
        assertEquals "lime_old_survey_123_%", LimeSurveyUtils.surveyOldTable(123, "%")
    }

    void testParseOldTableTail() {
        assertEquals "20110517133750", LimeSurveyUtils.parseOldTableTail("lime_old_survey_123_20110517133750")
    }

    void testParseOldTableDateTime() {
        def cal = Calendar.getInstance()
        cal.with {
            set(YEAR, 2011)
            set(MONTH, MAY)
            set(DAY_OF_MONTH, 17)
            set(HOUR_OF_DAY, 13)
            set(MINUTE, 37)
            set(SECOND, 50)
            set(MILLISECOND, 0)
        }

        assertEquals cal.getTime(), LimeSurveyUtils.parseOldTableDateTime("20110517133750")
    }
}
