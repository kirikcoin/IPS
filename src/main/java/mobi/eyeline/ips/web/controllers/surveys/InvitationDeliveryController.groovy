package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.InvitationDeliveryType
import mobi.eyeline.ips.repository.InvitationDeliveryRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import mobi.eyeline.util.jsf.components.input_file.UploadedFile

import javax.faces.model.SelectItem

@CompileStatic
@Slf4j('logger')
class InvitationDeliveryController extends BaseSurveyController {
    private final InvitationDeliveryRepository invitationDeliveryRepository =
            Services.instance().getInvitationDeliveryRepository()

    boolean deliveryModifyError

    InvitationDelivery invitationDelivery
    UploadedFile inputFile

    InvitationDeliveryController() {
        invitationDelivery = new InvitationDelivery()
    }

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
                    String type = BaseController.strings["invitations.deliveries.table.type.$it.type".toString()]
                    new TableItem(
                            id: it.id,
                            date: it.date,
                            type: type,
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

    public void saveDelivery() {
        invitationDelivery.survey = getSurvey()
        invitationDelivery.date = new Date()
        invitationDelivery.inputFile = (inputFile != null) ? inputFile.filename : null

        if(validate(invitationDelivery))
          invitationDeliveryRepository.save(invitationDelivery);
    }

    private boolean validate(InvitationDelivery invitationDelivery){
        deliveryModifyError =
                renderViolationMessage(validator.validate(invitationDelivery), [
                        'text':         'invitationText',
                        'speed':        'deliverySpeed',
                        'inputFile':    'deliveryReceievers',
                ])
        return !deliveryModifyError
    }

    List<SelectItem> getTypes() {
        InvitationDeliveryType.values().collect {
            new SelectItem(it, strings["invitations.deliveries.table.type.$it".toString()])
        }
    }

    static class TableItem implements Serializable {
        int id
        Date date
        String type
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
