package market;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import market.model.*;

/**
 * Created by PerevalovaMA on 26.08.2016.
 */
@WebServlet("/cart")
public class CartServlet extends AuthServlet {
    private final static String REGION_SEARCHED_LEVEL = "SUBJECT_FEDERATION";
    private OracleDao oracleDao = new OracleDao();
    private MysqlDao mysqlDao = new MysqlDao();

/*
    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String respData = db.getResp("{? = call PARTNER_MARKET.GET_ORDER_INFO(?)}", getRequestBody(req));
            writeResponseBody(resp, respData);
        } catch (SQLException e) {
            LOG.error("Failed to get order info");
            resp.sendError(500);
        }
    }
*/

    void initDataPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //    String request = getRequestBody(req);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        Cart cart = mapper.readValue(req.getInputStream(), Cart.class);
        Delivery delivery = cart.getDelivery();

        try {
            int regionId = getRegionId(delivery.getRegion());
            System.out.println(regionId);
        } catch (DataException e) {
            //TODO: response with delivery = false?
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getRegionId(Region region) throws SQLException {
        if (region == null) {
            throw new DataException("Region with searched level " + REGION_SEARCHED_LEVEL + " was not found");
        }

        if (region.getType().equals(REGION_SEARCHED_LEVEL)) {
            Integer regionId = oracleDao.selectValue("SELECT id FROM regions WHERE market_id = ?", (rs) -> rs.getInt("id"), region.getId());
            if (regionId == null) {
                throw new DataException("Region " + region.getName() + " was not found");
            } else {
                return regionId;
            }
        } else {
            return getRegionId(region.getParent());
        }
    }
}
