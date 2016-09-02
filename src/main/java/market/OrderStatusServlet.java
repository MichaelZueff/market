package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 31.08.2016.
 */
@WebServlet({"/order/status"})
public class OrderStatusServlet extends AuthServlet{

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            boolean isSuccess = db.execute("{? = call PARTNER_MARKET.CHANGE_ORDER_STATUS(?)}", getRequestBody(req));

            if (isSuccess) {
                resp.setContentType("application/xml; charset=UTF-8");
            } else {
                LOG.error(req.getRequestURI() + " Failed to update order status");
                resp.sendError(500);
            }
        } catch (SQLException e) {
            LOG.error(req.getRequestURI() + " Failed to update order status");
            resp.sendError(500);
        }
    }
}