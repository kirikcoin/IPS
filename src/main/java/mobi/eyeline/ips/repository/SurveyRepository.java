package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static mobi.eyeline.ips.model.Role.ADMIN;
import static mobi.eyeline.ips.model.Role.MANAGER;


public class SurveyRepository extends BaseRepository<Survey, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SurveyRepository.class);

    public SurveyRepository(DB db) {
        super(db);
    }

    // TODO: quite a strange request. Are we sure we want only the Group-By-Group type here?
    public List<Survey> listGroupByUser(User user,
                                        int limit,
                                        int offset) {

        final Session session = getSessionFactory().openSession();
        try {
            final Query query;

            if (user.getRole() == ADMIN || user.getRole() == MANAGER) {
                // Don't filter on restrictions.
                query = session.createQuery(
                        "select survey" +
                        " from Survey survey");

            } else {
                query = session.createQuery(
                        "select distinct perm.survey" +
                        " from UserSurveyPermissions perm" +
                        " where (perm.user.id = :id)")
                        .setProperties(user);
            }

            query.setFirstResult(offset).setMaxResults(limit);

            //noinspection unchecked
            return query.list();

        } finally {
            session.close();
        }
    }

    public List<Survey> listByUser(User user) {
        final Session session = getSessionFactory().openSession();
        try {
            if (user.getRole() == ADMIN || user.getRole() == MANAGER) {
                return list();

            } else {
                final Query query =
                        session.createQuery(
                        "select distinct perm.survey" +
                        " from UserSurveyPermissions perm" +
                        " where perm.user.id = :id")
                        .setProperties(user);

                //noinspection unchecked
                return query.list();
            }

        } finally {
            session.close();
        }
    }

    public int countGroupByUser(User user) {
        final Session session = getSessionFactory().openSession();
        try {
            final Query query;

            if (user.getRole() == ADMIN || user.getRole() == MANAGER) {
                // Don't filter on restrictions.
                query = session.createQuery(
                        "select count(survey)" +
                        " from Survey survey");

            } else {
                query = session.createQuery(
                        "select count(distinct perm.survey)" +
                        " from UserSurveyPermissions perm" +
                        " where (perm.user.id = :id)")
                        .setProperties(user);
            }

            final Number count = (Number) query.uniqueResult();
            return count.intValue();

        } finally {
            session.close();
        }
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
