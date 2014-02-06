package mobi.eyeline.ips.model

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize

class QuestionTest extends ValidationTestCase {

    void test1() {
        assertThat validate(new Question()), hasSize(2)
        assertThat validate(new Question(title: "")), hasSize(2)
    }

    void test2() {
        assertThat validate(new Question(title: "foo")), hasSize(1)
    }

    void test3() {
        def question =
                new Question(title: "foo", options: [new QuestionOption(answer: 'Foo')])
        assertThat validate(question), hasSize(0)
    }
}
