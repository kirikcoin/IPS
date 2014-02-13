package mobi.eyeline.ips.web.controllers.clients

import mobi.eyeline.ips.exceptions.LoginException
import mobi.eyeline.ips.model.Role
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.MailService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.util.HashUtils
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

class ClientListController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository
    private final UserService userService = Services.instance().userService
    private final MailService mailService = Services.instance().mailService

    String userLogin
    String userLoginForEdit

    String userEmail

    User userForEdit
    User newUser

    String search
    Boolean blockError
    Boolean unblockError

    boolean newUserDataValidationError
    boolean modifiedUserDataValidationError

    Boolean passwordResetError

    ClientListController() {
        userForEdit= new User()

        newUser= new User()
    }

    public DataTableModel getTableModel() {
        return new DataTableModel() {

            @Override
            List getRows(int offset,
                         int limit,
                         DataTableSortOrder sortOrder){
                def list = userRepository.list(
                        search,
                        sortOrder.columnId,
                        sortOrder.asc,
                        limit,
                        offset
                )

                return list.collect {
                    new TableItem(
                            fullname: it.fullName,
                            company: it.company,
                            login: it.login,
                            email: it.email,
                            blocked: it.blocked
                    )
                }
            }

            @Override
            public int getRowsCount() {
                userRepository.count(search)
            }
        }
    }

    void saveModifiedUser() {
        boolean editedEmailExists
        boolean editedLoginExists

        User user = userRepository.getByLogin(userLoginForEdit)
        String oldEmail = user.email
        String oldLogin = user.login

        user.fullName = userForEdit.fullName
        user.company = userForEdit.company
        user.login = userForEdit.login
        user.email = userForEdit.email

        editedEmailExists = !userService.isEmailAllowed(user)
        editedLoginExists = !userService.isLoginAllowed(user)
        modifiedUserDataValidationError =
                renderViolationMessage(validator.validate(user),
                        [
                                'fullName': 'clientSettingsFullName',
                                'company': 'clientSettingsCompany',
                                'login': 'clientSettingsLogin',
                                'email': 'clientSettingsEmail',
                        ])


        if(editedLoginExists){
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.login.exists"),
                    "clientSettingsLogin")
            modifiedUserDataValidationError = true
        }

        if(editedEmailExists){
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.email.exists"),
                    "clientSettingsEmail")
            modifiedUserDataValidationError = true
        }

        if (modifiedUserDataValidationError) {
            return
        }

        userRepository.update(user)

        if (oldLogin != user.login && oldEmail == user.email) {
            mailService.sendUserModified(user)
        }

        if (oldEmail != user.email) {
            mailService.sendUserModified(user,oldEmail)
        }
    }

    String createUser() {
        boolean emailExists
        boolean loginExists
        String password = userService.generatePassword()
        User user = new User(
                fullName: newUser.fullName,
                company: newUser.company,
                login: newUser.login,
                email: newUser.email,
                password: HashUtils.hashPassword(password),
                role: Role.CLIENT)

        emailExists = !userService.isEmailAllowed(user)
        loginExists = !userService.isLoginAllowed(user)
        newUserDataValidationError =
                renderViolationMessage(validator.validate(user),
                [
                        'fullName': 'newClientFullName',
                        'company': 'newClientCompany',
                        'login': 'newClientLogin',
                        'email': 'newClientEmail',
                ])

        if(loginExists){
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.login.exists"),
                            "newClientLogin")
            newUserDataValidationError = true
        }

        if(emailExists){
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.email.exists"),
                            "newClientEmail")
            newUserDataValidationError = true
        }

        if(newUserDataValidationError){
            return null
        }

        userRepository.save(user)
        mailService.sendUserRegistration(user, password)
        return null
    }

    void blockUser() {
        String userLogin = getParamValue("userLogin").asString()
        userService.blockUser(userLogin)
        blockError = false
    }

    void unblockUser() {
        String userLogin = getParamValue("userLogin").asString()
        userService.unblockUser(userLogin)
        unblockError = false
    }

    void resetPassword() {
        try {
            userService.restorePassword(userEmail)
            passwordResetError = false
        } catch (LoginException e) {
            passwordResetError = true
        }
    }

    static class TableItem implements Serializable {
        int id
        String fullname
        String company
        String login
        String email
        boolean blocked

    }
}
