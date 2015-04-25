package mobi.eyeline.ips.repository;


import mobi.eyeline.ips.model.DeliverySubscriber;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static mobi.eyeline.ips.model.DeliverySubscriber.State;

public class DeliverySubscriberRepository extends BaseRepository<DeliverySubscriber, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(DeliverySubscriberRepository.class);

  public DeliverySubscriberRepository(DB db) {
    super(db);
  }

  public void updateState(List<Pair<Integer, State>> idsAndStates,
                          State oldState) {

    final Session session = getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();

      for (Pair<Integer, State> idAndState : idsAndStates) {
        int count = session.createQuery(
            "UPDATE DeliverySubscriber" +
                " SET state = :newState" +
                " WHERE id = :id AND state = :oldState")
            .setParameter("newState", idAndState.getValue())
            .setParameter("oldState", oldState)
            .setParameter("id", idAndState.getKey())
            .executeUpdate();

        if (logger.isDebugEnabled()) {
          logger.debug("Subscriber-" + idAndState.getKey() + ": new state = " + idAndState.getValue() + ", if old state = " + oldState + ", count=" + count);
        }
      }

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

  public void updateState(List<Pair<Integer, State>> idsAndStates) {

    final Session session = getSessionFactory().openSession();
    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();

      for (Pair<Integer, State> idAndState : idsAndStates) {
        int count = session.createQuery(
            "UPDATE DeliverySubscriber" +
                " SET state = :newState" +
                " WHERE id = :id")
            .setParameter("newState", idAndState.getValue())
            .setParameter("id", idAndState.getKey())
            .executeUpdate();

        if (logger.isDebugEnabled()) {
          logger.debug("Subscriber-" + idAndState.getKey() + ": new state = " + idAndState.getValue() + ", count=" + count);
        }
      }

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

  // TODO: this is untested due to missing `time_to_sec' and `timediff' routines in HSQL.
  public int expire(State from, State to, long expirationDelaySeconds) {
    final Session session = getSessionFactory().openSession();

    Transaction transaction = null;
    try {
      transaction = session.beginTransaction();

      final int count = session.createSQLQuery(
          "UPDATE delivery_subscribers ds" +
              " SET state = :toState" +
              " WHERE " +
              " ds.state = :fromState AND time_to_sec(timediff(now(), ds.last_update)) > :diff")
          .setParameter("diff", expirationDelaySeconds)
          .setParameter("fromState", from)
          .setParameter("toState", to)
          .executeUpdate();

      transaction.commit();

      return count;

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
