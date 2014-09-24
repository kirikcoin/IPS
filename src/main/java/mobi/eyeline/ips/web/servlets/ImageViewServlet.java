package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ImageViewServlet extends HttpServlet {
    //TODO: dont forget any exceptions here
//    private final UserRepository userRepository = Services.instance().getUserRepository();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Integer userId = Integer.valueOf(request.getPathInfo().substring(1));

        User user = (User) request.getSession().getAttribute("updatedUser");
        response.setHeader("Content-Type", getServletContext().getMimeType("logoImage"));
        response.setHeader("Content-Disposition", "inline; filename=\"" + "logoImage" + "\"");


        response.getOutputStream().write(user.getUiProfile().getIcon());

    }
}
