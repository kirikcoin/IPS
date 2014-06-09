package mobi.eyeline.ips.service

import mobi.eyeline.ips.util.CsvParseService

/**
 * Created by dizan on 09.06.14.
 */
class CsvParseServiceTest extends GroovyTestCase {
    CsvParseService csvParseService
    File file

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

        assertEquals(['79130000001','79130000002'],lines)
    }

    void testInvalidFile() {

        shouldFail(CsvParseService.InvalidMsisdnFormatException) {
            getLines """79130000001
                        someString"""
        }

        try {
            getLines """79130000001
                        someString"""
        } catch (CsvParseService.InvalidMsisdnFormatException e) {
            assertEquals('someString', e.lineContent)
            assertEquals(2, e.lineNumber)
        }
    }

    void testDuplicateNumbers() {
        shouldFail(CsvParseService.DuplicateMsisdnException) {
            getLines """79130000001
                        79130000002
                        79130000001"""
        }

        try {
            getLines """79130000001
                        79130000002
                        79130000001"""
        } catch (CsvParseService.DuplicateMsisdnException e) {
            assertEquals('79130000001', e.lineContent)
            assertEquals(3, e.lineNumber)
        }
    }

    void testEmptyFile() {
        def lines = getLines """#someThing"""

        assertEquals([],lines)
    }
}
