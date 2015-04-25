package mobi.eyeline.ips.generators.impl

class PolynomialPermutationTest extends GroovyTestCase {

  void test1() {
    shouldFail(IllegalArgumentException) {
      new PolynomialPermutation(0)
    }
  }

  void test2() {
    shouldFail(IllegalArgumentException) {
      new PolynomialPermutation(-1)
    }
  }

}
