package mobi.eyeline.ips.web;

import mobi.eyeline.ips.service.Services;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 * <p/>
 * Реализация шаблона session-per-request.
 * До начала обработки запроса создается сессия и открывается транзакция.
 * После окончания обработки запроса транзакция откатывается (если она ранее не закоммитана).
 */
public class SessionPhaseListener implements PhaseListener {

  private static final Logger logger = LoggerFactory.getLogger(SessionPhaseListener.class);

  @Override
  public void beforePhase(PhaseEvent phaseEvent) {
    try {
      final UIViewRoot viewRoot = phaseEvent.getFacesContext().getViewRoot();
      logger.debug("------ BEFORE PHASE " +
          phaseEvent.getPhaseId() +
          ' ' +
          (viewRoot != null ? viewRoot.getViewId() : ""));

      if (phaseEvent.getPhaseId() == PhaseId.RESTORE_VIEW) {
        Session session = Services.instance().getDb().getSessionFactory().getCurrentSession();
        if (!session.getTransaction().isActive()) {
          session.beginTransaction();
        }
      }

    } catch (Exception e) {
      logger.error("Error in phase listener", e);
    }
  }

  @Override
  public void afterPhase(PhaseEvent phaseEvent) {
    try {
      final UIViewRoot viewRoot = phaseEvent.getFacesContext().getViewRoot();
      logger.debug("------ AFTER PHASE " +
          phaseEvent.getPhaseId() +
          ' ' +
          (viewRoot != null ? viewRoot.getViewId() : ""));

      if (phaseEvent.getPhaseId() == PhaseId.RENDER_RESPONSE) {
        logger.debug("REQUEST END");

        final SessionFactory sessionFactory = Services.instance().getDb().getSessionFactory();
        final Transaction transaction = sessionFactory.getCurrentSession().getTransaction();
        try {
          transaction.commit();

        } catch (HibernateException e) {
          logger.error("Transaction commit failed", e);
          if (transaction.isActive()) {
            transaction.rollback();
          }
        }

      }
    } catch (Exception e) {
      logger.error("Error in phase listener", e);
    }
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
