package mobi.eyeline.ips.utils

import groovy.transform.EqualsAndHashCode

class ResourceBundleConsistencyTest extends GroovyTestCase {

    private def locales = ['en', 'ru', 'sk']

    void testAll() {
        shouldFail { this.disabledTestAll() }
    }

    void disabledTestAll() {
        def bundles = locales.collectEntries { l ->
            [(l): new Properties().with {
                load(getClass().getResourceAsStream("/ips_${l}.properties"))
                keys().toList()
            }]
        }

        def errors = new LinkedHashSet()

        bundles.values().each { bundle ->
            bundle.each { key ->
                errors << new Error(property: key).with {
                    bundles.each { (it.value.grep(key) ? present : missing) << it.key }; it
                }
            }
        }

        errors.findAll { it.missing }
                .with {
            if (!empty) {
                it.each { _ -> println _ }
                fail 'Localization bundles are inconsistent'
            }
        }
    }

    @EqualsAndHashCode(includes = 'property')
    static class Error {
        String property
        def present = []
        def missing = []

        String toString() {
            "Key: ${property.padRight(70)} missing: " + "$missing".padRight(10) + " present: $present"
        }
    }
}
