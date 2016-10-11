package market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class DbDao {
    private DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(DbDao.class);

    public DbDao() {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/market");
            if (pool == null) {
                throw new ServletException("Unknown DataSource 'jdbc/market'");
            }
        } catch (NamingException | ServletException ex) {
            LOG.error("Failed to create connection pool", ex);
        }
    }

    private Response getResponse(String procedureName, String requestBody) {
        try (Connection dbConnection = pool.getConnection()) {
            CallableStatement cs = dbConnection.prepareCall("{? = call PARTNER_MARKET." + procedureName + "(?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, requestBody);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return new Response(cs.getInt(1), cs.getString(3));
        } catch (Exception e) {
            LOG.error("sql error ", e);
            return new Response(500, null);
        }
    }

    Response createOrder(String requestBody) {
        return getResponse("CREATE_ORDER", requestBody);
    }

    Response changeOrderStatus(String requestBody) {
        return getResponse("CHANGE_ORDER_STATUS", requestBody);
    }

    String getResp(String sql, String req) throws SQLException {
        try (Connection dbConnection = pool.getConnection()) {
            CallableStatement cs = dbConnection.prepareCall(sql);
            cs.registerOutParameter(1, Types.CLOB);
            cs.setString(2, req);
            cs.execute();
            return cs.getString(1);
        } catch (Exception e) {
            LOG.error("connection error ", e);
            return null;
        }
    }

    boolean execute(String sql, String req) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             CallableStatement cs = dbConnection.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, req);
            cs.execute();
            return (cs.getInt(1) == 1);
        }
    }
}
