package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.SurveySession
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.ResultsExportService
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import mobi.eyeline.util.jsf.components.data_table.model.ModelWithObjectIds

import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.model.SelectItem
import javax.faces.view.ViewScoped
import javax.inject.Inject
import javax.inject.Named

import static org.apache.commons.collections.CollectionUtils.isEmpty

@CompileStatic
@Slf4j('logger')
@Named
@ViewScoped
class SurveyResultsController extends BaseSurveyReadOnlyController {

  @Inject private transient AnswerRepository answerRepository
  @Inject private transient ResultsExportService resultsExportService
  @Inject private transient AccessNumberRepository accessNumberRepository
  @Inject private transient RespondentRepository respondentRepository

  Date periodStart
  Date periodEnd
  String filter
  Integer accessNumber

  boolean hasCoupons

  /** Current sort order of the results table. */
  DataTableSortOrder sortOrder

  String errorId

  List<String> selectedRows

  @PostConstruct
  void init() {
    periodStart = survey.startDate
    periodEnd = survey.endDate
    hasCoupons = survey.activePattern != null
  }

  ModelWithObjectIds getTableModel() {
    return new ModelWithObjectIds() {

      @Override
      List getRows(int startPos,
                   int count,
                   DataTableSortOrder sortOrder) {

        return answerRepository.list(
            getSurvey(),
            periodStart,
            periodEnd,
            filter,
            accessNumber > 0 ? accessNumber : null,
            sortOrder?.columnId,
            sortOrder?.asc,
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
            accessNumber > 0 ? accessNumber : null,
            null)
      }

      @Override
      String getId(Object o) {
        (o as SurveySession).respondent.id
      }
    }
  }


  @SuppressWarnings("GroovyUnusedDeclaration")
  void downloadResults(FacesContext context, OutputStream os) {
    def header = [
        strings['results.list.csv.msisdn'],
        strings['results.list.csv.c2s'],
        strings['results.list.csv.question.number'],
        strings['results.list.csv.question.text'],
        strings['results.list.csv.questionoption.number'],
        strings['results.list.csv.questionoption.text'],
        strings['results.list.csv.date']
    ]

    resultsExportService.writeResultsCsv(
        os,
        header,
        getSurvey(),
        periodStart,
        periodEnd,
        filter,
        accessNumber > 0 ? accessNumber : null,
        getTimeZone(),
        getLocale(),
        sortOrder)
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  void downloadCoupons(FacesContext context, OutputStream os) {
    def header = [
        strings['results.list.csv.msisdn'],
        strings['results.list.csv.c2s'],
        strings['results.list.csv.coupon']
    ]

    resultsExportService.writeCouponsCsv(
        os,
        header,
        getSurvey(),
        periodStart,
        periodEnd,
        filter,
        accessNumber > 0 ? accessNumber : null,
        getTimeZone(),
        getLocale(),
        sortOrder)
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  void downloadRespondents(FacesContext context, OutputStream os) {
    def header = [
        strings['results.list.csv.msisdn'],
        strings['results.list.csv.c2s'],
        strings['results.list.csv.start.date']
    ]

    resultsExportService.writeRespondentsCsv(
        os,
        header,
        getSurvey(),
        periodStart,
        periodEnd,
        filter,
        accessNumber > 0 ? accessNumber : null,
        getTimeZone(),
        getLocale(),
        sortOrder)
  }

  List<SelectItem> getAccessNumbers() {
    [
        new SelectItem(-1, strings['results.access.number.all']),
        accessNumberRepository.list(getSurvey()).collect { _ -> new SelectItem(_.id, _.number) }
    ].flatten() as List<SelectItem>
  }

  void deleteResults() {
    errorId = isEmpty(selectedRows) ? 'resultsDeleteNoResults' : 'resultsDeleteConfirmation'
  }

  void doDeleteResults() {
    respondentRepository.deleteAll selectedRows.collect { it.toInteger() }
    selectedRows = null

    FacesContext.currentInstance.externalContext
        .redirect("/pages/surveys/results.faces?id=${surveyId}")
  }
}
