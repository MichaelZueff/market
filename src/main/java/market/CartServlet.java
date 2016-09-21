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
        try {
            String reqData = getRequestBody(req);
            LOG.debug("/cart read request body");
            String respData = db.getResp("{? = call PARTNER_MARKET.GET_ORDER_INFO(?)}", reqData);
            writeResponseBody(resp, respData);
        } catch (SQLException e) {
            LOG.error("Failed to get order info");
            resp.sendError(500);
        }
    }
}
