package mobi.eyeline.ips.generators.util

class CharUtilsTest extends GroovyTestCase {

    void test1() {
        assertEquals 0, CharUtils.permutations([] as CharSequence[])
    }
}
