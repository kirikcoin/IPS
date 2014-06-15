package mobi.eyeline.ips.utils

import groovy.transform.EqualsAndHashCode

class ResourceBundleConsistencyTest extends GroovyTestCase {

    private def locales = ['en', 'ru', 'sk']

    static def value(key, locale = 'en') {
        new OrderedProperties().with {
            load(getClass().getResourceAsStream("/ips_${locale}.properties"))
            get(key) as String
        }
    }

    void testConsistency() {
        def bundles = locales.collectEntries { l ->
            [(l): new OrderedProperties().with {
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

        // Print keys, locale info and existing EN-values (good for retrieving texts for translation)
        String toString() {
            "${property} = ${value(property)}".padRight(200) + "$missing".padRight(10) + " present: $present"
        }

/*
        // Print keys and locale info
        String toString() {
            "${property.padRight(70)} missing: " + "$missing".padRight(10) + " present: $present"
        }
*/

    }

    /**
     * {@linkplain Properties} preserving key order.
     */
    @SuppressWarnings("GroovyUnsynchronizedMethodOverridesSynchronizedMethod")
    static class OrderedProperties extends Properties {

        private final LinkedHashSet keys = new LinkedHashSet()

        @Override
        Enumeration keys() {
            Collections.enumeration(keys)
        }

        @Override
        Object put(Object key, Object value) {
            keys << key
            super.put(key, value)
        }
    }
}
