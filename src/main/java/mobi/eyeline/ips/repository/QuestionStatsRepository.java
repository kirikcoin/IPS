package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionStats;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class QuestionStatsRepository extends BaseRepository<QuestionStats, Integer> {

    public QuestionStatsRepository(DB db) {
        super(db);
    }

    public void resetQuestionSentCount(Integer questionId) {
        final Session session = getSessionFactory().openSession();
        try {
            final Question question =
                    (Question) session.load(Question.class, questionId);

            final QuestionStats stats =
                    (QuestionStats) session.load(QuestionStats.class, question);
            stats.setSentCount(0);

        } finally {
            session.close();
        }
    }

    public void incrementQuestionStatistic(Integer questionId,
                                           int addSentCount,
                                           int addAnswerCount,
                                           int addResentCount) {

        // TODO: ugly transaction boilerplate. Is there a better way?
        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            QuestionStats questionStats =
                    (QuestionStats) session.get(QuestionStats.class, questionId);
            if (questionStats == null) {
                questionStats = new QuestionStats();
                questionStats.setId(questionId);
            }

            questionStats.setSentCount(questionStats.getSentCount() + addSentCount);
            questionStats.setAnswerCount(questionStats.getAnswerCount() + addAnswerCount);
            questionStats.setResentCount(questionStats.getResentCount() + addResentCount);

            session.saveOrUpdate(questionStats);
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public void delete(Session session, Survey survey) {
        final List<Integer> ids = new ArrayList<>();
        for (Question question : survey.getQuestions()) {
            ids.add(question.getId());
        }

        session.createQuery(
                "delete QuestionStats where id in :ids")
                .setParameterList("ids", ids)
                .executeUpdate();
    }

}
