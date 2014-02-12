package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SurveyResultsController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyResultsController)

    private final AnswerRepository answerRepository = Services.instance().answerRepository

    Date periodStart = survey.startDate
    Date periodEnd = survey.endDate

    DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int startPos,
                         int count,
                         DataTableSortOrder sortOrder) {

                return answerRepository.list(
                        SurveyResultsController.this.survey,
                        periodStart,
                        periodEnd,
                        "",
                        sortOrder.columnId,
                        sortOrder.asc,
                        count,
                        startPos)
            }

            @Override
            int getRowsCount() {
                return answerRepository.count(
                        SurveyResultsController.this.survey,
                        periodStart,
                        periodEnd,
                        "")
            }
        }
    }


}
