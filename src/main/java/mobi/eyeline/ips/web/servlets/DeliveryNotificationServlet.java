package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.service.deliveries.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeliveryNotificationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        NotificationService notificationService = Services.instance().getNotificationService();
        try{
            int id = Integer.parseInt(req.getParameter("resource_id"));
            int status = Integer.parseInt(req.getParameter("status"));
            resp.setStatus(notificationService.handleNotification(id, status));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
