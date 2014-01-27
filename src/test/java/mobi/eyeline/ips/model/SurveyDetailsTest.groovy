package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

class SurveyDetailsTest extends ValidationTestCase {

    void test1() {
        def violations = validate new SurveyDetails()
        assertThat violations, hasSize(1)
    }

    void test2() {
        def violations = validate new SurveyDetails(title: "")
        assertThat violations, hasSize(1)
    }

    void test3() {
        def violations = validate new SurveyDetails(title: "foo")
        assertThat violations, empty()
    }
}
