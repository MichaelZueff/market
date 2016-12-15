package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 15.12.2016.
 */
@WebServlet("/notifications")
public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isActive = req.getParameter("on");
        if (isActive != null) {
            if (isActive.equals("true")) {
                DbDao.setNotificationsActive(true);
            } else if (isActive.equals("false")) {
                DbDao.setNotificationsActive(false);
            }
        }
    }
}
