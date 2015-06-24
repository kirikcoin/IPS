package mobi.eyeline.ips.web.controllers.stats

import au.com.bytecode.opencsv.CSVWriter
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.util.DataTableUtil
import mobi.eyeline.ips.util.LocaleUtil
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.model.SelectItem
import javax.faces.view.ViewScoped
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Slf4j('logger')
@Named('globalStatController')
@ViewScoped
class GlobalStatController extends BaseController {

  @Inject private transient AccessNumberRepository accessNumberRepository
  @Inject private transient AnswerRepository answerRepository
  @Inject private transient RespondentRepository respondentRepository
  @Inject private transient SurveyRepository surveyRepository

  Date periodStart
  Date periodEnd
  String filter
  Integer accessNumber

  boolean showEmptyRows

  @PostConstruct
  void init() {
    periodStart = new Date().clearTime() - 30
    periodEnd = new Date().clearTime()
  }

  DataTableModel getModel() {
    List<C2sResponseModel> list = loadResults(periodStart, periodEnd, filter, accessNumber)
    if (!showEmptyRows) {
      list = list.findAll { it.nRespondents > 0 } as List
    }

    return new DataTableModel() {
      @Override
      List getRows(int startPos, int count, DataTableSortOrder sortOrder) {
        def slice = sortOrder ? DataTableUtil.sort(list, sortOrder) : list
        slice[startPos..<Math.min(startPos + count, rowsCount)]
      }

      final int rowsCount = list.size()
    }
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  void download(FacesContext context, OutputStream os) {
    final header = [
        strings['survey.list.table.id'],
        strings['global.stats.c2s.survey.name'],
        strings['results.access.number'],
        strings['global.stats.c2s.respondents.count']
    ]

    os.withWriter(LocaleUtil.exportCharset) { Writer writer ->
      final CSVWriter csvWriter = new CSVWriter(writer, ';' as char)

      try {
        // Add header line.
        csvWriter.writeNext(header as String[])

        List<C2sResponseModel> results = loadResults(periodStart, periodEnd, filter, accessNumber)
        if (!showEmptyRows) {
          results = results.findAll { it.nRespondents > 0 } as List
        }

        results.each { _ ->
          csvWriter.writeNext([
              _.surveyId,
              _.surveyName,
              _.c2s,
              _.nRespondents
          ] as String[])
        }

      } finally {
        csvWriter.close()
      }
    }
  }

  List<SelectItem> getAccessNumbers() {
    [
        new SelectItem(-1, strings['results.access.number.all']),
        accessNumberRepository.list().collect { _ -> new SelectItem(_.id, _.number) }
    ].flatten() as List<SelectItem>
  }

  @Memoized
  private List<C2sResponseModel> loadResults(Date from, Date to, String filter, Integer c2s) {
    final numbers = c2s && c2s > 0 ?
        [accessNumberRepository.load(c2s).number] :
        accessNumberRepository.list().collect { it.number }

    loadSurveys(filter).collectMany { s ->
      final Map<String, Integer> perSurvey = numbers.collectEntries { [(it): 0] }
      perSurvey.putAll(respondentRepository.countBySurvey(s, from, to, numbers)?:[:])

      perSurvey.collect { src, count ->
        new C2sResponseModel(
            surveyId: s.id,
            surveyName: s.details.title,
            c2s: src,
            nRespondents: count
        )
      }
    }
  }

  @Memoized
  private List<Survey> loadSurveys(String filter) {
    surveyRepository.list(
        isManagerRole() ? null : getCurrentUser(),
        isManagerRole() && getCurrentUser().onlyOwnSurveysVisible ? getCurrentUser() : null,
        filter,
        true,
        null,
        true,
        Integer.MAX_VALUE,
        0)
  }

  static class C2sResponseModel {
    int surveyId
    String surveyName
    String c2s
    int nRespondents
  }

}
