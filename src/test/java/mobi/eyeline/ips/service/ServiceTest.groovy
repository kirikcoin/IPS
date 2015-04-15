package mobi.eyeline.ips.service

import mobi.eyeline.ips.properties.DefaultMockConfig

class ServiceTest extends GroovyTestCase {

  void testDoubleInitialization() {
    Services.initialize(new ServicesImpl(new DefaultMockConfig()))

    shouldFail(AssertionError) {
      Services.initialize(new ServicesImpl(new DefaultMockConfig()))
    }
  }

  void testUninitialized() {
    //noinspection GroovyAccessibility
    Services.instance = null
    shouldFail(AssertionError) {
      Services.instance()
    }
  }
}
