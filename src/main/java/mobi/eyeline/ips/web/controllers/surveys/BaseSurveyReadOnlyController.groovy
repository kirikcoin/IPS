package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.web.controllers.BaseController

import javax.annotation.PostConstruct
import javax.enterprise.context.RequestScoped
import javax.faces.context.ExternalContext
import javax.inject.Inject
import javax.inject.Named

@CompileStatic
@RequestScoped
@Named("baseSurveyReadOnlyController")
class BaseSurveyReadOnlyController extends BaseController {
  @Inject protected transient SurveyRepository surveyRepository
  @Inject protected transient ExternalContext externalContext

  Integer surveyId
  Survey survey

  @PostConstruct
  void initSurvey() {
    surveyId = getRequest().getParameter("id")?.toInteger()
    survey = surveyRepository.load(surveyId)

    if (!survey.active) {
      // Prohibit display of deleted surveys.
      externalContext.redirect '/error.faces?id=404'
    }
  }

  Survey getSurvey() {
    return survey
  }
}
