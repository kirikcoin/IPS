package mobi.eyeline.ips.web.validators

class ImageValidatorTest extends GroovyTestCase {

    def imageValidator = new ImageValidator()

    void testOk() {
        assertTrue imageValidator.validate('a.jpg')
        assertTrue imageValidator.validate('урР урр.jpEg')
        assertTrue imageValidator.validate('  .jpg')
        assertTrue imageValidator.validate('..Jpg')
        assertTrue imageValidator.validate('zmeniť.PNG')
        assertTrue imageValidator.validate('zm en iť .PNG.jpg')
    }

    void testErr() {
        assertFalse imageValidator.validate('.jpg')
        assertFalse imageValidator.validate('урР урр.bmp')
        assertFalse imageValidator.validate('hellojpg')
        assertFalse imageValidator.validate('abc.jpga')
        assertFalse imageValidator.validate('z m en iť.P N G')
        assertFalse imageValidator.validate('abc')
        assertFalse imageValidator.validate('')
    }
}
