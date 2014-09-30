package mobi.eyeline.ips.web.servlets;

import mobi.eyeline.ips.repository.UserRepository;
import mobi.eyeline.ips.service.Services;
import mobi.eyeline.ips.web.controllers.LogoBean;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ImageViewServlet extends HttpServlet {

    private final UserRepository userRepository = Services.instance().getUserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String[] pathParts = request.getPathInfo().split("/");

        if ("logo".equals(pathParts[1])) {
            try {
                final int userId = Integer.parseInt(pathParts[2]);
                returnImage(response, userRepository.load(userId).getUiProfile().getIcon());

            } catch (Exception e) {
                response.sendError(HttpStatus.SC_BAD_REQUEST);
            }

        } else if ("preview".equals(pathParts[1])) {
            final LogoBean logoBean = (LogoBean) request.getSession().getAttribute("logoBean");

            returnImage(response, logoBean.getBytes());

        } else {
            response.sendError(HttpStatus.SC_NOT_FOUND);
        }
    }

    private void returnImage(HttpServletResponse response,
                             byte[] image) throws IOException {
        response.setHeader("Content-Type", "application/octet-stream");
//        response.setHeader("Content-Disposition", "inline; filename=\"" + "logo" + "\"");
        response.getOutputStream().write(image);
    }
}
