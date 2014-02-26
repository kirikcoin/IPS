package mobi.eyeline.ips.utils

import mobi.eyeline.ips.util.StringUtils

class StringUtilsTest extends GroovyTestCase {

    void testHash1() {
        assertEquals "666F6F", StringUtils.asHex("foo".getBytes('UTF-8'))
    }
}
