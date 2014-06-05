package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.messages.MissingParameterException;
import mobi.eyeline.ips.model.DeliverySubscriber;
import mobi.eyeline.ips.repository.DeliverySubscriberRepository;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.service.deliveries.NotificationService;
import mobi.eyeline.ips.util.RequestParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.DELIVERED;
import static mobi.eyeline.ips.model.DeliverySubscriber.State.UNDELIVERED;

public class DeliveryNotificationServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryNotificationServlet.class);

    private final NotificationService notificationService =
            Services.instance().getNotificationService();

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
        final Map<String, String[]> params = (Map<String, String[]>) req.getParameterMap();

        try {
            return new NotificationService.Notification(
                    RequestParseUtils.getInt(params, "resource_id"),
                    RequestParseUtils.getInt(params, "status"));

        } catch (MissingParameterException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Notification: format invalid: [" +
                        RequestParseUtils.toString(params) + "]");
            }
            return null;
        }
    }
}
