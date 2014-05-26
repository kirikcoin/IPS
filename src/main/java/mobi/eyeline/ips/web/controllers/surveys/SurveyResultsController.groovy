package mobi.eyeline.ips.web.controllers.surveys

import au.com.bytecode.opencsv.CSVWriter
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Answer
import mobi.eyeline.ips.model.SurveySession
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.faces.context.FacesContext

@CompileStatic
@Slf4j('logger')
class SurveyResultsController extends BaseSurveyController {

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


    void download(FacesContext context, OutputStream os) {
        final int chunkSize = 100

        os.withWriter('UTF-8') { Writer writer ->
            final CSVWriter csvWriter = new CSVWriter(writer, ';' as char)

            try {
                // Add header line.
                csvWriter.writeNext([
                        strings['results.list.csv.msisdn'],
                        strings['results.list.csv.question.number'],
                        strings['results.list.csv.question.text'],
                        strings['results.list.csv.questionoption.number'],
                        strings['results.list.csv.questionoption.text'],
                        strings['results.list.csv.date']
                ] as String[])

                final int count = answerRepository.count(
                        getSurvey(),
                        periodStart,
                        periodEnd,
                        filter)

                // Write in chunks.
                (0..count / chunkSize).each { int i ->
                    def records = answerRepository.list(
                            getSurvey(),
                            periodStart,
                            periodEnd,
                            filter,
                            chunkSize,
                            i * chunkSize)
                    writeCSVData records, csvWriter
                }

            } finally {
                csvWriter.close()
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeCSVData(List<SurveySession> sessions,
                              CSVWriter csvWriter) {
        sessions.each { SurveySession session ->
            session.answers.each { Answer answer ->
                csvWriter.writeNext([
                        session.respondent.msisdn,
                        answer.question.activeIndex,
                        answer.question.title,
                        answer.option.activeIndex,
                        answer.option.answer,
                        answer.date
                ] as String[])
            }
        }
    }

}
