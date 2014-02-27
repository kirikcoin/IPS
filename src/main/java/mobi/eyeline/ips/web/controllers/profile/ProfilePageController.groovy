package mobi.eyeline.ips.web.controllers.profile

import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.util.HashUtils
import mobi.eyeline.ips.web.controllers.BaseController

class ProfilePageController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository
    private final UserService userService = Services.instance().userService

    User user

    String currentPassword

    String newPassword
    String newPasswordConfirmation

    boolean updateOk

    ProfilePageController() {
        user = userRepository.getByLogin(this.userName)
    }

    void saveProfile() {
        updateOk = true
        if (!isPasswordIntact()) {
            // Update user password if corresponding fields are filled in.
            updateOk = updatePassword()
        }

        updateOk &= validateModel()
        if (updateOk) {
            userRepository.update(user)
        }
    }

    private boolean isPasswordIntact() {
        currentPassword == null && newPassword == null && newPasswordConfirmation == null
    }

    private boolean updatePassword() {
        if (!userService.checkPassword(user, currentPassword)) {
            addErrorMessage(
                    resourceBundle.getString("profile.edit.password.invalid"),
                    "currentPassword")
            return false
        }

        if (newPassword == null) {
            // XXX: This case somehow passes validation.
            // Why is validator not triggered for empty fields?
            addErrorMessage(
                    resourceBundle.getString("profile.edit.message.password.required"),
                    "newPassword")
            return false
        }

        if (newPassword != newPasswordConfirmation) {
            addErrorMessage(
                    resourceBundle.getString("profile.edit.password.confirmation.mismatch"),
                    "newPasswordConfirmation")
            return false
        }

        user.password = HashUtils.hashPassword(newPassword)
        return true
    }

    private boolean validateModel() {
        if (renderViolationMessage(validator.validate(user))) {
            return false
        }

        if (!userService.isEmailAllowed(user)) {
            addErrorMessage(
                    resourceBundle.getString("client.dialog.validation.email.exists"),
                    "email")
            return false
        }

        return true
    }

}
