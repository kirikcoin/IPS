package mobi.eyeline.ips.web.controllers.surveys

import javafx.beans.binding.ListBinding
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.InvitationDeliveryType
import mobi.eyeline.ips.repository.InvitationDeliveryRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by dizan on 26.05.14.
 */
class InvitationDeliveryController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyInvitesController)
    private final InvitationDeliveryRepository invitationDeliveryRepository =
            Services.instance().getInvitationDeliveryRepository()

    DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int offset,
                         int limit,
                         DataTableSortOrder sortOrder) {
                final List<InvitationDelivery> list = invitationDeliveryRepository.list(
                        getSurvey(),
                        sortOrder.columnId,
                        sortOrder.asc,
                        limit,
                        offset)
                return list.collect {InvitationDelivery it ->
                    new TableItem(
                            id: it.id,
                            date: it.date,
                            type: it.type,
                            speed: it.speed,
                            currentPosition : it.currentPosition,
                            errorsCount: it.errorsCount,
                            text: [new TextValue(text: it.text)]
                    )
                }
            }

            @Override
            int getRowsCount() {
                return invitationDeliveryRepository.count(getSurvey())
            }
        }
    }

    static class TableItem implements Serializable {
        int id
        Date date
        InvitationDeliveryType type
        int speed
        int currentPosition
        int errorsCount
        List<TextValue> text
    }

    static class TextValue {
        String text


        @Override
        public String toString() {
            text
        }
    }
}
