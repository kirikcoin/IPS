package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Answer;
import mobi.eyeline.ips.model.Question;
import mobi.eyeline.ips.model.QuestionOption;
import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.model.SurveySession;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.ilike;

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

        final Criteria criteria = session.createCriteria(Respondent.class);

        criteria.createAlias("survey", "survey");

        criteria.add(Restrictions.eq("survey", survey));

        criteria.add(Restrictions.ge("startDate", from));
        criteria.add(Restrictions.le("startDate", to));

        if (isNotBlank(filter)) {
            filter = filter.trim();

            criteria.add(ilike("msisdn", filter, MatchMode.ANYWHERE));
        }

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
                property = "date";
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

    public int count(Survey survey,
                     Date from,
                     Date to,
                     String filter) {

        final Session session = getSessionFactory().getCurrentSession();

        final Criteria criteria = session.createCriteria(Respondent.class);

        criteria.createAlias("survey", "survey");

        criteria.add(Restrictions.eq("survey", survey));

        criteria.add(Restrictions.ge("startDate", from));
        criteria.add(Restrictions.le("startDate", to));

        if (isNotBlank(filter)) {
            filter = filter.trim();

            criteria.add(ilike("msisdn", filter, MatchMode.ANYWHERE));
        }

        criteria.setProjection(Projections.rowCount());

        //noinspection unchecked
        return ((Number) criteria.uniqueResult()).intValue();
    }

    public int count(QuestionOption option) {
        final Session session = getSessionFactory().openSession();
        try {
            final Number count = (Number) session.createQuery(
                    "select count(answer)" +
                    " from Answer answer" +
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
}
