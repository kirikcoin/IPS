package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

@CompileStatic
class BaseSurveyReadOnlyController extends BaseController {
    protected final SurveyRepository surveyRepository = Services.instance().surveyRepository

    Integer surveyId
    Survey survey

    BaseSurveyReadOnlyController() {
        surveyId = getRequest().getParameter("id")?.toInteger()
        survey = surveyRepository.load(surveyId)
    }

    Survey getSurvey() {
        return survey
    }
}
