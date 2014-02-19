package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.model.SurveyInvitation
import mobi.eyeline.ips.repository.SurveyInvitationRepository
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.service.MadvUpdateService
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.util.StringUtils
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

class SurveyInvitesController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyInvitesController)
    private final SurveyInvitationRepository surveyInvitationRepository =
            Services.instance().surveyInvitationRepository
    private final SurveyRepository surveyRepository = Services.instance().surveyRepository
    private final MadvUpdateService madvUpdateService = Services.instance().madvUpdateService


    Date newChannelDate = new Date().clearTime()
    boolean chanelError
    boolean identifierError
    int newChannelNumber
    boolean campaignDefined
    String newCampaignIdentifier
    String campaign
    Survey currentSurvey = survey

    SurveyInvitesController() {
            campaignDefined = isNotEmpty(survey.getStatistics().campaign)
            campaign = survey.getStatistics().campaign

    }

    DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int offset,
                         int limit,
                         DataTableSortOrder sortOrder) {
                def list = surveyInvitationRepository.list(
                            currentSurvey,
                            sortOrder.columnId,
                            sortOrder.asc,
                            limit,
                            offset)

                return list.collect {
                    new TableItem(
                            id: it.id,
                            date: it.date,
                            number: it.value
                    )
                }
            }

            @Override
            int getRowsCount() {
                surveyInvitationRepository.count(currentSurvey)
            }
        }
    }

    // TODO: rename `addChannel' to something more meaningful,
    // keeping in mind that the entity is called `SurveyInvitation'.
    void addChannel() {
        SurveyInvitation surveyInvitation = new SurveyInvitation(
                survey: surveyRepository.get(surveyId),
                date: newChannelDate,
                value: newChannelNumber,
        )
        chanelError =
                renderViolationMessage(validator.validate(surveyInvitation),
                        [
                                'date': 'newChannelDate',
                                'value': 'newChannelNumber',

                        ])
        if(chanelError) {
            return
        }

        surveyInvitationRepository.save(surveyInvitation)
    }

    void deleteChannel() {
        // TODO: rename `channelId' to something more meaningful,
        // keeping in mind that the entity is called `SurveyInvitation'.
        int id = getParamValue("channelId").asInteger()
        surveyInvitationRepository.delete(surveyInvitationRepository.load(id))
    }

    void addCampaignIdentifier() {
        if(StringUtils.isInteger(newCampaignIdentifier)){
            survey.getStatistics().campaign = newCampaignIdentifier
            survey.getStatistics().sentCount = 0
            surveyRepository.update(survey)
            return
        } else {
            addErrorMessage(getResourceBundle().getString("client.dialog.validation.login.exists"),
                    "newIdentifier")
            identifierError = true
        }

        if(identifierError) {
            return
        }

    }

    void updateSentCount() {
        madvUpdateService.runNow()
    }

    static class TableItem implements Serializable {
        int id
        Date date
        int number
    }


}