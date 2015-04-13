package mobi.eyeline.ips.model

import mobi.eyeline.ips.utils.HashUtilsSupport

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class ValidationTestCase {
  private Validator validator

  void init() {
    validator = Validation.buildDefaultValidatorFactory().validator

    HashUtilsSupport.init()
  }

  Set<ConstraintViolation> validate(any) { validator.validate any }
}
