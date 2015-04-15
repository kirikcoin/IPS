package mobi.eyeline.ips.web.controllers.profile

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.util.HashUtils
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.controllers.LocaleController
import mobi.eyeline.ips.web.controllers.TimeZoneHelper
import mobi.eyeline.ips.web.validators.SimpleConstraintViolation

import javax.annotation.PostConstruct
import javax.enterprise.context.RequestScoped
import javax.faces.model.SelectItem
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Named("profilePageController")
@RequestScoped
class ProfileController extends BaseController {

  @Inject private UserRepository userRepository
  @Inject private UserService userService

  User user

  String currentPassword

  String newPassword
  String newPasswordConfirmation
  LocaleController localeController

  boolean updateOk

  @PostConstruct
  void init() {
    user = getCurrentUser()
    localeController = new LocaleController()
  }

  void saveProfile() {
    updateOk = true

    updateOk &= validateModel()

    if (!isPasswordIntact()) {
      // Update user password if corresponding fields are filled in.
      updateOk &= updatePassword()
    }

    if (updateOk) {
      userRepository.update(user)
    }

    localeController.changeLocale(user)
  }

  private boolean isPasswordIntact() {
    currentPassword == null && newPassword == null && newPasswordConfirmation == null
  }

  private boolean updatePassword() {
    if (!userService.checkPassword(user, currentPassword)) {
      addErrorMessage(strings['profile.edit.password.invalid'], 'currentPassword')
      return false
    }

    if (newPassword == null) {
      // XXX: This case somehow passes validation.
      // Why is validator not triggered for empty fields?
      addErrorMessage(strings['profile.edit.message.password.required'], 'newPassword')
      return false
    }

    if (newPassword != newPasswordConfirmation) {
      addErrorMessage(
          strings['profile.edit.password.confirmation.mismatch'],
          'newPasswordConfirmation')
      return false
    }

    user.password = HashUtils.hashPassword(newPassword)
    return true
  }

  private boolean validateModel() {
    def messages = validator.validate(user)
    if (!userService.isEmailAllowed(user)) {
      messages << new SimpleConstraintViolation<User>('email',
          strings['client.dialog.validation.email.exists'])
    }

    return !renderViolationMessage(messages as Set, [:], ['fullName', 'email', 'phoneNumber'])
  }

  List<SelectItem> getTimeZones() { TimeZoneHelper.getTimeZones(getLocale()) }
}
