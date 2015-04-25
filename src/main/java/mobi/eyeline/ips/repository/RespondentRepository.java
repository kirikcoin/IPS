package mobi.eyeline.ips.repository;

import mobi.eyeline.ips.model.Respondent;
import mobi.eyeline.ips.model.Survey;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hibernate.criterion.Restrictions.eq;

public class RespondentRepository extends BaseRepository<Respondent, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(RespondentRepository.class);

  public RespondentRepository(DB db) {
    super(db);
  }

  public int countBySurvey(Survey survey) {

    final Session session = getSessionFactory().openSession();
    try {
      //noinspection unchecked
      final Number count = (Number) session
          .createCriteria(Respondent.class)
          .add(eq("survey", survey))
          .setProjection(Projections.rowCount())
          .uniqueResult();

      return count.intValue();

    } finally {
      session.close();
    }
  }

  public int countFinishedBySurvey(Survey survey) {

    final Session session = getSessionFactory().openSession();
    try {
      //noinspection unchecked
      final Number count = (Number) session
          .createCriteria(Respondent.class)
          .add(eq("survey", survey))
          .add(eq("finished", true))
          .setProjection(Projections.rowCount())
          .uniqueResult();

      return count.intValue();

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
