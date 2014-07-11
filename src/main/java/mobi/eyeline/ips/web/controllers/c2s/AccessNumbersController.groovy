package mobi.eyeline.ips.web.controllers.c2s

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.ips.web.controllers.surveys.SurveySettingsController
import mobi.eyeline.jsfcomponents.data_table.model.DataTableModel
import mobi.eyeline.jsfcomponents.data_table.model.DataTableSortOrder


@CompileStatic
@Slf4j('logger')
class AccessNumbersController extends BaseController {

    private final AccessNumberRepository accessNumberRepository = Services.instance().accessNumberRepository

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

                return list.collect { AccessNumber it ->
                        new TableItem(
                                id: it.id,
                                number: it.number,
                                surveyTitle: it.surveyStats?.survey?.details?.title,
                                surveyId: it.surveyStats?.survey?.id
                        )
                }
            }

            @Override
            int getRowsCount() {
                accessNumberRepository.count(search)
            }
        }
    }

    void goToSurvey() {
        def surveyId = getParamValue('surveyId').asInteger()
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
