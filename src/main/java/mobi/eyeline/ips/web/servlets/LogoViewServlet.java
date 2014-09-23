package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.model.User;
import mobi.eyeline.ips.web.controllers.SkinController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoViewServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SkinController skinController= (SkinController) request.getSession().getAttribute("skinController");
//        User user = (User) request.getSession().getAttribute("currentUser");

        response.setHeader("Content-Type", getServletContext().getMimeType("logo"));
        response.setHeader("Content-Disposition", "inline; filename=\"" + "logo" + "\"");


        response.getOutputStream().write(skinController.getLogo());
    }
}
