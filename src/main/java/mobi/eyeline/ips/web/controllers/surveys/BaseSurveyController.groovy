package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey

import javax.annotation.PostConstruct
import javax.enterprise.inject.Model

@CompileStatic
@Model
class BaseSurveyController extends BaseSurveyReadOnlyController {

  Survey persistedSurvey

  @PostConstruct
  void initPersistedSurvey() {
    persistedSurvey = surveyRepository.load(surveyId)
  }
}
