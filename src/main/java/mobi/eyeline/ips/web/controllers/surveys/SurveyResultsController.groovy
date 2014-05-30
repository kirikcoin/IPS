package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.service.ResultsExportService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

import javax.faces.context.FacesContext

@CompileStatic
@Slf4j('logger')
class SurveyResultsController extends BaseSurveyController {

    private final AnswerRepository answerRepository = Services.instance().answerRepository
    private final ResultsExportService resultsExportService = Services.instance().resultsExportService

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


    @SuppressWarnings("GroovyUnusedDeclaration")
    void download(FacesContext context, OutputStream os) {
        def header = [
                strings['results.list.csv.msisdn'],
                strings['results.list.csv.question.number'],
                strings['results.list.csv.question.text'],
                strings['results.list.csv.questionoption.number'],
                strings['results.list.csv.questionoption.text'],
                strings['results.list.csv.date']
        ]

        resultsExportService.writeCsv(os, header, getSurvey(), periodStart, periodEnd, filter)
    }

}
