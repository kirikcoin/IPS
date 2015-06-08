package mobi.eyeline.ips.web.controllers.surveys

import au.com.bytecode.opencsv.CSVWriter
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.Question
import mobi.eyeline.ips.model.QuestionOption
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.SurveyService
import mobi.eyeline.util.jsf.components.chart.pie.PieModel

import javax.annotation.PostConstruct
import javax.faces.bean.ViewScoped
import javax.faces.context.FacesContext
import javax.faces.model.SelectItem
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Slf4j('logger')
@Named
@ViewScoped
class SurveyResultsStatsController extends BaseSurveyReadOnlyController {

  @Inject private AnswerRepository answerRepository
  @Inject private RespondentRepository respondentRepository
  @Inject private SurveyService surveyService
  @Inject private AccessNumberRepository accessNumberRepository

  Date periodStart
  Date periodEnd
  Integer accessNumber

  @PostConstruct
  void init() {
    periodStart = survey.startDate
    periodEnd = survey.endDate
  }

  List<SelectItem> getAccessNumbers() {
    [
        new SelectItem(-1, strings['results.access.number.all']),
        accessNumberRepository.list(getSurvey()).collect { _ -> new SelectItem(_.id, _.number) }
    ].flatten() as List<SelectItem>
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  void download(FacesContext context, OutputStream os) {
    os.withWriter('UTF-8') { Writer writer ->
      final csvWriter = new CSVWriter(writer, ';' as char)
      try {
        // Add header line.
        csvWriter.writeNext([
            strings['results.list.csv.question.number'],
            strings['results.list.csv.question.text'],
            strings['results.list.csv.questionoption.number'],
            strings['results.list.csv.questionoption.text'],
            strings['survey.stats.count']
        ] as String[])

        survey.activeQuestions.each { q ->
          q.activeOptions.each { opt ->
            csvWriter.writeNext([
                q.activeIndex + 1,
                q.title,
                opt.activeIndex + 1,
                opt.answer,
                count(opt)
            ] as String[])
          }

          if (q.enabledDefaultAnswer) {
            csvWriter.writeNext([
                q.activeIndex + 1,
                q.title,
                -1,
                '',
                getTextAnswersCount(q)
            ] as String[])
          }
        }
      } finally {
        csvWriter.close()
      }
    }
  }

  private String getSource() {
    (accessNumber && accessNumber > 0) ? accessNumberRepository.load(accessNumber).number : null
  }

  PieModel getOptionsRatioModel(Question question) {
    new PieModel().with {
      question.activeOptions.each { opt -> addPart "${opt.answer}", count(opt) }
      addPart strings['survey.stats.arbitrary.answer'] as String, getTextAnswersCount(question)

      it as PieModel
    }
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  String colorLoop(QuestionOption opt) {
    def colors = ['green', '#adff2f', 'blue', 'yellow', 'black', 'magenta']
    return colors[opt.activeIndex % colors.size()]
  }

  int count(QuestionOption opt) {
    answerRepository.count opt, periodStart, periodEnd, source
  }

  int getTextAnswersCount(Question question) {
    answerRepository.countTextAnswers question, periodStart, periodEnd, source
  }

  int getTotalCount(Question question) {
    answerRepository.count question, periodStart, periodEnd, source
  }
}
