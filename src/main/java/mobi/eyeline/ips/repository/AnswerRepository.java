package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.criterion.Restrictions.*;

public class AnswerRepository extends BaseRepository<Answer, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(AnswerRepository.class);

  private final AccessNumberRepository accessNumberRepository;

  public AnswerRepository(DB db, AccessNumberRepository accessNumberRepository) {
    super(db);
    this.accessNumberRepository = accessNumberRepository;
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
                                  Integer accessNumberId,
                                  String orderProperty,
                                  Boolean asc,
                                  int limit,
                                  int offset) {

    final Session session = getSessionFactory().openSession();

    try {
      survey = (Survey) session.load(Survey.class, survey.getId());

      final Criteria criteria = getCriteria(session, survey, from, to, null, filter, accessNumberId);
      criteria.createAlias("survey", "survey", JoinType.LEFT_OUTER_JOIN);

      criteria.setFirstResult(offset).setMaxResults(limit);

      {
        final String property;
        if (orderProperty != null) {
          switch (orderProperty) {
            case "respondent":
              property = "msisdn";
              break;
            case "date":
              property = "startDate";
              break;
            case "questions":
              property = "answersCount";
              break;
            case "source":
              property = "source";
              break;
            default:
              throw new RuntimeException("Unexpected sort column: " + orderProperty);
          }

        } else {
          property = "startDate";
        }
        criteria.addOrder(toBoolean(asc) ? Order.asc(property) : Order.desc(property));
    }

      //noinspection unchecked
      return asSessions(survey, (List<Respondent>) criteria.list());

    } finally {
      session.close();
    }
  }

  private List<SurveySession> asSessions(Survey survey, List<Respondent> respondents) {
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
                                  Integer accessNumberId,
                                  Boolean hasCoupon,
                                  Order sortOrder,
                                  int limit,
                                  int offset) {

    final Session session = getSessionFactory().getCurrentSession();

    final Criteria criteria =
        getCriteria(session, survey, from, to, hasCoupon, filter, accessNumberId);
    if (sortOrder != null) {
      criteria.addOrder(sortOrder);
    }
    criteria.createAlias("survey", "survey", JoinType.LEFT_OUTER_JOIN);

    criteria.setFirstResult(offset).setMaxResults(limit);

    //noinspection unchecked
    return asSessions(survey, (List<Respondent>) criteria.list());
  }

  public int count(Survey survey,
                   Date from,
                   Date to,
                   String filter,
                   Integer accessNumberId,
                   Boolean hasCoupon) {

    final Session session = getSessionFactory().openSession();
    try {
      survey = (Survey) session.load(Survey.class, survey.getId());

      final Criteria criteria =
              getCriteria(session, survey, from, to, hasCoupon, filter, accessNumberId);

      criteria.setProjection(Projections.rowCount());

      return fetchInt(criteria);

    } finally {
      session.close();
    }
  }

  private Criteria getCriteria(Session session,
                               Survey survey,
                               Date from,
                               Date to,
                               Boolean hasCoupon,
                               String filter,
                               Integer accessNumberId) {
    final Criteria criteria = session.createCriteria(Respondent.class);

    criteria.add(eq("survey", survey));

    criteria.add(ge("startDate", from));
    criteria.add(le("startDate", to));
    if (hasCoupon != null) {
      criteria.add(hasCoupon ? isNotNull("coupon") : isNull("coupon"));
    }

    if (isNotBlank(filter)) {
      filter = filter.trim();

      criteria.add(ilike("msisdn", filter, MatchMode.ANYWHERE));
    }

    if (accessNumberId != null) {
      final String number = accessNumberRepository.load(session, accessNumberId).getNumber();
      criteria.add(eq("source", number));
    }

    return criteria;
  }

  /**
   * Counts the number of times the given {@linkplain QuestionOption option} was chosen.
   *
   * @param from    Results time frame: start. Ignored if {@code null}.
   * @param to      Results time frame: end. Ignored if {@code null}.
   * @param source  Respondent source, e.g. C2S number.
   */
  public int count(QuestionOption option,
                   Date from,
                   Date to,
                   String source) {
    final Session session = getSessionFactory().openSession();
    try {
      final Criteria criteria = session
          .createCriteria(OptionAnswer.class)
          .setProjection(Projections.rowCount())
          .add(eq("option", option));

      if (from != null)     criteria.add(ge("date", from));
      if (to != null)       criteria.add(le("date", to));

      if (source != null) {
        criteria.createAlias("respondent", "respondent");
        criteria.add(eq("respondent.source", source));
      }

      return ((Number) criteria.uniqueResult()).intValue();

    } finally {
      session.close();
    }
  }

  /**
   * Counts the number of answers (of any possible kind) for the given
   * {@linkplain Question question}.
   *
   * @param from    Results time frame: start. Ignored if {@code null}.
   * @param to      Results time frame: end. Ignored if {@code null}.
   * @param source  Respondent source, e.g. C2S number.
   */
  public int count(Question question, Date from, Date to, String source) {
    final Session session = getSessionFactory().openSession();
    try {
      final Criteria criteria = createQuery(session, Answer.class, question, from, to, source);
      return fetchInt(criteria);

    } finally {
      session.close();
    }
  }

  private Criteria createQuery(Session session,
                               Class<? extends Answer> clazz,
                               Question question,
                               Date from,
                               Date to,
                               String source) {

    final Criteria criteria = session
        .createCriteria(clazz)
        .setProjection(Projections.rowCount())
        .add(eq("question", question));

    if (from != null)     criteria.add(ge("date", from));
    if (to != null)       criteria.add(le("date", to));

    if (source != null) {
      criteria.createAlias("respondent", "respondent");
      criteria.add(eq("respondent.source", source));
    }
    return criteria;
  }

  /**
   * Counts the number of times a text answer was given to the specified question.
   *
   * @param from    Results time frame: start. Ignored if {@code null}.
   * @param to      Results time frame: end. Ignored if {@code null}.
   * @param source  Respondent source, e.g. C2S number.
   */
  public int countTextAnswers(Question question, Date from, Date to, String source) {
    final Session session = getSessionFactory().openSession();
    try {
      final Criteria criteria = createQuery(session, TextAnswer.class, question, from, to, source);
      return fetchInt(criteria);

    } finally {
      session.close();
    }
  }
}
