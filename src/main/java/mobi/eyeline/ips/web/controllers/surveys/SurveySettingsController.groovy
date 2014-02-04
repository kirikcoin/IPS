package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.faces.FacesException
import javax.faces.component.UIViewRoot
import javax.faces.context.ExternalContext
import javax.faces.context.FacesContext

class SurveySettingsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SurveySettingsController)

    private final SurveyRepository surveyRepository = Services.instance().surveyRepository
    private final UserRepository userRepository = Services.instance().userRepository

//    String tabStyles = "tab_active,tab,tab,tab"

    Survey survey

    Integer surveyId

//    int selectedTab = 0

    String errorId

    int newSurveyClientId

    SurveySettingsController() {
        surveyId = getRequest().getParameter("id")?.toInteger()

        survey = surveyRepository.load(surveyId)
        newSurveyClientId = survey.client.id
    }

//    void selectTab() {
//        selectedTab = getParamValue("tab_id").asInteger()
//
//        def sb = new StringBuilder()
//        for (i in 0..3) {
//            if (sb.length() > 0) {
//                sb << ","
//            }
//
//            sb << ((i == selectedTab) ? "tab_active" : "tab")
//        }
//        tabStyles = sb.toString()
//
//        redirect()
//    }

    void saveMessage() {
        boolean validationError =
                renderViolationMessage(validator.validate(survey.details))
        if (validationError) {
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        surveyRepository.update(survey)
        goToSurvey(surveyId)
    }

    void saveSettings() {
        survey.client = userRepository.load(newSurveyClientId)

        boolean validationError = renderViolationMessage(
                validator.validate(survey),
                [
                        'details.title': 'newSurveyTitle',
                        'startDate': 'newSurveyStartDate',
                        'endDate': 'newSurveyEndDate',
                        'statistics.accessNumber': 'newAccessNumber'
                ])

        if (validationError) {
            surveyRepository.refresh(survey)
            survey.details.title = '111'
            this.errorId =
                    FacesContext.currentInstance.externalContext.requestParameterMap["errorId"]
            return
        }

        surveyRepository.update(survey)
        goToSurvey(surveyId)
    }

    String deleteSurvey() {
        // Feels safer to reload
        def survey = surveyRepository.load(surveyId)
        survey.active = false
        surveyRepository.update(survey)

        return "SURVEY_LIST"
    }

    static void goToSurvey(int surveyId) {
        FacesContext.currentInstance.externalContext
                .redirect("/pages/surveys/settings.faces?id=${surveyId}")
    }

//    private void redirect() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ext = context.getExternalContext();
//        UIViewRoot view = context.getViewRoot();
//        String actionUrl = context.getApplication().getViewHandler().getActionURL(
//                context, view.getViewId());
//        try {
//            // TODO encode id value
//            actionUrl = ext.encodeActionURL(actionUrl + "?id=" + surveyId);
//            ext.redirect(actionUrl);
//        } catch (IOException e) {
//            throw new FacesException(e);
//        }
//    }
}
