package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.DeliveryAbonent
import mobi.eyeline.ips.model.DeliveryAbonentStatus
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.model.InvitationDeliveryStatus
import mobi.eyeline.ips.model.InvitationDeliveryType
import mobi.eyeline.ips.repository.DeliveryAbonentRepository
import mobi.eyeline.ips.repository.InvitationDeliveryRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.PhoneValidator
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import mobi.eyeline.util.jsf.components.input_file.UploadedFile

import javax.faces.model.SelectItem
import java.text.MessageFormat

@CompileStatic
@Slf4j('logger')
class InvitationDeliveryController extends BaseSurveyController {
    private final InvitationDeliveryRepository invitationDeliveryRepository =
            Services.instance().getInvitationDeliveryRepository()
    private final DeliveryAbonentRepository deliveryAbonentRepository =
            Services.instance().getDeliveryAbonentRepository()

    boolean deliveryModifyError

    InvitationDelivery invitationDelivery
    Boolean dialogForEdit
    Integer modifiedDeliveryId

    List<String> abonents
    UploadedFile inputFile
    String progress

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
                    String statusString = BaseController.strings["invitations.deliveries.table.status.$it.status".toString()]
                    new TableItem(
                            id: it.id,
                            date: it.date,
                            type: type,
                            speed: it.speed,
                            currentPosition : it.currentPosition,
                            errorsCount: it.errorsCount,
                            status: it.status,
                            statusString: statusString,
                            text: it.text
                    )
                }
            }

            @Override
            int getRowsCount() {
                return invitationDeliveryRepository.count(getSurvey())
            }
        }
    }

    public void fillDeliveryForEdit() {
        modifiedDeliveryId = getParamValue("modifiedDeliveryId").asInteger()
        if(modifiedDeliveryId != null){
            invitationDelivery = invitationDeliveryRepository.get(modifiedDeliveryId)
            dialogForEdit = true
        } else {
            invitationDelivery = new InvitationDelivery()
            dialogForEdit = false
        }
    }

    public void saveDelivery() {
        invitationDelivery.survey = getSurvey()
        invitationDelivery.date = new Date()
        invitationDelivery.inputFile = (inputFile != null) ? inputFile.filename : null

        if(validate(invitationDelivery)){
            if (validate(inputFile)){
                invitationDeliveryRepository.save(invitationDelivery)

                abonents.each {String msisdn->
                    deliveryAbonentRepository.save(
                            new DeliveryAbonent(
                                    msisdn:msisdn,
                                    invitationDelivery: invitationDelivery,
                                    status: DeliveryAbonentStatus.NEW
                            )
                    )
                }
            }
        }

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

    private boolean validate(UploadedFile file){
        FileValidationResult result = validateFile(file)
        if(result.error){
            addErrorMessage(result.errorMessage, 'deliveryReceievers')
            deliveryModifyError = true
        }

        abonents = result.msisdns

        return !deliveryModifyError
    }

    private FileValidationResult validateFile(UploadedFile file) {
        PhoneValidator phoneValidator = new PhoneValidator()
        FileValidationResult validationResult= new FileValidationResult(error: false)

        def msisdns = []
        try {
            file.inputStream.eachLine('UTF-8') { String line, int lineNumber ->

                   if (!line.startsWith('#')) {
                       line = line.replace('+', '')
                       if(!phoneValidator.validate(line)) {
                           validationResult.generateInvalidErrorMessage(lineNumber,line)
                           throw new FileValidateException()
                       }
                       if(msisdns.contains(line)){
                           validationResult.generateDublicateErrorMessage(lineNumber,line)
                           throw new FileValidateException()
                       }
                       msisdns << line
                   }
            }
        }catch (FileValidateException){
            return validationResult
        }

        validationResult.msisdns = msisdns

        return validationResult;
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
        InvitationDeliveryStatus status
        String statusString
        String text
    }

    static class FileValidationResult {
        boolean error
        String errorMessage
        List<String> msisdns

        void generateInvalidErrorMessage(int lineNumber, String invalidString) {
            error=true
            String bundleMessage = BaseController.strings["invitations.deliveries.dialog.file.error.invalid"]
            errorMessage = MessageFormat.format(
                    bundleMessage,
                    invalidString,
                    lineNumber);
        }

        void generateDublicateErrorMessage(int lineNumber, String invalidString) {
            error=true
            String bundleMessage = BaseController.strings["invitations.deliveries.dialog.file.error.dublicate"]
            errorMessage= MessageFormat.format(
                    bundleMessage,
                    invalidString,
                    lineNumber);
        }

    }

    static class FileValidateException extends Exception{

    }
}
