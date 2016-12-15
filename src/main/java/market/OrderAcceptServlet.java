package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by PerevalovaMA on 29.08.2016.
 */
@WebServlet({"/order/accept"})
public class OrderAcceptServlet extends AuthServlet {

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = DbDao.createOrder(getRequestBody(req));

        if (response.getCode() != 200) {
            LOG.error(req.getRequestURI() + " failed to create the order");
        }

        writeResponse(resp, response);
    }
}


