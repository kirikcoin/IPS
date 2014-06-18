package mobi.eyeline.ips.utils

import static mobi.eyeline.ips.model.SurveyPattern.Mode.LATIN_SYMBOLS
import static mobi.eyeline.ips.model.SurveyPattern.Mode.NUMERICAL
import static mobi.eyeline.ips.util.PatternUtil.asRegex

class PatternUtilTest extends GroovyTestCase {

    void test1() {
        assertEquals '[1-9][0-9]{4}', asRegex(NUMERICAL, 5)
    }

    void test2() {
        assertEquals '[1-9A-Z][0-9A-Z]{7}', asRegex(LATIN_SYMBOLS, 8)
    }
}
