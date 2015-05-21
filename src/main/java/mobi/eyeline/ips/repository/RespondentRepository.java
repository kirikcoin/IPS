package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.ge;
import static org.hibernate.criterion.Restrictions.lt;

public class RespondentRepository extends BaseRepository<Respondent, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(RespondentRepository.class);

  public RespondentRepository(DB db) {
    super(db);
  }

  /**
   * Counts the number of respondents who got to the survey finish. Here by finish we mean the point
   * where end message (if any) would be sent.
   * As a result, redirects using external links aren't accounted even though they effectively
   * end the survey.
   *
   * @param bySource  If set, filter results by respondent's {@linkplain Respondent#source source}.
   * @param source    Source value, {@code null} value is acceptable and not ignored.
   */
  public int countBySurvey(Survey survey,
                           Date from,
                           Date to,
                           boolean bySource,
                           String source) {

    final Session session = getSessionFactory().openSession();
    try {
      //noinspection unchecked
      final Criteria criteria = session
          .createCriteria(Respondent.class)
          .setProjection(Projections.rowCount())
          .add(eq("survey", survey));

      if (from != null) criteria.add(ge("startDate", from));
      if (to != null)   criteria.add(lt("startDate", to));

      if (bySource) {
        criteria.add(eq("source", source));
      }

      final Number count = (Number) criteria.uniqueResult();
      return count.intValue();

    } finally {
      session.close();
    }
  }

  /**
   * Counts the number of respondents who got to the survey finish. Here by finish we mean the point
   * where end message (if any) would be sent.
   * As a result, redirects using external links aren't accounted even though they effectively
   * end the survey.
   *
   * @param bySource  If set, filter results by respondent's {@linkplain Respondent#source source}.
   * @param source    Source value, {@code null} value is acceptable and not ignored.
   */
  public int countFinishedBySurvey(Survey survey, boolean bySource, String source) {

    final Session session = getSessionFactory().openSession();
    try {
      //noinspection unchecked
      final Criteria criteria = session.createCriteria(Respondent.class)
          .setProjection(Projections.rowCount())
          .add(eq("survey", survey))
          .add(eq("finished", true));

      if (bySource) {
        criteria.add(eq("source", source));
      }

      final Number count = (Number) criteria.uniqueResult();
      return count.intValue();

    } finally {
      session.close();
    }
  }

  private Respondent find(Session session, Survey survey, String msisdn, String source) {

    return (Respondent) session.createQuery(
        "from Respondent" +
        " where" +
        " msisdn = :msisdn and" +
        " survey = :survey and" +
        " ((source = :source) or (source is null and :source is null))")
        .setString("msisdn", msisdn)
        .setEntity("survey", survey)
        .setString("source", source)
        .uniqueResult();
  }

  public Respondent findOrCreate(String msisdn, Survey survey, String source) {
    final Session session = getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();

      Respondent respondent = find(session, survey, msisdn, source);
      if (respondent == null) {
        respondent = new Respondent();
        respondent.setMsisdn(msisdn);
        respondent.setSurvey(survey);
        respondent.setSource(source);
        session.save(respondent);

        respondent = find(session, survey, msisdn, source);
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
