package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey

import javax.faces.bean.ManagedBean

@CompileStatic
@ManagedBean(name = "baseSurveyController")
class BaseSurveyController extends BaseSurveyReadOnlyController {

  Survey persistedSurvey

  BaseSurveyController() {
    persistedSurvey = surveyRepository.load(surveyId)
  }
}
