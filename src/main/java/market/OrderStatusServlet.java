package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 31.08.2016.
 */
@WebServlet({"/order/status"})
public class OrderStatusServlet extends AuthServlet {

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = DbDao.changeOrderStatus(getRequestBody(req));

        if (response.getCode() != 200) {
            LOG.error(req.getRequestURI() + " failed to change order status");
        }

        writeResponse(resp, response);
    }
}