package mobi.eyeline.ips.utils

import mobi.eyeline.ips.messages.MissingParameterException

import static mobi.eyeline.ips.util.RequestParseUtils.*

class RequestParseUtilsTest extends GroovyTestCase {

    Map<String, String[]> asMap(Map orig) {
        [:].with { orig.entrySet().each { put(it.key, it.value as String[]) }; it }
    }

    void testInt() {
        assertEquals 42, getInt(asMap(['key': ['42', 'def']]), 'key')

        shouldFail(MissingParameterException) { getInt(asMap(['key': ['def']]), 'key') }
        shouldFail(MissingParameterException) { getInt(asMap(['key1': ['12']]), 'key') }
        shouldFail(MissingParameterException) { getInt(asMap([:]), 'key') }
    }

    void testBoolean() {
        assertEquals true, getBoolean(asMap(['key': ['true', 'false']]), 'key')
        assertEquals false, getBoolean(asMap(['key': ['false', 'true']]), 'key')
        assertEquals false, getBoolean(asMap(['key': ['foo']]), 'key')

        shouldFail(MissingParameterException) { getBoolean(asMap([:]), 'key') }
    }

    void testString() {
        assertEquals "foo", getString(asMap(['key': ['foo', 'bar']]), 'key')
        assertEquals "", getString(asMap(['key': ['', 'true']]), 'key')

        shouldFail(MissingParameterException) { getString(asMap(['key2': ['bar']]), 'key') }
    }

    void testToString() {
        assertEquals "{\"key1\"=[val1, val2], \"key2\"=[val3], \"key3\"=[]}",
                toString(asMap(['key1': ['val1', 'val2'], 'key2': ['val3'], 'key3': []]))
    }

    void testToString2() {
        assertEquals "{}", toString(asMap([:]))
    }
}
