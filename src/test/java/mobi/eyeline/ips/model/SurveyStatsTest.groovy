package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.hasSize

/**
 * Created by dizan on 03.03.14.
 */
class SurveyStatsTest extends ValidationTestCase{
    void test1() {
        assertThat validate(new SurveyStats(accessNumber: 'a'*69)), hasSize(1)
        assertThat validate(new SurveyStats(accessNumber:'a'*71 )), hasSize(2)
        assertThat validate(new SurveyStats(accessNumber:'8'*69 )), empty()
        assertThat validate(new SurveyStats(accessNumber:'8-1*.2#' )), empty()
    }
}
