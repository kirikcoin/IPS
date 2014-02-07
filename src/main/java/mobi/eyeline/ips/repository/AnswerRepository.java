package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Answer;
import mobi.eyeline.ips.model.QuestionOption;
import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnswerRepository extends BaseRepository<Answer, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnswerRepository.class);

    public AnswerRepository(DB db) {
        super(db);
    }

    public void clear(Survey survey, Respondent respondent) {
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery(
                    "delete Answer where question.survey = :survey and respondent = :respondent")
                    .setEntity("survey", survey)
                    .setEntity("respondent", respondent)
                    .executeUpdate();

            transaction.commit();

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

    public void save(Respondent respondent, QuestionOption option) {

        final Answer answer = new Answer();
        answer.setRespondent(respondent);
        answer.setQuestion(option.getQuestion());
        answer.setOption(option);

        save(answer);
    }

    public Answer getLast(Survey survey, Respondent respondent) {
        final Session session = getSessionFactory().openSession();
        try {
            return (Answer) session.createQuery(
                    "from Answer" +
                    " where question.survey = :survey and respondent = :respondent" +
                    " order by id desc")
                    .setEntity("survey", survey)
                    .setEntity("respondent", respondent)
                    .setMaxResults(1)
                    .uniqueResult();

        } finally {
            session.close();
        }
    }
}
