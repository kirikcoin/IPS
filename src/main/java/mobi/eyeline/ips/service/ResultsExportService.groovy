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

    void writeResultsCsv(OutputStream os,
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
                        filter,
                        null)

                // Write in chunks.
                (0..count / chunkSize).each { int i ->
                    def records = answerRepository.list(
                            survey,
                            periodStart,
                            periodEnd,
                            filter,
                            null,
                            chunkSize,
                            i * chunkSize)
                    writeResultsData records, csvWriter
                }

            } finally {
                csvWriter.close()
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeResultsData(List<SurveySession> sessions,
                                  CSVWriter csvWriter) {
        sessions.each { SurveySession session ->
            session.answers.each { Answer answer ->
                csvWriter.writeNext([
                        session.respondent.msisdn,
                        answer.question.activeIndex + 1,
                        answer.question.title,
                        answer.option.activeIndex + 1,
                        answer.option.answer,
                        answer.date
                ] as String[])
            }
        }
    }

    void writeCouponsCsv(OutputStream os,
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
                        filter,
                        true)

                // Write in chunks.
                (0..count / chunkSize).each { int i ->
                    def records = answerRepository.list(
                            survey,
                            periodStart,
                            periodEnd,
                            filter,
                            true,
                            chunkSize,
                            i * chunkSize)
                    writeCouponsData records, csvWriter
                }

            } finally {
                csvWriter.close()
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeCouponsData(List<SurveySession> sessions,
                                  CSVWriter csvWriter) {
        sessions.each { SurveySession session ->
            session.answers.each { Answer answer ->
                csvWriter.writeNext([
                        session.respondent.msisdn,
                        session.respondent.coupon
                ] as String[])
            }
        }
    }
}
