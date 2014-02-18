package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.SurveyInvitation
import mobi.eyeline.ips.repository.SurveyInvitationRepository
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SurveyInvitesController extends BaseSurveyController {
    private static final Logger logger = LoggerFactory.getLogger(SurveyInvitesController)
    private final SurveyInvitationRepository surveyInvitationRepository =
            Services.instance().surveyInvitationRepository
    private final SurveyRepository surveyRepository = Services.instance().surveyRepository

    Date newChannelDate = new Date().clearTime()
    boolean chanelError
    int newChannelNumber


    public DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            List getRows(int offset,
                         int limit,
                         DataTableSortOrder sortOrder) {
                def list = surveyInvitationRepository.list(
                            sortOrder.columnId,
                            sortOrder.asc,
                            limit,
                            offset)

                return list.collect {
                    new TableItem(
                            date: it.date,
                            number: it.value
                    )
                }
            }

            @Override
            int getRowsCount() {
                surveyInvitationRepository.count()
            }
        }
    }

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

    static class TableItem implements Serializable {
        Date date
        int number
    }


}
