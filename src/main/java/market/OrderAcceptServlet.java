package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by PerevalovaMA on 29.08.2016.
 */
@WebServlet({"/order/accept"})
public class OrderAcceptServlet extends AuthServlet{

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String respData = db.getResp("{? = call PARTNER_MARKET.CREATE_ORDER(?)}", getRequestBody(req));

            if (respData != null) {
                writeResponseBody(resp, respData);
            } else {
                LOG.error(req.getRequestURI() + " empty response from oracle");
                resp.sendError(500);
            }
        } catch (SQLException e) {
            LOG.error(req.getRequestURI() + " Failed to create order");
            resp.sendError(500);
        }
    }
}


