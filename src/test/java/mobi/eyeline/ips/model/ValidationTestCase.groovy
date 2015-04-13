package mobi.eyeline.ips.model

import mobi.eyeline.ips.utils.HashUtilsSupport

import javax.validation.Validation
import javax.validation.Validator

abstract class ValidationTestCase extends GroovyTestCase {
  protected Validator validator

  public void setUp() {
    validator = Validation.buildDefaultValidatorFactory().validator

    HashUtilsSupport.init()

  }

  def validate = { validator.validate it }
}
