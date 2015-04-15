package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.InvitationDelivery
import mobi.eyeline.ips.repository.InvitationDeliveryRepository
import mobi.eyeline.ips.service.CsvParseService
import mobi.eyeline.ips.service.CsvParseService.CsvLineException
import mobi.eyeline.ips.service.CsvParseService.DuplicateMsisdnException
import mobi.eyeline.ips.service.CsvParseService.InvalidMsisdnFormatException
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.service.deliveries.DeliveryService
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.validators.SimpleConstraintViolation
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import mobi.eyeline.util.jsf.components.input_file.UploadedFile

import javax.faces.bean.ManagedBean
import javax.faces.model.SelectItem
import javax.validation.ConstraintViolation
import java.text.MessageFormat
import java.util.regex.Pattern

import static mobi.eyeline.ips.model.InvitationDelivery.State.ACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.State.COMPLETED
import static mobi.eyeline.ips.model.InvitationDelivery.State.INACTIVE
import static mobi.eyeline.ips.model.InvitationDelivery.Type.NI_DIALOG

@CompileStatic
@Slf4j('logger')
@ManagedBean(name = "invitationDeliveryController")
class InvitationDeliveryController extends BaseController {

  private static final int DEFAULT_SPEED = 10

  private final InvitationDeliveryRepository invitationDeliveryRepository =
      Services.instance().invitationDeliveryRepository
  private final CsvParseService csvParseService = Services.instance().csvParseService
  private final DeliveryService deliveryService = Services.instance().deliveryService

  boolean deliveryModifyError
  boolean deliveryReceiversError
  Boolean activateError
  Boolean pauseError

  InvitationDelivery invitationDelivery
  String speedString
  String typeString
  String retriesMaxString
  String retriesIntervalString
  Boolean dialogForEdit
  Integer modifiedDeliveryId
  String modifiedDeliveryFilename

  List<String> msisdnList
  UploadedFile inputFile
  String progress

  private List<ConstraintViolation> errorMessages = []

  @Delegate
  BaseSurveyReadOnlyController surveys =
      beanByName('baseSurveyReadOnlyController', BaseSurveyReadOnlyController)

  final List<SelectItem> types = InvitationDelivery.Type.values().collect {
    InvitationDelivery.Type t -> new SelectItem(t, nameOf(t))
  }

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

        return list.collect {
          //noinspection UnnecessaryQualifiedReference
          new TableItem(
              id: it.id,
              date: it.date,
              type: InvitationDeliveryController.nameOf(it.type),
              typeValue: it.type,
              speed: it.speed,
              processedCount: it.processedCount,
              totalCount: it.totalCount,
              errorsCount: it.errorsCount,
              state: it.state,
              stateString: InvitationDeliveryController.nameOf(it.state),
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

  @SuppressWarnings("GrMethodMayBeStatic")
  static String nameOf(InvitationDelivery.Type type) {
    //noinspection UnnecessaryQualifiedReference
    BaseController.strings["invitations.deliveries.table.type.$type".toString()]
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  static String nameOf(InvitationDelivery.State state) {
    //noinspection UnnecessaryQualifiedReference
    BaseController.strings["invitations.deliveries.table.status.$state".toString()]
  }

  public void fillDeliveryForEdit() {
    modifiedDeliveryId = getParamValue("modifiedDeliveryId").asInteger()
    if (modifiedDeliveryId != null) {
      invitationDelivery = invitationDeliveryRepository.get(modifiedDeliveryId)
      modifiedDeliveryFilename = invitationDelivery.inputFile
      speedString = invitationDelivery.speed
      typeString = nameOf(invitationDelivery.type)
      retriesMaxString = invitationDelivery.retriesMax
      retriesIntervalString = invitationDelivery.retriesIntervalMinutes
      dialogForEdit = true

    } else {
      invitationDelivery = new InvitationDelivery()
      speedString = DEFAULT_SPEED
      retriesMaxString = invitationDelivery.retriesMax
      retriesIntervalString = invitationDelivery.retriesIntervalMinutes
      dialogForEdit = false
    }
  }

  public void saveDelivery() {
    if (modifiedDeliveryId == null) {
      // Create new.
      invitationDelivery.survey = getSurvey()
      invitationDelivery.date = new Date()
      invitationDelivery.inputFile = inputFile?.filename

      if (validate(inputFile, invitationDelivery)) {
        invitationDeliveryRepository.saveWithSubscribers(invitationDelivery, msisdnList)

        if (invitationDelivery.state == ACTIVE) {
          deliveryService.start invitationDelivery
        }
      } else {
        invitationDelivery.state = INACTIVE
      }

    } else {
      // Update existing.
      final InvitationDelivery persistedDelivery =
          invitationDeliveryRepository.load(modifiedDeliveryId)

      if (persistedDelivery.state == COMPLETED) {
        logger.error "Delivery state is COMPLETED, cannot edit!"
        return
      }

      final boolean speedChanged = (persistedDelivery.speed != invitationDelivery.speed)
      final boolean stateChanged = (persistedDelivery.state != invitationDelivery.state)

      if (persistedDelivery.state == INACTIVE) {
        persistedDelivery.type = invitationDelivery.type
        persistedDelivery.text = invitationDelivery.text
        persistedDelivery.retriesEnabled = invitationDelivery.retriesEnabled
        persistedDelivery.retriesMax = invitationDelivery.retriesMax
        persistedDelivery.retriesIntervalMinutes = invitationDelivery.retriesIntervalMinutes
      }

      persistedDelivery.speed = invitationDelivery.speed
      persistedDelivery.state = invitationDelivery.state

      if (validate(persistedDelivery)) {
        invitationDeliveryRepository.update(persistedDelivery)

        if (speedChanged) {
          deliveryService.updateSpeed(persistedDelivery)
        }

        if (stateChanged) {
          if (persistedDelivery.state == ACTIVE) {
            deliveryService.start persistedDelivery

          } else if (persistedDelivery.state == INACTIVE) {
            deliveryService.stop persistedDelivery
          }
        }
      }
    }
  }

  public void startDelivery() {
    invitationDelivery.state = ACTIVE
    saveDelivery()
  }

  private boolean validate(InvitationDelivery invitationDelivery) {
    final def pattern = Pattern.compile('^[1-9]\\d{0,2}+$')

    if (speedString != null && pattern.matcher(speedString).matches()) {
      invitationDelivery.speed = Integer.parseInt(speedString)

    } else {
      errorMessages << new SimpleConstraintViolation('speed',
          strings['invitations.deliveries.dialog.speed.max'])
      deliveryModifyError = true
    }

    if (retriesMaxString != null) {
      try {
        invitationDelivery.retriesMax = Integer.parseInt(retriesMaxString)
      } catch (NumberFormatException e) {
        errorMessages << new SimpleConstraintViolation('retriesMax',
            strings['invitations.deliveries.retries.number.interval'])
        deliveryModifyError = true

      }
    } else {
      errorMessages << new SimpleConstraintViolation('retriesMax',
          strings['invitations.deliveries.retries.number.interval'])
      deliveryModifyError = true
    }

    if (retriesIntervalString != null) {
      try {
        invitationDelivery.retriesIntervalMinutes = Integer.parseInt(retriesIntervalString)
      } catch (NumberFormatException e) {
        errorMessages << new SimpleConstraintViolation('retriesIntervalMinutes',
            strings['invitations.deliveries.retries.interval.interval'])
        deliveryModifyError = true

      }
    } else {
      errorMessages << new SimpleConstraintViolation('retriesIntervalMinutes',
          strings['invitations.deliveries.retries.interval.interval'])
      deliveryModifyError = true
    }


    if (invitationDelivery.type != NI_DIALOG && !invitationDelivery.text) {
      errorMessages << new SimpleConstraintViolation('text',
          strings['invitations.deliveries.dialog.text'])
      deliveryModifyError = true
    }

    deliveryModifyError |= renderViolationMessage(
        (validator.validate(invitationDelivery) + errorMessages) as Set,
        [
            'text'                  : 'invitationText',
            'speed'                 : 'deliverySpeed',
            'inputFile'             : 'deliveryReceivers',
            'retriesMax'            : 'retriesMax',
            'retriesIntervalMinutes': 'retriesIntervalMinutes'

        ],
        ['text', 'inputFile', 'speed', 'retriesMax', 'retriesIntervalMinutes'])
    if (!invitationDelivery.inputFile) {
      deliveryReceiversError = true
    }

    return !deliveryModifyError
  }

  private boolean validate(UploadedFile inputFile,
                           InvitationDelivery invitationDelivery) {

    if (inputFile && inputFile.length < 0) {
      errorMessages << new SimpleConstraintViolation('inputFile',
          strings['invitations.deliveries.dialog.file.error.upload'])
      deliveryModifyError = true
    }

    if (inputFile) {
      validate(inputFile)
    }

    validate(invitationDelivery)

    return !deliveryModifyError
  }

  private boolean validate(UploadedFile file) {
    FileValidationResult result = validateFile(file)
    if (result.error) {
      errorMessages << new SimpleConstraintViolation('inputFile',
          result.errorMessage)
      deliveryModifyError = true
    }

    msisdnList = result.msisdnList

    return !deliveryModifyError
  }

  private FileValidationResult validateFile(UploadedFile file) {
    try {
      def list = csvParseService.parseFile file.inputStream
      return new FileValidationResult(list)

    } catch (CsvLineException e) {
      return new FileValidationResult(e)
    }
  }

  void activateDelivery() {
    try {
      InvitationDelivery delivery = invitationDeliveryRepository.load modifiedDeliveryId
      delivery.state = ACTIVE

      invitationDeliveryRepository.update delivery
      deliveryService.start delivery

      activateError = false

    } catch (Exception e) {
      logger.error("Error activating delivery", e)
      activateError = true
    }
  }

  void pauseDelivery() {
    try {
      InvitationDelivery delivery = invitationDeliveryRepository.load modifiedDeliveryId
      delivery.state = INACTIVE

      invitationDeliveryRepository.update delivery
      deliveryService.stop delivery

      pauseError = false

    } catch (Exception e) {
      logger.error("Error pause delivery", e)
      pauseError = true
    }
  }

  static class TableItem implements Serializable {
    int id
    Date date
    String type
    InvitationDelivery.Type typeValue
    int speed
    int processedCount
    int totalCount
    int errorsCount
    InvitationDelivery.State state
    String stateString
    String text
  }

  static class FileValidationResult {
    final boolean error
    final String errorMessage
    final List<String> msisdnList

    FileValidationResult(List<String> msisdnList) {
      this.error = false
      this.errorMessage = null
      this.msisdnList = msisdnList
    }

    FileValidationResult(CsvLineException e) {
      error = true
      msisdnList = null

      def genMessage = { String k ->
        //noinspection UnnecessaryQualifiedReference
        MessageFormat.format(BaseController.strings[k] as String, e.lineContent, e.lineNumber)
      }

      switch (e) {
        case InvalidMsisdnFormatException:
          errorMessage = genMessage 'invitations.deliveries.dialog.file.error.invalid'
          break

        case DuplicateMsisdnException:
          errorMessage = genMessage 'invitations.deliveries.dialog.file.error.duplicate'
          break

        default:
          errorMessage = null // To fix warning
          throw new AssertionError()
      }
    }

  }
}