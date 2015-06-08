package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.repository.RespondentRepository
import mobi.eyeline.ips.service.SurveyService
import mobi.eyeline.util.jsf.components.chart.bar.BarModel

import javax.enterprise.inject.Model
import javax.inject.Inject

@CompileStatic
@Model
class SurveyDeliveryStatsController extends BaseSurveyReadOnlyController {

  @Inject private RespondentRepository respondentRepository
  @Inject private SurveyService surveyService

  BarModel getInvitationsRatioModel() {
    final model = new BarModel()

    model.addSection(
        ''
    ).with {
      addValue(strings['survey.stats.overall.respondents.invitations'] as String,
          surveyService.countInvitations(survey))
      addValue(strings['survey.stats.overall.respondents.respondents'] as String,
          // The ones with `null' source come from deliveries.
          respondentRepository.countBySurvey(survey, null, null, true, null))
      addValue(strings['survey.stats.overall.respondents.finished'] as String,
          respondentRepository.countFinishedBySurvey(survey, true, null))
    }

    return model
  }

}
