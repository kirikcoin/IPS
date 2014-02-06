package mobi.eyeline.ips.web.controllers.clients

import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import mobi.eyeline.util.jsf.components.data_table.model.ModelException

/**
 * Created by dizan on 05.02.14.
 */
class ClientListController extends BaseController {

    private final UserRepository userRepository = Services.instance().userRepository
    String search

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


    static class TableItem implements Serializable {
        int id
        String fullname
        String company
        String login
        String email
        boolean isBlocked

    }
}
