package mobi.eyeline.ips.utils

import static mobi.eyeline.ips.model.SurveyPattern.Mode.DIGITS
import static mobi.eyeline.ips.model.SurveyPattern.Mode.DIGITS_AND_LATIN
import static mobi.eyeline.ips.util.PatternUtil.asRegex

class PatternUtilTest extends GroovyTestCase {

  void test1() {
    assertEquals '[1-9][0-9]{4}', asRegex(DIGITS, 5)
  }

  void test2() {
    assertEquals '[1-9A-Z][0-9A-Z]{7}', asRegex(DIGITS_AND_LATIN, 8)
  }
}
