package mobi.eyeline.ips.generators

class SequenceGeneratorTest extends GroovyTestCase {

    @SuppressWarnings("GrMethodMayBeStatic")
    void ensureUnique(List list) { assertEquals list.size(), new HashSet(list).size() }

    @SuppressWarnings("GrMethodMayBeStatic")
    List produce(gen, max = null) {
        def results = []

        int idx = 0
        def result
        while ((result = gen.next()) && (!max || idx++ < max)) {
            results << result
        }
        results
    }

    void test1() {
        def gen = new GeneratorBuilder('[01][01]').build(0)

        assertEquals 4, gen.total
        assertEquals 4, gen.available
        assertEquals 0, gen.currentPosition

        def results = produce(gen)
        assertEquals gen.total, gen.currentPosition

        assertEquals(['00', '11', '10', '01'], results)
    }

    void test2() {
        def gen = new GeneratorBuilder('[0-9]{4}').build(0)

        assertEquals 10_000, gen.total
        assertEquals 10_000, gen.available

        def results = produce(gen)
        ensureUnique(results)

        assertEquals(
                '[3332, 3413, 3654, 4055, 4616, 5337, 6218, 7259, 8460, 9821, 1342]',
                results[0..10].toString())
    }

    void test3() {
        def gen = new GeneratorBuilder('[a-z]{8}').build(0)

        def results = produce(gen, 100_000)
        ensureUnique(results)

        assertEquals(
                '[iriririq, iririwgr, irirjlas, irirkjqt, irirlscu, irirnkkv, irirpmow, irirryox, iriruuky, iriryacz, irisbpra]',
                results[0..10].toString())
    }

    void test4() {
        def gen = new GeneratorBuilder('[1-9][0-9]{3}').build(0)

        def results = produce(gen)
        ensureUnique(results)

        assertEquals(
                '[3999, 4360, 5441, 7242, 9763, 4004, 7965, 3646, 9047, 6168, 4009]',
                results[0..10].toString())
    }

    void test5() {
        def gen = new GeneratorBuilder('[1-9a-z][0-9a-z]{6}').build(0)

        def results = produce(gen, 100_000)
        ensureUnique(results)

        assertEquals(
                '[lx99999, lyajh4y, lyba5oz, lyck8yc, lyedrw1, lygoqk2, lyjh4yf, lymty04, lyqn8s5, lyu0yai, lyzv3g7]',
                results[0..10].toString())
    }

    void test6() {
        def gen = new GeneratorBuilder('[1-9][0-9]{5}').build(0)

        def results = produce(gen)
        ensureUnique(results)

        assertEquals(
                '[399999, 401440, 405761, 412962, 423043, 436004, 451845, 470566, 492167, 516648, 544009]',
                results[0..10].toString())
    }
}
