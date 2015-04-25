package mobi.eyeline.ips.generators.util

import mobi.eyeline.ips.generators.UnsupportedPatternException

class AutomatonUtilsTest extends GroovyTestCase {

  @SuppressWarnings("GrMethodMayBeStatic")
  private def asStateSymbols(String regex) {
    def a = AutomatonUtils.toAutomaton regex
    (AutomatonUtils.asStateSymbols(a)) as List
  }

  void test1() {
    assertEquals(
        [('a'..'z').sum()] * 8,
        asStateSymbols('[a-z]{8}'))
  }

  void test2() {
    assertEquals(
        [('0'..'9').sum()] * 4,
        asStateSymbols('[0-9]{4}'))
  }

  void test3() {
    assertEquals(
        [('a'..'z').sum() + ('0'..'9').sum()],
        asStateSymbols('[0-9a-z]{1}'))
  }

  void test4() {
    assertEquals(
        [('1'..'9').sum()] + [('0'..'9').sum()] * 3,
        asStateSymbols('[1-9][0-9]{3}'))
  }

  void testUnsupported1() {
    shouldFail(UnsupportedPatternException) {
      AutomatonUtils.toAutomaton(']123[')
    }
  }

  void testUnsupported2() {
    def a = AutomatonUtils.toAutomaton('[ab]*')
    shouldFail(UnsupportedPatternException) {
      AutomatonUtils.asStateSymbols(a)
    }
  }

  void testUnsupported3() {
    def a = AutomatonUtils.toAutomaton('[ab]{1,2}')
    shouldFail(UnsupportedPatternException) {
      AutomatonUtils.asStateSymbols(a)
    }
  }

}
