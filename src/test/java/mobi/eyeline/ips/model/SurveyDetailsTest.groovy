package mobi.eyeline.ips.model

import org.apache.commons.lang3.RandomStringUtils

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

    void test4() {
        def randomString = RandomStringUtils.randomAlphabetic(71)
        def violations =
                validate new SurveyDetails(title: randomString,endText: randomString)
        assertThat violations, hasSize(2)
    }
    void test5() {
        def randomString1 = RandomStringUtils.randomAlphabetic(71)
        def randomString2 = RandomStringUtils.randomAlphabetic(69)
        def violations =
                validate new SurveyDetails(title: randomString1,endText: randomString2)
        assertThat violations, hasSize(1)
    }
    void test6() {
        def randomString1 = RandomStringUtils.randomAlphabetic(69)
        def violations =
                validate new SurveyDetails(title: randomString1,endText: randomString1)
        assertThat violations, hasSize(0)
    }
}
