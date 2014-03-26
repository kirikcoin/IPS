package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@CompileStatic
class SurveyResultsController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyResultsController)

    private final AnswerRepository answerRepository = Services.instance().answerRepository

    Date periodStart = survey.startDate
    Date periodEnd = survey.endDate
    String filter

    DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int startPos,
                         int count,
                         DataTableSortOrder sortOrder) {

                return answerRepository.list(
                        getSurvey(),
                        periodStart,
                        periodEnd,
                        filter,
                        sortOrder.columnId,
                        sortOrder.asc,
                        count,
                        startPos)
            }

            @Override
            int getRowsCount() {
                return answerRepository.count(
                        getSurvey(),
                        periodStart,
                        periodEnd,
                        filter)
            }
        }
    }
}
