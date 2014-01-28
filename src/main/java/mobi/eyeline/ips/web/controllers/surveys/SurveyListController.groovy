package mobi.eyeline.ips.web.controllers.surveys

import mobi.eyeline.ips.model.Survey
import mobi.eyeline.ips.repository.SurveyRepository
import mobi.eyeline.ips.repository.UserRepository
import mobi.eyeline.ips.service.Services
import mobi.eyeline.ips.web.controllers.BaseController
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

import static mobi.eyeline.ips.web.controllers.surveys.SurveyListController.SurveyState.FINISHED
import static mobi.eyeline.ips.web.controllers.surveys.SurveyListController.SurveyState.IN_PROGRESS
import static mobi.eyeline.ips.web.controllers.surveys.SurveyListController.SurveyState.NOT_STARTED

class SurveyListController extends BaseController {

    String search

    private final SurveyRepository surveyRepository = Services.instance().surveyRepository
    private final UserRepository userRepository = Services.instance().userRepository


    public DataTableModel getTableModel() {

        return new DataTableModel() {
            @Override
            public List getRows(int offset,
                                int limit,
                                DataTableSortOrder sortOrder) {

                def list = surveyRepository.list(
                        managerRole ? null : userRepository.getByLogin(userName),
                        search,
                        true,
                        sortOrder.columnId,
                        sortOrder.asc,
                        limit,
                        offset)

                return list.collect {
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
                        managerRole ? null : userRepository.getByLogin(userName),
                        search,
                        true)
            }
        }
    }

    static enum SurveyState {

        /**
         * now < {@link Survey#startDate}
         */
        NOT_STARTED,

        /**
         * {@link Survey#startDate} <= now <= {@link Survey#endDate}
         */
        IN_PROGRESS,

        /**
         * {@link Survey#endDate} < now
         */
        FINISHED
    }

    static class TableItem implements Serializable {

        int id

        String title
        String client

        Date startDate
        Date endDate

        String accessNumber

        public SurveyState getState() {
            final Date now = new Date()

            if (startDate.after(now))                           return NOT_STARTED
            if (startDate.before(now) && now.before(endDate))   return IN_PROGRESS
            if (endDate.before(now))                            return FINISHED

            throw new AssertionError("Could not determine survey execution state")
        }
    }
}
