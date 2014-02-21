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
        def x
        def isFirst = {l, e ->
            //noinspection GroovyAssignabilityCheck
            ListUtils.isFirst(l, e, {it.skipped})
        }

        assertTrue isFirst([x = a(1), a(2), a(3)], x)
        assertTrue isFirst([x = a(1), d(2), d(3)], x)

        assertFalse isFirst([a(1), x = a(2), a(3)], x)

        assertTrue isFirst([d(1), x = a(2), a(3)], x)
        assertFalse isFirst([d(1), a(2), x = a(3)], x)
        assertTrue isFirst([d(1), d(2), x = a(3)], x)
    }

    void testIsLast() {
        def x
        def isLast = {l, e ->
            //noinspection GroovyAssignabilityCheck
            ListUtils.isLast(l, e, {it.skipped})
        }

        assertTrue isLast([a(1), a(2), x = a(3)], x)
        assertTrue isLast([a(1), d(2), x = a(3)], x)

        assertFalse isLast([a(1), x = a(2), a(3)], x)

        assertTrue isLast([a(1), x, d(3)], x)
        assertFalse isLast([d(1), x, a(3)], x)
        assertTrue isLast([x, d(2), d(3)], x)
    }

    void testGetNext() {
        def x
        def getNext = {l, e ->
            //noinspection GroovyAssignabilityCheck
            ListUtils.getNext(l, e, {it.skipped})
        }

        assertNull getNext([a(1), a(2), x = a(3)], x)
        assertNull getNext([a(1), x = a(2), d(3)], x)
        assertNull getNext([x = a(1), d(2), d(3)], x)

        assertEquals a(3), getNext([x = a(1), d(2), a(3)], x)
        assertEquals a(2), getNext([x = a(1), a(2), a(3)], x)
    }
}
