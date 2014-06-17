package mobi.eyeline.ips.generators.impl

import mobi.eyeline.ips.generators.impl.SimplePattern

class SimplePatternTest extends GroovyTestCase {

    void test1() {
        def options = ['ab', 'cd']
        def pattern = new SimplePattern(options as CharSequence[])

        assertEquals 4, pattern.capacity
        assertEquals(
                ['ac', 'ad', 'bc', 'bd'],
                (0..<4).collect { pattern.convert it })
    }

    void test2() {
        def options = ['0', '123']
        def pattern = new SimplePattern(options as CharSequence[])

        assertEquals 3, pattern.capacity
        assertEquals(
                ['01', '02', '03'],
                (0..<3).collect { pattern.convert it })
    }

    void test3() {
        def options = ['123', '0']
        def pattern = new SimplePattern(options as CharSequence[])

        assertEquals 3, pattern.capacity
        assertEquals(
                ['10', '20', '30'],
                (0..<3).collect { pattern.convert it })
    }

    void test4() {
        def options = ['0123456789', '0123456789', '0123456789']
        def pattern = new SimplePattern(options as CharSequence[])

        assertEquals 1_000, pattern.capacity
    }
}
