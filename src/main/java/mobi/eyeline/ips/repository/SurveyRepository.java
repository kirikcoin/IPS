package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Role;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.ilike;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.criterion.Restrictions.sqlRestriction;


public class SurveyRepository extends BaseRepository<Survey, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SurveyRepository.class);

    public SurveyRepository(DB db) {
        super(db);
    }

    static DetachedCriteria getCriteriaSurvey(User user) {
        final DetachedCriteria criteria = DetachedCriteria
                .forClass(Survey.class)
                .createAlias("details", "details")
                .createAlias("client", "client");

        if (user.getRole() == Role.CLIENT) {
            criteria.add(Restrictions.eq("client.id", user.getId()));
        }
        return criteria;
    }

    public List<Survey> list(User user,
                             String filter,
                             Boolean active,
                             String orderProperty,
                             boolean asc,
                             int limit,
                             int offset) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = session.createCriteria(Survey.class);

        criteria.createAlias("details", "details");
        criteria.createAlias("statistics", "statistics");
        criteria.createAlias("client", "client");

        if (active != null) {
            criteria.add(Restrictions.eq("active", active));
        }

        if (user != null) {
            criteria.add(Restrictions.eq("client.id", user.getId()));
        }

        if (isNotBlank(filter)) {
            filter = filter.trim();

            final List<Criterion> filters = new ArrayList<>();
//            filters.add(sqlRestriction("id LIKE '%" + filter + "%'"));
            filters.add(ilike("id", filter, MatchMode.ANYWHERE));
            filters.add(ilike("details.title", filter, MatchMode.ANYWHERE));
            filters.add(ilike("statistics.accessNumber", filter, MatchMode.ANYWHERE));
            if (user == null) {
                filters.add(ilike("client.fullName", filter, MatchMode.ANYWHERE));
            }

            criteria.add(or(
                    filters.toArray(new Criterion[filters.size()])
            ));
        }

        criteria.setFirstResult(offset).setMaxResults(limit);

        if (orderProperty != null) {

            final String property;
            switch (orderProperty) {
                case "id":          property = "id"; break;
                case "title":       property = "details.title"; break;
                case "client":      property = "client.fullName"; break;
                case "state":       property = "startDate"; break;
                case "period":       property = "startDate"; break;
                case "accessNumber":       property = "statistics.accessNumber"; break;
                default:
                    throw new RuntimeException("Unexpected sort column: " + orderProperty);
            }

            criteria.addOrder(asc ? Order.asc(property) : Order.desc(property));
        }

        //noinspection unchecked
        return (List<Survey>) criteria.list();
    }

    public int count(User user,
                     String filter,
                     Boolean active) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = session.createCriteria(Survey.class);

        criteria.createAlias("details", "details");
        criteria.createAlias("statistics", "statistics");
        criteria.createAlias("client", "client");

        if (active != null) {
            criteria.add(Restrictions.eq("active", active));
        }

        if (user != null) {
            criteria.add(Restrictions.eq("client.id", user.getId()));
        }

        if (isNotBlank(filter)) {
            filter = filter.trim();

            final List<Criterion> filters = new ArrayList<>();
//            filters.add(sqlRestriction("id LIKE '%" + filter + "%'"));
            filters.add(ilike("details.title", filter, MatchMode.ANYWHERE));
            filters.add(ilike("statistics.accessNumber", filter, MatchMode.ANYWHERE));
            if (user == null) {
                filters.add(ilike("client.fullName", filter, MatchMode.ANYWHERE));
            }

            criteria.add(or(
                    filters.toArray(new Criterion[filters.size()])
            ));
        }

        criteria.setProjection(Projections.rowCount());

        //noinspection unchecked
        return ((Number) criteria.uniqueResult()).intValue();
    }

    public List<Survey> listActiveGroup() {
        final Session session = getSessionFactory().openSession();
        try {
            final Query query = session.createQuery(
                    "from Survey " +
                            " where active = true" +
                            " order by id");

            //noinspection unchecked
            return query.list();

        } finally {
            session.close();
        }
    }
    
    public String getLastSurveyArchiveDateLabel(Survey survey) {
        final Session session = getSessionFactory().openSession();
        try {
            final List<String> names =
                    listTablesLike(session, "lime_old_survey_" + survey.getId() + "%");

            // Should we use number ordering?
            final String lastTableName = Collections.max(names);
            return lastTableName.substring(lastTableName.lastIndexOf('_'), lastTableName.length());

        } finally {
            session.close();
        }
    }

    private static class CountSurveyAnswersModel {
        private final boolean existSurveyTable;
        private final int countAnswer;

        private CountSurveyAnswersModel(Boolean existSurveyTable, int countAnswer) {
            this.existSurveyTable = existSurveyTable;
            this.countAnswer = countAnswer;
        }

        public boolean isExistSurveyTable() {
            return existSurveyTable;
        }

        public int getCountAnswer() {
            return countAnswer;
        }
    }

    private CountSurveyAnswersModel countSurveyAnswers(Session session, Survey survey) {
        final String surveyTable =
                LimeSurveyUtils.surveyTable(survey.getId());

        if (tableExists(session, surveyTable)) {
            return new CountSurveyAnswersModel(true, count(session, surveyTable));

        } else {
            return new CountSurveyAnswersModel(false, 0);
        }
    }

}
