package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

@CompileStatic
class BaseSurveyController extends BaseSurveyReadOnlyController {

    Survey persistedSurvey

    BaseSurveyController() {
        persistedSurvey = surveyRepository.load(surveyId)
    }
}
