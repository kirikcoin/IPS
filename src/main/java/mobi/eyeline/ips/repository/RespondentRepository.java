package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.not;

public class RespondentRepository extends BaseRepository<Respondent, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(RespondentRepository.class);

    public RespondentRepository(DB db) {
        super(db);
    }

    public boolean isRegisteredForSurvey(String respondentPhone,
                                         int surveyId) {

        final Respondent resp = findBySurveyAndPhone(respondentPhone, surveyId);
        return resp != null;
    }

    /**
     * Gets respondents (1) registered for a survey {@code surveyId},
     * and (2) not answered to question {@code questionId}.
     */
    // XXX: not called just yet
    public List<Respondent> selectUnansweredRespondentsForSurveyQuestion(
            int surveyId, int questionId) throws IOException, SQLException {

        final Session session = getSessionFactory().openSession();
        try {

            final Question question =
                    (Question) session.load(Question.class, questionId);

            final String surveyTable = LimeSurveyUtils.surveyTable(surveyId);
            final String questionColumn =
                    LimeSurveyUtils.answerField(surveyId, question.getId(), 0);

            final String sqlTemplate = "SELECT id FROM %s where %s is not null";
            final String sqlQuery = String.format(sqlTemplate, surveyTable, questionColumn);

            // TODO: get rid of raw SQL
            // Get IDs of answer rows containing an answer for the specified question.
            @SuppressWarnings("unchecked")
            final List<Integer> answersForQuestion = (List<Integer>) session
                    .createSQLQuery(sqlQuery)
                    .list();

            //noinspection unchecked
            return (List<Respondent>) session
                    .createCriteria(entityClass)
                    .add(eq("sid", surveyId))
                    .add(not(in("aid", answersForQuestion)))
                    .list();

        } finally {
            session.close();
        }
    }

    /**
     * @return Empty list if nothing found
     */
    public List<Respondent> listBySurvey(int surveyId) {

        final Session session = getSessionFactory().openSession();
        try {
            //noinspection unchecked
            return (List<Respondent>) session
                    .createCriteria(Respondent.class)
                    .add(eq("sid", surveyId))
                    .list();

        } finally {
            session.close();
        }
    }

    public int countBySurvey(Survey survey) {

        final Session session = getSessionFactory().openSession();
        try {
            //noinspection unchecked
            final Number count = (Number) session
                    .createCriteria(Respondent.class)
                    .add(eq("sid", survey.getId()))
                    .setProjection(Projections.rowCount())
                    .uniqueResult();

            return count.intValue();

        } finally {
            session.close();
        }
    }

    // XXX: not called just yet
    public Respondent findBySurveyAndPhone(String msisdn,
                                           int sid) {

        final Session session = getSessionFactory().openSession();
        try {
            return (Respondent) session
                    .createCriteria(Respondent.class)
                    .add(eq("msisdn", msisdn))
                    .add(eq("sid", sid))
                    .uniqueResult();

        } finally {
            session.close();
        }
    }

    // TODO: all of this stuff should be performed in a single transaction
    // TODO: this logic should probably be extracted to some upper layer
    // XXX: not called just yet
//    public void registerRespondent(String msisdn, int sid)
//            throws NamingException, SQLException, IOException {
//
//        final Connection connection = Services.instance().getDb().getConnection();
//        try {
//            final Respondent found = findBySurveyAndPhone(msisdn, sid);
//            if (found == null) {
//                final DBService.InsertedId insertResult =
//                        AnswerService.insertEmptyAnswerTable(connection, sid);
//
//                final Respondent respondent = new Respondent();
//                respondent.setMsisdn(msisdn);
//                respondent.setSid(sid);
//                respondent.setAid(insertResult.id);
//                save(respondent);
//
//                Services.instance().getSurveyStatsRepository().incRegistered(
//                        getSessionFactory().getCurrentSession(), sid);
//            }
//        } finally {
//            connection.close();
//        }
//    }

    public Respondent find(Survey survey,
                           String msisdn) {
        final Session session = getSessionFactory().openSession();
        try {
            return find(session, survey, msisdn);
        } finally {
            session.close();
        }
    }

    private Respondent find(Session session,
                            Survey survey,
                            String msisdn) {

        return (Respondent) session.createQuery(
                "from Respondent where msisdn = :msisdn and survey = :survey")
                .setString("msisdn", msisdn)
                .setEntity("survey", survey)
                .uniqueResult();
    }

    public Respondent findOrCreate(String msisdn, Survey survey) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Respondent respondent = find(session, survey, msisdn);
            if (respondent == null) {
                respondent = new Respondent();
                respondent.setMsisdn(msisdn);
                respondent.setSurvey(survey);
                session.save(respondent);

                respondent = find(session, survey, msisdn);
            }

            transaction.commit();

            return respondent;

        } catch (HibernateException e) {
            if ((transaction != null) && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (HibernateException ee) {
                    logger.error(e.getMessage(), e);
                }
            }
            throw e;

        } finally {
            session.close();
        }
    }
}
