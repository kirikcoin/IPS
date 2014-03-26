package mobi.eyeline.ips.web.controllers.surveys

import groovy.transform.CompileStatic
import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyDetails
import mobi.eyeline.ips.model.SurveyStats
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory


@CompileStatic
class SurveyListController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SurveyListController)

    private final SurveyRepository surveyRepository = Services.instance().surveyRepository
    private final UserRepository userRepository = Services.instance().userRepository

    //
    //  List
    //

    String search


    //
    //  New survey creation.
    //

    String newSurveyTitle

    Date newSurveyStartDate = new Date().plus(1).clearTime()
    Date newSurveyEndDate = newSurveyStartDate.plus(7)

    Integer newSurveyClientId

    boolean newSurveyValidationError = false

    public DataTableModel getTableModel() {

        return new DataTableModel() {
            @Override
            public List getRows(int offset,
                                int limit,
                                DataTableSortOrder sortOrder) {

                def list = surveyRepository.list(
                        isManagerRole() ? null : getCurrentUser(),
                        search,
                        true,
                        sortOrder.columnId,
                        sortOrder.asc,
                        limit,
                        offset)

                return list.collect { Survey it ->
                    new TableItem(
                            id: it.id,
                            title: it.details.title,
                            client: it.client?.fullName,
                            startDate: it.startDate,
                            endDate: it.endDate,
                            accessNumber: it.statistics.accessNumber)
                }
            }

            @Override
            public int getRowsCount() {
                surveyRepository.count(
                        isManagerRole() ? null : getCurrentUser(),
                        search,
                        true)
            }
        }
    }

    void createSurvey() {

        def checkClientIdUnset = {
            if (newSurveyClientId == null) {
                addErrorMessage(strings['survey.validation.client.empty'], 'clients')
                return true
            }
            return false
        }

        def checkStartInTheFuture = {
            if (newSurveyStartDate != null && newSurveyStartDate.before(new Date())) {
                addErrorMessage(
                        strings['survey.validation.start.date.future'],
                        'newSurveyStartDate')
                return true
            }
            return false
        }

        newSurveyValidationError = checkClientIdUnset()
        if (newSurveyValidationError) {
            return  // Can't check further
        }

        newSurveyValidationError |= checkStartInTheFuture()

        def survey = new Survey(
                startDate: newSurveyStartDate,
                endDate: newSurveyEndDate,
                active: true,
                client: userRepository.load(newSurveyClientId))
        survey.details = new SurveyDetails(survey: survey, title: newSurveyTitle)
        survey.statistics = new SurveyStats(survey: survey)

        newSurveyValidationError |= renderViolationMessage(
                validator.validate(survey),
                [
                        'details.title': 'newSurveyTitle',
                        'startDate': 'newSurveyStartDate',
                        'endDate': 'newSurveyEndDate',
                ])

        if (newSurveyValidationError) {
            // Stay on the current page.
            return
        }

        def surveyId = surveyRepository.save(survey)
        SurveySettingsController.goToSurvey(surveyId)
    }

    void surveyClickHandler() {
        def surveyId = getParamValue("surveyId").asInteger()

        SurveySettingsController.goToSurvey(surveyId)
    }

    static class TableItem implements Serializable {

        int id

        String title
        String client

        Date startDate
        Date endDate

        String accessNumber
    }
}
