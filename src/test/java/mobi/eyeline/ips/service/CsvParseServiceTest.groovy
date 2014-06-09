package mobi.eyeline.ips.service

import mobi.eyeline.ips.util.CsvParseService

import static mobi.eyeline.ips.util.CsvParseService.*

class CsvParseServiceTest extends GroovyTestCase {
    CsvParseService csvParseService

    void setUp() {
        super.setUp()
        csvParseService = new CsvParseService()
    }

    private List<String> getLines(String input) {
        csvParseService.parseFile new ByteArrayInputStream(input.getBytes('UTF-8'))
    }

    void testSharp() {
        def lines = getLines """
            #79130000001
            #79130000002
            89130000003"""

        assertEquals(['89130000003'], lines)
    }

    void testPlus() {
        def lines = getLines """+79130000001
                                +79130000002"""

        assertEquals(['79130000001', '79130000002'], lines)
    }

    void testInvalidFile() {
        assertEquals new InvalidMsisdnFormatException(2, 'someString'),
                groovy.test.GroovyAssert.shouldFail(InvalidMsisdnFormatException, {
                    getLines """79130000001
                        someString"""
                })
    }

    void testDuplicateNumbers() {
        assertEquals new DuplicateMsisdnException(3, '79130000001'),
                groovy.test.GroovyAssert.shouldFail(DuplicateMsisdnException, {
                    getLines """79130000001
                        79130000002
                        79130000001"""
                })
    }

    void testEmptyFile() {
        def lines = getLines """#someThing"""

        assertEquals([], lines)
    }
}
