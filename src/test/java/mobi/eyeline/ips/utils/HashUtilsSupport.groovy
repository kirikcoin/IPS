package mobi.eyeline.ips.utils

import mobi.eyeline.ips.util.HashUtils

abstract class HashUtilsSupport {

  static def init() {
    // TODO: rename to something more meaningful. `hash' clashes with the private String field.
    String.metaClass.pw = { -> HashUtils.hash(delegate as String, "SHA-256", "UTF-8") }
  }
}
