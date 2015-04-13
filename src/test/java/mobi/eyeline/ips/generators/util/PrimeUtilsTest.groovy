package mobi.eyeline.ips.generators.util

import com.google.common.collect.HashMultiset

class PrimeUtilsTest extends GroovyTestCase {

  @SuppressWarnings("GrMethodMayBeStatic")
  def from(Map map) {
    map.inject(HashMultiset.create()) { set, k, v -> set.add(k as long, v); set }
  }

  void test1() {
    assertEquals(
        from([7: 1]),
        PrimeUtils.factorize(7))
  }

  void test2() {
    assertEquals(
        from([3: 8, 11: 8]),
        PrimeUtils.factorize(BigInteger.valueOf(33).pow(8).longValue()))
  }

  void test3() {
    assertEquals(
        from([2: 16, 3: 16]),
        PrimeUtils.factorize(BigInteger.valueOf(36).pow(8).longValue()))
  }

  void test4() {
    assertEquals(
        from([11: 1, 389: 1, 19961: 1, 33029: 1]),
        PrimeUtils.factorize(2_821_109_907_451L))
  }
}
