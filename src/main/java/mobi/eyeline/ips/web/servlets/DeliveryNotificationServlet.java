package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.messages.MissingParameterException;
import mobi.eyeline.ips.service.deliveries.NotificationService;
import mobi.eyeline.ips.util.RequestParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.*;

public class DeliveryNotificationServlet extends HttpServlet {
  private static final Logger logger = LoggerFactory.getLogger(DeliveryNotificationServlet.class);

  @Inject private NotificationService notificationService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      doGet0(req, resp);
    } catch (Exception e) {
      logger.error("Notification processing error", e);
      resp.setStatus(SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void doGet0(HttpServletRequest req, HttpServletResponse resp)
      throws InterruptedException {
    final NotificationService.Notification notification = parse(req);
    if (notification == null) {
      resp.setStatus(SC_BAD_REQUEST);

    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("Notification: [" + notification + "]");
      }

      final boolean ok = notificationService.handleNotification(notification);
      resp.setStatus(ok ? SC_OK : SC_BAD_REQUEST);
    }
  }

  private NotificationService.Notification parse(HttpServletRequest req) {
    @SuppressWarnings("unchecked")

    final Map<String, String[]> params = req.getParameterMap();

    if (logger.isTraceEnabled()) {
      logger.trace("Notification parameters: " + RequestParseUtils.toString(params));
    }
    boolean isDelivered;
    try {
      isDelivered = RequestParseUtils.getBoolean(params, "is-delivered");
    } catch (MissingParameterException e) {
      isDelivered = false;
      if (logger.isDebugEnabled()) {
        logger.debug("Notification: is-delivered parameter");
      }
    }

    try {
      return new NotificationService.Notification(
          RequestParseUtils.getInt(params, "resource_id"),
          RequestParseUtils.getInt(params, "status"), isDelivered);

    } catch (MissingParameterException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Notification: format invalid: [" +
            RequestParseUtils.toString(params) + "]");
      }
      return null;
    }
  }
}
