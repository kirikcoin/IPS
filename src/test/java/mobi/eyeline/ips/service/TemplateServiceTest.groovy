package mobi.eyeline.ips.service

import groovy.mock.interceptor.MockFor
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.properties.Config

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.isEmptyString
import static org.hamcrest.Matchers.not

class TemplateServiceTest extends GroovyTestCase {

  TemplateService templateService

  def configClass
  def config

  void setUp() {
    configClass = new MockFor(Config)

    config = configClass.proxyDelegateInstance() as Config
    templateService = new TemplateService('http://example.com')
  }

  void tearDown() {
    configClass.verify config
  }

  void testFormatUserRegistration() {
    def user = new User(
        login: 'admin',
        password: 'ignored',
        email: 'username@example.com',
        fullName: 'John Doe')

    def text = templateService.formatUserRegistration(user, 'pw\$!jFo22/=')

    assertThat text, not(isEmptyString())
  }

  void testFormatUserDeactivation() {
    def user = new User(
        login: 'admin',
        password: 'ignored',
        email: 'username@example.com',
        fullName: 'John Doe')

    def text = templateService.formatUserDeactivation(user)

    assertThat text, not(isEmptyString())
  }

  void testFormatPasswordRestore() {
    def user = new User(
        login: 'admin',
        password: 'ignored',
        email: 'username@example.com',
        fullName: 'John Doe')

    def text = templateService.formatPasswordRestore(user, 'pw\$!jFo22/=')

    assertThat text, not(isEmptyString())
  }
}
