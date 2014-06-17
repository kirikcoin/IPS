package mobi.eyeline.ips.model

import static mobi.eyeline.ips.model.InvitationDelivery.State.ACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.Type.NI_DIALOG
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.empty
import static org.hamcrest.Matchers.hasSize

class InvitationDeliveryTest extends ValidationTestCase {

    void test1() {
        assertThat validate(new InvitationDelivery()), hasSize(3)
    }

    void test2() {
        def delivery = new InvitationDelivery(
                type: NI_DIALOG,
                state: ACTIVE,
                inputFile: "txt.txt",
                speed: 1,
                text: "a" * 200,)
        def violations = validate delivery
        assertEquals "text", violations[0].propertyPath.first().name
    }

    void test3() {
        def delivery = new InvitationDelivery(
                type: NI_DIALOG,
                state: ACTIVE,
                inputFile: "txt.txt",
                speed: 200,
                text: "a")
        def violations = validate delivery
        assertEquals "speed", violations[0].propertyPath.first().name

        delivery = new InvitationDelivery(
                type: NI_DIALOG,
                state: ACTIVE,
                inputFile: "txt.txt",
                speed: -1,
                text: "a")
        violations = validate delivery
        assertEquals "speed", violations[0].propertyPath.first().name
    }

}
