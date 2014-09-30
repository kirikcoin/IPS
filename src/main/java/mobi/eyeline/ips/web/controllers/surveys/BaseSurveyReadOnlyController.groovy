package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController

import javax.faces.bean.ManagedBean
import javax.faces.context.FacesContext

@CompileStatic
@ManagedBean(name = "baseSurveyReadOnlyController")
class BaseSurveyReadOnlyController extends BaseController {
    protected final SurveyRepository surveyRepository = Services.instance().surveyRepository

    Integer surveyId
    Survey survey

    BaseSurveyReadOnlyController() {
        surveyId = getRequest().getParameter("id")?.toInteger()
        survey = surveyRepository.load(surveyId)

        if (!survey.active) {
            // Prohibit display of deleted surveys.
            FacesContext.currentInstance.externalContext.redirect '/error.faces?id=404'
        }
    }

    Survey getSurvey() {
        return survey
    }
}
