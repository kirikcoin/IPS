package mobi.eyeline.ips.utils

import mobi.eyeline.ips.util.ListUtils

class ListUtilsTest extends GroovyTestCase {

    private static class Element {
        def id
        boolean skipped

        boolean equals(o) { id == o.id }
    }

    // Active element
    def a = { new Element(id: it) }

    // Disabled element
    def d = { new Element(id: it, skipped: true) }

    void testIsFirst() {
        def isFirst = {l, e ->
            //noinspection GroovyAssignabilityCheck
            ListUtils.isFirst(l, e, {it.skipped})
        }

        assertTrue isFirst([a(1), a(2), a(3)], a(1))
        assertTrue isFirst([a(1), d(2), d(3)], a(1))

        assertFalse isFirst([a(1), a(2), a(3)], a(2))

        assertTrue isFirst([d(1), a(2), a(3)], a(2))
        assertFalse isFirst([d(1), a(2), a(3)], a(3))
        assertTrue isFirst([d(1), d(2), a(3)], a(3))
    }

    void testIsLast() {
        def isLast = {l, e ->
            //noinspection GroovyAssignabilityCheck
            ListUtils.isLast(l, e, {it.skipped})
        }

        assertTrue isLast([a(1), a(2), a(3)], a(3))
        assertTrue isLast([a(1), d(2), d(3)], a(3))

        assertFalse isLast([a(1), a(2), a(3)], a(2))

        assertTrue isLast([a(1), a(2), d(3)], a(2))
        assertFalse isLast([d(1), a(2), a(3)], a(2))
        assertTrue isLast([a(1), d(2), d(3)], a(1))
    }

    void testGetNext() {
        def getNext = {l, e ->
            //noinspection GroovyAssignabilityCheck
            ListUtils.getNext(l, e, {it.skipped})
        }

        assertNull getNext([a(1), a(2), a(3)], a(3))
        assertNull getNext([a(1), a(2), d(3)], a(2))
        assertNull getNext([a(1), d(2), d(3)], a(1))

        assertEquals a(3), getNext([a(1), d(2), a(3)], a(1))
        assertEquals a(2), getNext([a(1), a(2), a(3)], a(1))
    }
}
