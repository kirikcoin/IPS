package mobi.eyeline.ips.web.validators

import javax.faces.validator.ValidatorException

class EmailValidatorTest extends GroovyTestCase {

    EmailValidator emailValidator = new EmailValidator()

    void testOk1() {
        emailValidator.validate(null, null, 'user@example.com')
    }

    void testOk2() {
        assertTrue EmailValidator.isValid('user@example.com')
    }

    void testErr1() {
        shouldFail(ValidatorException) {
            emailValidator.validate(null, null, 'example.com')
        }
    }

    void testErr2() {
        assertFalse EmailValidator.isValid('foo')
    }
}
