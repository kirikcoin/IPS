package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.AnswerRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.SurveyService
import mobi.eyeline.util.jsf.components.chart.bar.BarModel
import mobi.eyeline.util.jsf.components.chart.line.LineModel

import javax.annotation.PostConstruct
import javax.faces.bean.ViewScoped
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Slf4j('logger')
@Named
@ViewScoped
class SurveyC2sStatsController extends BaseSurveyReadOnlyController {

  @Inject private RespondentRepository respondentRepository
  @Inject private AccessNumberRepository accessNumberRepository

  Date periodStart
  Date periodEnd

  @PostConstruct
  void init() {
    periodStart = survey.startDate
    periodEnd = survey.endDate
  }

  List<AccessNumber> getAccessNumbers() {
    accessNumberRepository.list(getSurvey())
  }

  @SuppressWarnings("GrMethodMayBeStatic")
  String colorLoop(AccessNumber num) {
    final colors = ['green', '#adff2f', 'blue', 'yellow', 'black', 'magenta']

    final idx = accessNumbers.indexOf(num)
    return colors[idx % colors.size()]
  }

  BarModel getBarModel() {
    final model = new BarModel()
    model.addSection(
        ''
    ).with {
      accessNumbers.each { num ->
        addValue(
            num.number,
            respondentRepository.countBySurvey(getSurvey(), periodStart, periodEnd, true, num.number))
      }
    }

    model
  }

  @Memoized
  LineModel<Date, Number> getLineModel() {
    final model = LineModel.createDateModel()

    datePoints.each { date ->
      accessNumbers.each { num ->
        final count = respondentRepository.countBySurvey(
            getSurvey(),
            date,
            date + 1,
            true,
            num.number)

        model.addPoint num.number, date, count
      }
    }

    model
  }

  List<Date> getDatePoints() {
    Date start = periodStart.clone() as Date
    start.clearTime()

    final rc = []
    while (start < periodEnd) {
      rc << (start.clone() as Date)
      start += 1
    }

    rc
  }

}
