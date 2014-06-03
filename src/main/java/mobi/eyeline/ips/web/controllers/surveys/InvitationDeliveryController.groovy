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

// TODO-9: UI, USSD push message text: maybe, add some caption to it?
// Was:
//    My message
// Proposal:
//    Message content: My message
//
// TODO-10: If "1.1" is entered in the speed input, dialog closes with "Conversion error"
// TODO-11: "Expand" links in the table don't get proper pointer (see `Results' tab)
@CompileStatic
@Slf4j('logger')
class InvitationDeliveryController extends BaseSurveyController {
    // TODO-1: Use property accessors instead of get()-methods.
    private final InvitationDeliveryRepository invitationDeliveryRepository =
            Services.instance().invitationDeliveryRepository
    private final DeliveryAbonentRepository deliveryAbonentRepository =
            Services.instance().deliveryAbonentRepository

    boolean deliveryModifyError
    Boolean activateError
    Boolean pauseError

    InvitationDelivery invitationDelivery
    Boolean dialogForEdit
    Integer modifiedDeliveryId
    String modifiedDeliveryFilename

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
                return list.collect { InvitationDelivery it ->
                    String type = BaseController.strings["invitations.deliveries.table.type.$it.type".toString()]
                    String statusString = BaseController.strings["invitations.deliveries.table.status.$it.status".toString()]
                    new TableItem(
                            id: it.id,
                            date: it.date,
                            type: type,
                            speed: it.speed,
                            currentPosition: it.currentPosition,
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
        if (modifiedDeliveryId != null) {
            invitationDelivery = invitationDeliveryRepository.get(modifiedDeliveryId)
            modifiedDeliveryFilename = invitationDelivery.inputFile
            dialogForEdit = true
        } else {
            invitationDelivery = new InvitationDelivery()
            dialogForEdit = false
        }
    }

    public void saveDelivery() {
        if (modifiedDeliveryId == null) {
            invitationDelivery.survey = getSurvey()
            invitationDelivery.date = new Date()
            invitationDelivery.inputFile = (inputFile != null) ? inputFile.filename : null

            // TODO-2: Persist survey and associated MSISDNs in a single transaction
            // (extract this code to some repository-class?).
            if (validate(invitationDelivery)) {
                if (validate(inputFile)) {
                    invitationDeliveryRepository.save(invitationDelivery)
                    abonents.each { String msisdn ->
                        deliveryAbonentRepository.save(
                                new DeliveryAbonent(
                                        msisdn: msisdn,
                                        invitationDelivery: invitationDelivery,
                                        status: DeliveryAbonentStatus.NEW
                                )
                        )
                    }
                }
            }
        } else {
            InvitationDelivery editedDelivery = invitationDeliveryRepository.load(modifiedDeliveryId)

            editedDelivery.with { InvitationDelivery invDel ->
                invDel.type = invitationDelivery.type
                invDel.text = invitationDelivery.text
                invDel.speed = invitationDelivery.speed
            }

            if (validate(editedDelivery)) {
                invitationDeliveryRepository.update(editedDelivery)
            }

        }

    }

    private boolean validate(InvitationDelivery invitationDelivery) {

        deliveryModifyError =
                renderViolationMessage(validator.validate(invitationDelivery), [
                        'text': 'invitationText',
                        'speed': 'deliverySpeed',
                        // TODO-8: Correct spelling is `receivers'.
                        'inputFile': 'deliveryReceievers',
                ])

        return !deliveryModifyError
    }

    private boolean validate(UploadedFile file) {
        FileValidationResult result = validateFile(file)
        if (result.error) {
            // TODO-8: Correct spelling is `receivers'.
            addErrorMessage(result.errorMessage, 'deliveryReceievers')
            deliveryModifyError = true
        }

        abonents = result.msisdns

        return !deliveryModifyError
    }

    private FileValidationResult validateFile(UploadedFile file) {
        PhoneValidator phoneValidator = new PhoneValidator()
        FileValidationResult validationResult = new FileValidationResult(error: false)

        // TODO-3: Parsing and validation should be decoupled from controller,
        // moved to service-class and get some tests.
        def msisdns = []
        try {
            file.inputStream.eachLine('UTF-8') { String line, int lineNumber ->

                if (!line.startsWith('#')) {
                    line = line.replace('+', '')
                    if (!phoneValidator.validate(line)) {
                        validationResult.generateInvalidErrorMessage(lineNumber, line)
                        throw new FileValidateException()
                    }
                    if (msisdns.contains(line)) {
                        validationResult.generateDublicateErrorMessage(lineNumber, line)
                        throw new FileValidateException()
                    }
                    msisdns << line
                }
            }
        } catch (FileValidateException e) {
            // TODO-7: Incorrect syntax in `catch' clause?
            return validationResult
        }

        validationResult.msisdns = msisdns

        return validationResult;
    }

    void activateDelivery() {
        try {
            InvitationDelivery editedDelivery = invitationDeliveryRepository.load(modifiedDeliveryId)
            editedDelivery.status = InvitationDeliveryStatus.ACTIVE
            invitationDeliveryRepository.update(editedDelivery)
            activateError = false

        } catch (Exception e) {
            // TODO-4: Correct error message.
            logger.error("Error deactivating account", e)
            activateError = true
        }
    }

    void pauseDelivery() {
        try {
            InvitationDelivery editedDelivery = invitationDeliveryRepository.load(modifiedDeliveryId)
            editedDelivery.status = InvitationDeliveryStatus.INACTIVE
            invitationDeliveryRepository.update(editedDelivery)
            pauseError = false

        } catch (Exception e) {
            // TODO-5: What's an `account' here?
            logger.error("Error pause account", e)
            pauseError = true
        }
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
            error = true
            String bundleMessage = BaseController.strings["invitations.deliveries.dialog.file.error.invalid"]
            errorMessage = MessageFormat.format(
                    bundleMessage,
                    invalidString,
                    lineNumber);
        }

        // TODO-6: Correct is `duplicate'
        void generateDublicateErrorMessage(int lineNumber, String invalidString) {
            error = true
            String bundleMessage = BaseController.strings["invitations.deliveries.dialog.file.error.dublicate"]
            errorMessage = MessageFormat.format(
                    bundleMessage,
                    invalidString,
                    lineNumber);
        }

    }

    static class FileValidateException extends Exception {

    }
}
