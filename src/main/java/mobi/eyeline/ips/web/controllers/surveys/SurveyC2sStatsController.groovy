package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.util.logging.Slf4j
import mobi.eyeline.ips.model.AccessNumber
import mobi.eyeline.ips.repository.AccessNumberRepository
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.web.controllers.ColorLoop
import mobi.eyeline.util.jsf.components.chart.bar.BarModel
import mobi.eyeline.util.jsf.components.chart.line.LineModel

import javax.annotation.PostConstruct
import javax.faces.view.ViewScoped
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@Slf4j('logger')
@Named
@ViewScoped
class SurveyC2sStatsController extends BaseSurveyReadOnlyController {

  @Inject private transient RespondentRepository respondentRepository
  @Inject private transient AccessNumberRepository accessNumberRepository

  Date periodStart
  Date periodEnd

  @PostConstruct
  void init() {
    periodStart = survey.startDate.clearTime()
    periodEnd = survey.endDate.clearTime()
  }

  List<AccessNumber> getAccessNumbers() {
    accessNumberRepository.list(getSurvey())
  }

  @SuppressWarnings('GrMethodMayBeStatic')
  String colorLoop(AccessNumber num) { ColorLoop.colorLoop accessNumbers.indexOf(num) }

  BarModel getBarModel() {
    final model = new BarModel()
    model.addSection(
        ''
    ).with {

      final from = (periodStart.clone() as Date).clearTime() - 1
      final to = (periodEnd.clone() as Date).clearTime()

      accessNumbers.each { num ->
        addValue(num.number, getCount(from, to, num.number))
      }
    }

    model
  }

  LineModel<Date, Number> getLineModel() {
    final model = LineModel.createDateModel()

    datePoints.each { date ->
      accessNumbers.each { num ->
        model.addPoint num.number, date, getCount(date, date + 1, num.number)
      }
    }

    model
  }

  @Memoized
  private int getCount(Date from, Date to, String number) {
    respondentRepository.countBySurvey(getSurvey(), from, to, true, number)
  }

  List<Date> getDatePoints() {
    Date start = periodStart.clone() as Date
    start.clearTime()
    start -= 1

    final rc = []
    while (start < periodEnd) {
      rc << (start.clone() as Date)
      start += 1
    }

    rc
  }

}
