package mobi.eyeline.ips.web.controllers.c2s

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

@CompileStatic
class AccessNumbersController extends BaseController {
    private final AccessNumberRepository accessNumberRepository = Services.instance().accessNumberRepository

    String search

    AccessNumbersController() {

    }

    public DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int offset, int limit, DataTableSortOrder sortOrder) {
                def list = accessNumberRepository.list(
                        search,
                        sortOrder.columnId,
                        sortOrder.asc,
                        limit,
                        offset)
                return list.collect{AccessNumber it ->
                        new TableItem(
                                id:it.id,
                                number: it.number,
                                survey: it.survey.survey as String
                        )
                }
            }

            @Override
            int getRowsCount() {
                return 0
            }
        }
    }

    static class TableItem implements Serializable {
        int id
        String number
        String survey
    }
}
