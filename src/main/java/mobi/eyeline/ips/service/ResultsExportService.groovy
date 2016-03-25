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
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.util.DataTableUtil
import mobi.eyeline.ips.util.LocaleUtil
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

import java.text.DateFormat

import static mobi.eyeline.ips.model.RespondentSource.RespondentSourceType.TELEGRAM

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
                       Integer accessNumber,
                       TimeZone timeZone,
                       Locale locale,
                       DataTableSortOrder sortOrder) {

    os.withWriter(LocaleUtil.exportCharset) { Writer writer ->
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
            accessNumber,
            null,    // hasCoupon: doesn't really matter
            timeZone,
            locale,
            sortOrder,
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
      session.answers.each { Answer answer ->
        String answerOptionText;
        int answerOptionNumber

        answerOptionText = answer.answer
        answerOptionNumber =
            (answer instanceof OptionAnswer) ? (answer as OptionAnswer).option.activeIndex + 1 : -1

        final resp = session.respondent
        csvWriter.writeNext([
            resp.source.sourceType == TELEGRAM ? "Telegram-$resp.msisdn" : resp.msisdn,
            resp.source.name,
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
                       Integer accessNumber,
                       TimeZone timeZone,
                       Locale locale,
                       DataTableSortOrder sortOrder) {

    os.withWriter(LocaleUtil.exportCharset) { Writer writer ->
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
            accessNumber,
            true,    // with coupons only
            timeZone,
            locale,
            sortOrder,
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
      final resp = session.respondent
      csvWriter.writeNext([
          resp.source.sourceType == TELEGRAM ? "Telegram-$resp.msisdn" : resp.msisdn,
          resp.source.name,
          resp.coupon
      ] as String[])
    }
  }

  void writeRespondentsCsv(OutputStream os,
                           List<String> header,
                           Survey survey,
                           Date periodStart,
                           Date periodEnd,
                           String filter,
                           Integer accessNumber,
                           TimeZone timeZone,
                           Locale locale,
                           DataTableSortOrder sortOrder) {

    os.withWriter(LocaleUtil.exportCharset) { Writer writer ->
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
            accessNumber,
            null,    // coupons don't matter
            timeZone,
            locale,
            sortOrder,
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
      final resp = session.respondent
      csvWriter.writeNext([
          resp.source.sourceType == TELEGRAM ? "Telegram-$resp.msisdn" : resp.msisdn,
          resp.source.name,
          df.format(resp.startDate)
      ] as String[])
    }
  }

  private void iterateResults(
      CSVWriter csvWriter,
      Survey survey,
      Date periodStart,
      Date periodEnd,
      String filter,
      Integer accessNumber,
      Boolean hasCoupon,
      TimeZone timeZone,
      Locale locale,
      DataTableSortOrder sortOrder,
      @ClosureParams(value = SimpleType,
          options = ['List<SurveySession>', 'CSVWriter', 'TimeZone', 'Locale']) Closure doWrite) {

    final int count = answerRepository.count(
        survey,
        periodStart,
        periodEnd,
        filter,
        accessNumber,
        hasCoupon)

    // Write in chunks.
    (Math.ceil(count / chunkSize)).times { int i ->
      def records = answerRepository.list(
          survey,
          periodStart,
          periodEnd,
          filter,
          accessNumber,
          hasCoupon,
          DataTableUtil.orderBy(
              sortOrder,
              ['respondent': 'msisdn', 'date': 'startDate', 'questions': 'answersCount']),
          chunkSize,
          i * chunkSize)
      doWrite records, csvWriter, timeZone, locale
    }
  }
}
