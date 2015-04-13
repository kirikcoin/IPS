package mobi.eyeline.ips.service

import groovy.transform.InheritConstructors
import mobi.eyeline.ips.model.User

import static junit.framework.TestCase.fail

@SuppressWarnings("GroovyMissingReturnStatement")
@InheritConstructors
class MockTemplateService extends TemplateService {

  String formatUserRegistration(User user, String rawPassword) { fail() }

  String formatUserModified(User user) { fail() }

  String formatUserDeactivation(User user) { fail() }

  String formatUserActivation(User user) { fail() }

  String formatPasswordRestore(User user, String rawNewPassword) { fail() }
}
