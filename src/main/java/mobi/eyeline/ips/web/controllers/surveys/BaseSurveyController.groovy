package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

class BaseSurveyController extends BaseController {
    protected final SurveyRepository surveyRepository = Services.instance().surveyRepository

    Survey survey
    Integer surveyId

    BaseSurveyController() {
        surveyId = getRequest().getParameter("id")?.toInteger()
        survey = surveyRepository.load(surveyId)

    }
}
