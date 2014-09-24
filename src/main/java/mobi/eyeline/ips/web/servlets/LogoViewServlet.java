package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.web.controllers.LogoBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoViewServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogoBean logoBean= (LogoBean) request.getSession().getAttribute("skinController");

        response.setHeader("Content-Type", getServletContext().getMimeType("logo"));
        response.setHeader("Content-Disposition", "inline; filename=\"" + "logo" + "\"");


        response.getOutputStream().write(logoBean.getLogo());
    }
}
