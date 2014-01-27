package mobi.eyeline.ips.web;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * author: Denis Enenko
 * date: 20.01.2014
 *
 * Реализация шаблона session-per-request.
 * До начала обработки запроса создается сессия и открывается транзакция.
 * После окончания обработки запроса транзакция откатывается (если она ранее не закоммитана).
 */
public class SessionPhaseListener implements PhaseListener
{
  @Override
  public void beforePhase(PhaseEvent phaseEvent) {
    try {
//      System.out.println("------ BEFORE PHASE " + phaseEvent.getPhaseId() + ' ' + phaseEvent.getFacesContext().getViewRoot().getViewId());
      if(phaseEvent.getPhaseId() == PhaseId.RESTORE_VIEW) {
        //todo: Берем текущую сессию hibernate, если транзакция не активна, открываем новую транзакцию.
      }
    }
    catch(Exception ex) {
      System.err.println("Error in phase listener. " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  @Override
  public void afterPhase(PhaseEvent phaseEvent) {
    try {
//      System.out.println("------ AFTER PHASE " + phaseEvent.getPhaseId() + ' ' + phaseEvent.getFacesContext().getViewRoot().getViewId());
      if(phaseEvent.getPhaseId() == PhaseId.RENDER_RESPONSE) {
//        System.out.println("REQUEST END\n\n");
        //todo: Берем текущую сессию hibernate, если транзакция активна, откатываем ее.
      }
    }
    catch(Exception ex) {
      System.err.println("Error in phase listener. " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
