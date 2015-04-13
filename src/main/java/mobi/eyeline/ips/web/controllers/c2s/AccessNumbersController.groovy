package mobi.eyeline.ips.web.controllers.c2s

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.controllers.surveys.SurveySettingsController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

import javax.faces.bean.ManagedBean

@CompileStatic
@Slf4j('logger')
@ManagedBean(name = "accessNumberController")
class AccessNumbersController extends BaseController {

  private
  final AccessNumberRepository accessNumberRepository = Services.instance().accessNumberRepository

  String search

  Integer modifiedNumberId
  String newNumber
  Boolean numberAddError


  DataTableModel getTableModel() {
    new DataTableModel() {
      @Override
      List getRows(int offset, int limit, DataTableSortOrder sortOrder) {
        def list = accessNumberRepository.list(
            search,
            sortOrder.columnId,
            sortOrder.asc,
            limit,
            offset)

        return list.collect { an ->
          new TableItem(
              id: an.id,
              number: an.number,
              surveyTitle: an.surveyStats?.survey?.details?.title,
              surveyId: an.surveyStats?.survey?.id
          )
        }
      }

      @Override
      int getRowsCount() {
        accessNumberRepository.count(search)
      }
    }
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  void goToSurvey(int surveyId) {
    SurveySettingsController.goToSurvey(surveyId)
  }

  void deleteNumber() {
    def number = accessNumberRepository.load(modifiedNumberId)
    accessNumberRepository.delete(number)
  }

  void addNumber() {
    final AccessNumber number = new AccessNumber(number: newNumber)

    numberAddError = renderViolationMessage(
        validator.validate(number),
        [
            'number': 'newNumberValue'
        ])
    if (numberAddError) {
      return
    }

    numberAddError = (accessNumberRepository.find(newNumber) != null)
    if (numberAddError) {
      addErrorMessage(strings['accessnumbers.duplicate'], 'newNumberValue')
    }

    if (!numberAddError) {
      accessNumberRepository.save number
    }
  }

  static class TableItem implements Serializable {
    int id
    String number

    String surveyTitle
    Integer surveyId
  }
}
