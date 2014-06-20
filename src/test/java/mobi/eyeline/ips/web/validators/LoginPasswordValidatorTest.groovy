package mobi.eyeline.ips.web.validators

import javax.faces.validator.ValidatorException

class LoginPasswordValidatorTest extends GroovyTestCase {

    void test1() {
        shouldFail(ValidatorException) {
            new LoginPasswordValidator().validate(null, null, 'кириллица!')
        }
    }

    void test2() {
        shouldFail(ValidatorException) {
            new LoginPasswordValidator().validate(null, null, null)
        }

        shouldFail(ValidatorException) {
            new LoginPasswordValidator().validate(null, null, '')
        }
    }

    void test3() {
        shouldFail(ValidatorException) {
            new LoginPasswordValidator().validate(null, null, 'ab%')
        }

        shouldFail(ValidatorException) {
            new LoginPasswordValidator().validate(null, null, '\'; drop tables; --')
        }
    }
}
