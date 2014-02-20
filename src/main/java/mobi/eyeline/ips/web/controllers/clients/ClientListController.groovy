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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import static org.apache.commons.lang3.StringUtils.isEmpty;

class ClientListController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ClientListController)

    private final UserRepository userRepository = Services.instance().userRepository
    private final UserService userService = Services.instance().userService
    private final MailService mailService = Services.instance().mailService

    String userLogin
    String userLoginForEdit

    String userEmail

    User userForEdit

    String search
    Boolean blockError
    Boolean unblockError

    boolean modifiedUserDataValidationError

    Boolean passwordResetError

    ClientListController() {
        userForEdit= new User()
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
            int getRowsCount() {
                userRepository.count(search)
            }
        }
    }

    String saveModifiedUser() {
        User user;

        if(isEmpty(userLoginForEdit)){
            String password = userService.generatePassword()
            user = new User(
                    fullName: userForEdit.fullName,
                    company: userForEdit.company,
                    login: userForEdit.login,
                    email: userForEdit.email,
                    password: HashUtils.hashPassword(password),
                    role: Role.CLIENT)

            if (!validateUser(user)) return null

            userRepository.save(user)
            mailService.sendUserRegistration(user, password)
            return null
        } else {
            user = userRepository.getByLogin(userLoginForEdit)
            String oldEmail = user.email
            String oldLogin = user.login

            user.fullName = userForEdit.fullName
            user.company = userForEdit.company
            user.login = userForEdit.login
            user.email = userForEdit.email

            if (!validateUser(user)) return null

            userRepository.update(user)

            if (oldLogin != user.login && oldEmail == user.email) {
                mailService.sendUserModified(user)
            }

            if (oldEmail != user.email) {
                mailService.sendUserModified(user,oldEmail)
            }
        }
    }

    private boolean validateUser(User user) {
        boolean loginExists, emailExists
        emailExists = !userService.isEmailAllowed(user)
        loginExists = !userService.isLoginAllowed(user)
        modifiedUserDataValidationError =
                renderViolationMessage(validator.validate(user),
                        [
                                'fullName': 'clientSettingsFullName',
                                'company': 'clientSettingsCompany',
                                'login': 'clientSettingsLogin',
                                'email': 'clientSettingsEmail',
                        ])

        if (loginExists) {
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.login.exists"),
                    "clientSettingsLogin")
            modifiedUserDataValidationError = true
        }

        if (emailExists) {
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.email.exists"),
                    "clientSettingsEmail")
            modifiedUserDataValidationError = true
        }

        if (modifiedUserDataValidationError) {
            return false
        }
        return true
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
            userService.resetPassword(userEmail)
            passwordResetError = false
        } catch (LoginException e) {
            passwordResetError = true
            logger.error("Error in password reset.", e)

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
