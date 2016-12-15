package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 26.08.2016.
 */
@WebServlet("/cart")
public class CartServlet extends AuthServlet {

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = DbDao.getOrderInfo(getRequestBody(req));

        if (response.getCode() != 200)  {
            LOG.error(req.getRequestURI() + " failed to get order info");
        }

        writeResponse(resp, response);
    }
}
