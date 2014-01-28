package mobi.eyeline.ips.web.controllers.surveys;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.repository.SurveyRepository;
import mobi.eyeline.ips.repository.UserRepository;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.web.controllers.BaseController;
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel;
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder;
import mobi.eyeline.util.jsf.components.data_table.model.ModelException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SurveyListController extends BaseController {

    private String search;

    private final SurveyRepository surveyRepository = Services.instance().getSurveyRepository();
    private final UserRepository userRepository = Services.instance().getUserRepository();

    public SurveyListController() {}

    private List<Survey> listSurveys(int offset,
                                     int limit,
                                     DataTableSortOrder sortOrder) {

        final User owner =
                isManagerRole() ? null : userRepository.getByLogin(getUserName());
        return surveyRepository.list(
                owner,
                getSearch(),
                true,
                sortOrder.getColumnId(),
                sortOrder.isAsc(),
                limit,
                offset);
    }

    private int countSurveys() {
        final User owner =
                isManagerRole() ? null : userRepository.getByLogin(getUserName());
        return surveyRepository.count(
                owner,
                getSearch(),
                true);
    }

    public DataTableModel getTableModel() {
        return new DataTableModel() {
            @Override
            public List getRows(int offset,
                                int limit,
                                DataTableSortOrder sortOrder) throws ModelException {

                final List<Survey> rawResults = listSurveys(offset, limit, sortOrder);

                final List<TableItem> results = new ArrayList<>();
                for (Survey survey : rawResults) {
                    final TableItem item = new TableItem();

                    item.setId(survey.getId());
                    item.setTitle(survey.getDetails().getTitle());
                    item.setClient((survey.getClient() != null) ? survey.getClient().getFullName() : null);
                    item.setStartDate(survey.getStartDate());
                    item.setEndDate(survey.getEndDate());
                    item.setAccessNumber(survey.getStatistics().getAccessNumber());

                    results.add(item);
                }

                return results;
            }

            @Override
            public int getRowsCount() throws ModelException {
                return countSurveys();
            }
        };
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }


    public static enum SurveyState {

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

    public static class TableItem implements Serializable {

        private long id;

        private String title;
        private String client;

        private Date startDate;
        private Date endDate;

        private String accessNumber;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getAccessNumber() {
            return accessNumber;
        }

        public void setAccessNumber(String accessNumber) {
            this.accessNumber = accessNumber;
        }

        public SurveyState getState() {
            final Date now = new Date();
            if (now.compareTo(startDate) < 0) {
                return SurveyState.NOT_STARTED;
            } else if (now.compareTo(startDate) >= 0 && now.compareTo(endDate) <= 0) {
                return SurveyState.IN_PROGRESS;
            } else if (now.compareTo(endDate) > 0) {
                return SurveyState.FINISHED;
            } else {
                throw new AssertionError("Could not determine survey execution state");
            }
        }
    }
}
