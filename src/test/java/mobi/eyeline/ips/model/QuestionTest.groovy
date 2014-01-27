package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

class QuestionTest extends ValidationTestCase {

    void test1() {
        assertThat validate(new Question()), hasSize(1)
        assertThat validate(new Question(title: "")), hasSize(1)
    }

    void test2() {
        assertThat validate(new Question(title: "foo")), hasSize(0)
    }
}
