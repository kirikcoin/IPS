package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.*;

public class AnswerRepository extends BaseRepository<Answer, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(AnswerRepository.class);

    public AnswerRepository(DB db) {
        super(db);
    }

    public void clear(Survey survey, Respondent respondent) {
        if (survey.getQuestions().isEmpty()) {
            return;
        }

        final Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            clearSentCounts(session, respondent, survey.getQuestions());

            session.createQuery(
                    "delete Answer where question in :questions and respondent = :respondent")
                    .setParameterList("questions", survey.getQuestions())
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

    private void clearSentCounts(Session session, Respondent respondent, List<Question> questions) {
        @SuppressWarnings("unchecked")
        final List<Question> answeredQuestions = (List<Question>) session.createQuery(
                "select answer.question from Answer answer" +
                " where answer.respondent = :respondent and answer.question in :questions")
                .setParameterList("questions", questions)
                .setEntity("respondent", respondent)
                .list();

        for (Question question : answeredQuestions) {
            int decSentCount = question.getSentCount() - 1;
            if (decSentCount < 0) {
                logger.error("Attempt to decrement question sent count resulted to negative value. " +
                        "Question = [" + question + "], respondent = [" + respondent + "]");
                decSentCount = 0;
            }
            question.setSentCount(decSentCount);

            session.update(question);
        }
    }

    public void save(Respondent respondent, QuestionOption option) {

        final OptionAnswer answer = new OptionAnswer();
        answer.setRespondent(respondent);
        answer.setQuestion(option.getQuestion());
        answer.setOption(option);

        save(answer);
    }

    public void save(Respondent respondent, String answerText, Question question) {
        final TextAnswer answer = new TextAnswer();
        answer.setRespondent(respondent);
        answer.setQuestion(question);
        answer.setText(answerText);

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

    private List<Answer> list(Respondent respondent) {
        final Session session = getSessionFactory().getCurrentSession();
        //noinspection unchecked
        return (List<Answer>) session.createQuery(
                "from Answer where respondent = :respondent")
                .setEntity("respondent", respondent)
                .list();
    }

    public List<SurveySession> list(Survey survey,
                                    Date from,
                                    Date to,
                                    String filter,
                                    String orderProperty,
                                    boolean asc,
                                    int limit,
                                    int offset) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = getCriteria(session, survey, from, to, null, filter);
        criteria.createAlias("survey", "survey", JoinType.LEFT_OUTER_JOIN);

        criteria.setFirstResult(offset).setMaxResults(limit);

        {
            final String property;
            if (orderProperty != null) {
                switch (orderProperty) {
                    case "respondent":      property = "msisdn";         break;
                    case "date":            property = "startDate";      break;
                    case "questions":       property = "answersCount";   break;
                    default:
                        throw new RuntimeException("Unexpected sort column: " + orderProperty);
                }

            } else {
                property = "startDate";
            }
            criteria.addOrder(asc ? Order.asc(property) : Order.desc(property));
        }

        @SuppressWarnings("unchecked")
        final List<Respondent> respondents = (List<Respondent>) criteria.list();

        final List<SurveySession> results = new ArrayList<>(respondents.size());
        for (Respondent respondent : respondents) {
            results.add(
                    new SurveySession(survey, respondent, list(respondent))
            );
        }

        return results;
    }



    public List<SurveySession> list(Survey survey,
                                    Date from,
                                    Date to,
                                    String filter,
                                    Boolean hasCoupon,
                                    int limit,
                                    int offset) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = getCriteria(session, survey, from, to, hasCoupon, filter);
        criteria.createAlias("survey", "survey", JoinType.LEFT_OUTER_JOIN);

        criteria.setFirstResult(offset).setMaxResults(limit);

        @SuppressWarnings("unchecked")
        final List<Respondent> respondents = (List<Respondent>) criteria.list();

        final List<SurveySession> results = new ArrayList<>(respondents.size());
        for (Respondent respondent : respondents) {
            results.add(
                    new SurveySession(survey, respondent, list(respondent))
            );
        }

        return results;
    }

    public int count(Survey survey,
                     Date from,
                     Date to,
                     String filter,
                     Boolean hasCoupon) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = getCriteria(session, survey, from, to, hasCoupon, filter);

        criteria.setProjection(Projections.rowCount());

        //noinspection unchecked
        return ((Number) criteria.uniqueResult()).intValue();
    }

    private Criteria getCriteria(Session session,
                                 Survey survey,
                                 Date from,
                                 Date to,
                                 Boolean hasCoupon,
                                 String filter) {
        final Criteria criteria = session.createCriteria(Respondent.class);

        criteria.add(Restrictions.eq("survey", survey));

        criteria.add(Restrictions.ge("startDate", from));
        criteria.add(Restrictions.le("startDate", to));
        if (hasCoupon != null) {
            criteria.add(hasCoupon ? isNotNull("coupon") : isNull("coupon"));
        }

        if (isNotBlank(filter)) {
            filter = filter.trim();

            criteria.add(ilike("msisdn", filter, MatchMode.ANYWHERE));
        }
        return criteria;
    }

    public int count(QuestionOption option) {
        final Session session = getSessionFactory().openSession();
        try {
            final Number count = (Number) session.createQuery(
                    "select count(answer)" +
                    " from OptionAnswer answer" +
                    " where answer.option = :option")
                    .setEntity("option", option)
                    .uniqueResult();
            return count.intValue();

        } finally {
            session.close();
        }
    }

    public int count(Question question) {
        final Session session = getSessionFactory().openSession();
        try {
            final Number count = (Number) session.createQuery(
                    "select count(answer)" +
                    " from Answer answer" +
                    " where answer.question = :question")
                    .setEntity("question", question)
                    .uniqueResult();
            return count.intValue();

        } finally {
            session.close();
        }
    }

    public int countTextAnswers(Question question) {
        final Session session = getSessionFactory().openSession();
        try {
            final Number count = (Number) session
                    .createCriteria(TextAnswer.class)
                    .add(eq("question",question))
                    .setProjection(Projections.rowCount()).uniqueResult();

            return count.intValue();

        } finally {
            session.close();
        }
    }
}
