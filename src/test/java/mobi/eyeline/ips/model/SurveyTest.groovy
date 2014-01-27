package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.not

class SurveyTest extends ValidationTestCase {

    void test1() {
        assertThat validate(new Survey()), not(empty())
    }

    void test2() {
        def date1 = Date.parse("HH:mm", "00:00")
        def date2 = Date.parse("HH:mm", "01:00")

        assertThat validate(
                new Survey(startDate: date2, endDate: date1)
        ), not(empty())
    }

    void test3() {
        assertThat validate(new Survey()), not(empty())
    }

    void testValid1() {
        def date1 = Date.parse("HH:mm", "00:00")
        def date2 = Date.parse("HH:mm", "01:00")

        assertThat validate(
                new Survey(startDate: date1, endDate: date2)
        ), empty()
    }

    void testValid2() {
        def same = Date.parse("HH:mm", "00:00")

        assertThat validate(
                new Survey(startDate: same, endDate: same)
        ), empty()
    }
}
