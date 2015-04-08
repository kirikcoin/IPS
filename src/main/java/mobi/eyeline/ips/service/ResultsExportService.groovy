package mobi.eyeline.ips.service

import au.com.bytecode.opencsv.CSVWriter
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Answer
import mobi.eyeline.ips.model.OptionAnswer
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveySession
import mobi.eyeline.ips.model.TextAnswer
import mobi.eyeline.ips.repository.AnswerRepository

import java.text.DateFormat

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
                         String filter,
                         TimeZone timeZone,
                         Locale locale) {

        os.withWriter('UTF-8') { Writer writer ->
            final CSVWriter csvWriter = new CSVWriter(writer, ';' as char)

            try {
                // Add header line.
                csvWriter.writeNext(header as String[])

                iterateResults(
                        csvWriter,
                        survey,
                        periodStart,
                        periodEnd,
                        filter,
                        null,    // hasCoupon: doesn't really matter
                        timeZone,
                        locale,
                        this.&writeResultsData
                )

            } finally {
                csvWriter.close()
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeResultsData(List<SurveySession> sessions,
                                  CSVWriter csvWriter,
                                  TimeZone timeZone,
                                  Locale locale) {

        def df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale)
        df.timeZone = timeZone

        sessions.each { session ->
            session.answers.each {Answer answer ->
                String answerOptionText;
                int answerOptionNumber

                answerOptionText = answer.answer
                answerOptionNumber =
                        (answer instanceof OptionAnswer) ? (answer as OptionAnswer).option.activeIndex + 1 : -1

                   csvWriter.writeNext([
                        session.respondent.msisdn,
                        answer.question.activeIndex + 1,
                        answer.question.title,
                        answerOptionNumber,
                        answerOptionText,
                        df.format(answer.date)
                ] as String[])
            }
        }
    }

    void writeCouponsCsv(OutputStream os,
                         List<String> header,
                         Survey survey,
                         Date periodStart,
                         Date periodEnd,
                         String filter,
                         TimeZone timeZone,
                         Locale locale) {

        os.withWriter('UTF-8') { Writer writer ->
            final CSVWriter csvWriter = new CSVWriter(writer, ';' as char)

            try {
                // Add header line.
                csvWriter.writeNext(header as String[])

                iterateResults(
                        csvWriter,
                        survey,
                        periodStart,
                        periodEnd,
                        filter,
                        true,    // with coupons only
                        timeZone,
                        locale,
                        this.&writeCouponsData
                )

            } finally {
                csvWriter.close()
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeCouponsData(List<SurveySession> sessions,
                                  CSVWriter csvWriter,
                                  TimeZone timeZone,
                                  Locale locale) {
        sessions.each { session ->
            session.answers.each { answer ->
                csvWriter.writeNext([
                        session.respondent.msisdn,
                        session.respondent.coupon
                ] as String[])
            }
        }
    }

    void writeRespondentsCsv(OutputStream os,
                             List<String> header,
                             Survey survey,
                             Date periodStart,
                             Date periodEnd,
                             String filter,
                             TimeZone timeZone,
                             Locale locale) {

        os.withWriter('UTF-8') { Writer writer ->
            final CSVWriter csvWriter = new CSVWriter(writer, ';' as char)

            try {
                // Add header line.
                csvWriter.writeNext(header as String[])

                iterateResults(
                        csvWriter,
                        survey,
                        periodStart,
                        periodEnd,
                        filter,
                        null,    // coupons don't matter
                        timeZone,
                        locale,
                        this.&writeRespondentsData
                )

            } finally {
                csvWriter.close()
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void writeRespondentsData(List<SurveySession> sessions,
                                      CSVWriter csvWriter,
                                      TimeZone timeZone,
                                      Locale locale) {
        final df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale)
        df.timeZone = timeZone

        sessions.each { session ->
            csvWriter.writeNext([
                    session.respondent.msisdn,
                    df.format(session.respondent.startDate)
            ] as String[])
        }
    }

    private void iterateResults(CSVWriter csvWriter,
                                Survey survey,
                                Date periodStart,
                                Date periodEnd,
                                String filter,
                                Boolean hasCoupon,
                                TimeZone timeZone,
                                Locale locale,
                                @ClosureParams(value = SimpleType, options = ['List<SurveySession>', 'CSVWriter', 'TimeZone', 'Locale']) Closure doWrite) {
        final int count = answerRepository.count(
                survey,
                periodStart,
                periodEnd,
                filter,
                hasCoupon)

        // Write in chunks.
        (Math.ceil(count / chunkSize)).times { int i ->
            def records = answerRepository.list(
                    survey,
                    periodStart,
                    periodEnd,
                    filter,
                    hasCoupon,
                    chunkSize,
                    i * chunkSize)
            doWrite records, csvWriter, timeZone, locale
        }
    }
}
