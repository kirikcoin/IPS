package mobi.eyeline.ips.web.controllers.clients

import mobi.eyeline.ips.exceptions.LoginException
import mobi.eyeline.ips.model.User
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.UserService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

/**
 * Created by dizan on 05.02.14.
 */
class ClientListController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository
    private final UserService userService = Services.instance().userService

    def String userLogin
    def String userLoginForEdit
    def User userForEdit
    Boolean blockError
    Boolean unblockError
    String search

    ClientListController() {
        userForEdit= new User()
        userForEdit.fullName=""
        userForEdit.company=""
        userForEdit.login=""
        userForEdit.email=""
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
                            isBlocked: it.blocked
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
      //  userLoginForEdit = getParamValue("userLoginForEdit").asString()
        User user = userRepository.getByLogin(userLoginForEdit)
        user.fullName = userForEdit.fullName;
        user.company = userForEdit.company;
        user.login = userForEdit.login;
        user.email = userForEdit.email;
        userRepository.update(user)



    }


    void blockUser() {
        String userLogin = getParamValue("userLogin").asString()
            try {
                userService.blockUser(userLogin);
                blockError = false
            } catch (LoginException e) {
                blockError = true
            }
    }

    void unblockUser() {
        String userLogin = getParamValue("userLogin").asString()
            try {
                userService.unblockUser(userLogin);
                unblockError = false
            } catch (LoginException e) {
                unblockError = true
            }
    }



    static class TableItem implements Serializable {
        int id
        String fullname
        String company
        String login
        String email
        boolean isBlocked

    }
}
