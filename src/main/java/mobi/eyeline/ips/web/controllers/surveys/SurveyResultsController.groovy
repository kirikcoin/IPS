package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.service.ResultsExportService
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

import javax.annotation.PostConstruct
import javax.enterprise.inject.Model
import javax.faces.context.FacesContext
import javax.inject.Inject

@CompileStatic
@Slf4j('logger')
@Model
class SurveyResultsController extends BaseSurveyReadOnlyController {

  @Inject private AnswerRepository answerRepository
  @Inject private ResultsExportService resultsExportService

  Date periodStart
  Date periodEnd
  String filter

  boolean hasCoupons

  @PostConstruct
  void init() {
    periodStart = survey.startDate
    periodEnd = survey.endDate
    hasCoupons = survey.activePattern != null
  }

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
            filter,
            null)
      }
    }
  }


  @SuppressWarnings("GroovyUnusedDeclaration")
  void downloadResults(FacesContext context, OutputStream os) {
    def header = [
        strings['results.list.csv.msisdn'],
        strings['results.list.csv.question.number'],
        strings['results.list.csv.question.text'],
        strings['results.list.csv.questionoption.number'],
        strings['results.list.csv.questionoption.text'],
        strings['results.list.csv.date']
    ]

    resultsExportService.writeResultsCsv(
        os, header, getSurvey(), periodStart, periodEnd, filter, getTimeZone(), getLocale())
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  void downloadCoupons(FacesContext context, OutputStream os) {
    def header = [
        strings['results.list.csv.msisdn'],
        strings['results.list.csv.coupon']
    ]

    resultsExportService.writeCouponsCsv(
        os, header, getSurvey(), periodStart, periodEnd, filter, getTimeZone(), getLocale())
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  void downloadRespondents(FacesContext context, OutputStream os) {
    def header = [
        strings['results.list.csv.msisdn'],
        strings['results.list.csv.start.date']
    ]

    resultsExportService.writeRespondentsCsv(
        os, header, getSurvey(), periodStart, periodEnd, filter, getTimeZone(), getLocale())
  }
}
