package mobi.eyeline.ips.web.controllers.surveys

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

import static mobi.eyeline.ips.model.InvitationUpdateStatus.UNDEFINED

class SurveyInvitesController extends BaseSurveyController {

    private static final Logger logger = LoggerFactory.getLogger(SurveyInvitesController)

    private final SurveyInvitationRepository surveyInvitationRepository =
            Services.instance().surveyInvitationRepository
    private final SurveyRepository surveyRepository = Services.instance().surveyRepository
    private final MadvUpdateService madvUpdateService = Services.instance().madvUpdateService

    // MADV campaign
    String madvId
    boolean madvIdError

    // Invitations
    Date inviteDate = new Date()
    String inviteValue
    boolean inviteError

    SurveyInvitesController() {
        madvId = survey.statistics.campaign
    }

    boolean isCampaignDefined() { survey.statistics.campaign != null }

    DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int offset,
                         int limit,
                         DataTableSortOrder sortOrder) {

                def list = surveyInvitationRepository.list(
                            SurveyInvitesController.this.survey,
                            sortOrder.columnId,
                            sortOrder.asc,
                            limit,
                            offset)

                return list.collect {
                    new TableItem(
                            id: it.id,
                            date: it.date,
                            value: it.value
                    )
                }
            }

            @Override
            int getRowsCount() {
                surveyInvitationRepository.count(SurveyInvitesController.this.survey)
            }
        }
    }

    void addInvite() {
        int inviteValueInt = 0
        try {
            inviteValueInt = Integer.parseInt(inviteValue)
        } catch (Exception ignored) {}

        def invite = new SurveyInvitation(
                survey: surveyRepository.load(surveyId),
                date: inviteDate,
                value: inviteValueInt)

        inviteError = renderViolationMessage(validator.validate(invite), [
                'date': 'inviteDate',
                'value': 'inviteValue'
        ])
        if (!inviteError) {
            surveyInvitationRepository.save(invite)
        }
    }

    void deleteInvite() {
        int id = getParamValue("inviteId").asInteger()
        surveyInvitationRepository.delete(surveyInvitationRepository.load(id))
    }

    void onMadvEditSave() {
        if (StringUtils.isInteger(madvId)) {
            survey.statistics.with {
                campaign = madvId
                sentCount = 0
                lastUpdate = null
                updateStatus = UNDEFINED
            }
            surveyRepository.update(survey)
            madvUpdateService.runNow(survey.id)

            madvIdError = false

        } else {
            addErrorMessage(
                    resourceBundle.getString('invitations.block.advertising.company.dialog.id.error'),
                    'newIdentifier')
            madvIdError = true
        }
    }

    void onMadvEditCancel() {
        madvId = survey.statistics.campaign
    }

    void clearMadvId() {
        survey.statistics.with {
            campaign = null
            sentCount = 0
            lastUpdate = null
            updateStatus = UNDEFINED
        }
        surveyRepository.update(survey)

        madvId = null
    }

    void updateSentCount() { madvUpdateService.runNow() }

    int getTotalInvitations() {
        surveyInvitationRepository.list(survey).collect {it.value}.sum(0) as int
    }

    static class TableItem implements Serializable {
        int id
        Date date
        int value
    }

}