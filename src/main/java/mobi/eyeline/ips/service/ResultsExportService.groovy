package mobi.eyeline.ips.service

import au.com.bytecode.opencsv.CSVWriter
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Answer
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveySession
import mobi.eyeline.ips.repository.AnswerRepository

@CompileStatic
@Slf4j('logger')
public class ResultsExportService {

    private final AnswerRepository answerRepository
    private final int chunkSize

    ResultsExportService(AnswerRepository answerRepository, int chunkSize) {
        this.answerRepository = answerRepository
        this.chunkSize = chunkSize
    }

    void writeCsv(OutputStream os,
                  List<String> header,
                  Survey survey,
                  Date periodStart,
                  Date periodEnd,
                  String filter) {

        os.withWriter('UTF-8') { Writer writer ->
            final CSVWriter csvWriter = new CSVWriter(writer, ';' as char)

            try {
                // Add header line.
                csvWriter.writeNext(header as String[])

                final int count = answerRepository.count(
                        survey,
                        periodStart,
                        periodEnd,
                        filter)

                // Write in chunks.
                (0..count / chunkSize).each { int i ->
                    def records = answerRepository.list(
                            survey,
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
