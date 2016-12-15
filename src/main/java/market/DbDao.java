package market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class DbDao {
    private static final Logger LOG = LoggerFactory.getLogger(DbDao.class);
    private static DataSource pool;
    private static Boolean isNotificationsActive = true;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    static {
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

    private static Response getResponse(String procedureName, String requestBody) {
        try (Connection dbConnection = pool.getConnection()) {
            LOG.debug("Connection opened");
            CallableStatement cs = dbConnection.prepareCall("{? = call PARTNER_MARKET." + procedureName + "(?, ?)}");
            LOG.debug("Statement prepared");

            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, requestBody);
            cs.registerOutParameter(3, Types.CLOB);
            cs.execute();
            LOG.debug("SQL executed");

            return new Response(cs.getInt(1), cs.getString(3));
        } catch (SQLException e) {
            LOG.error("SQL error ", e);
            return new Response(500, null);
        }
    }

    private static List<String> getReceiversList() {
        List<String> receivers = new ArrayList<>();
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareCall("SELECT phone FROM market_notifications_list")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                receivers.add(rs.getString("phone"));
            }
        } catch (SQLException e) {
            LOG.error("Error while selecting notifications receivers ", e);
        }

        return receivers;
    }

    private static void sendMessage(List<String> phones, String message) {
        try {
            if (!phones.isEmpty()) {
                String phone = String.join(",", phones);
                URL obj = new URL("http://www.smstraffic.ru/multi.php?login=texnosila2&password=wuvuwaru&phones=" + phone + "&message=" + URLEncoder.encode(message, "Windows-1251") + "&rus=1&originator=Tehnosila&want_sms_ids=1");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.getResponseCode();
            } else {
                LOG.error("Empty receivers list");
            }
        } catch (MalformedURLException e) {
            LOG.error("Wrong url for sms sending", e);
        } catch (Exception e) {
            LOG.error("Error while sms sending", e);
        }
    }

    static void setNotificationsActive(boolean status) {
        isNotificationsActive = status;
    }

    static Response getOrderInfo(String requestBody) {
        Future<Response> task = executorService.submit(() -> getResponse("GET_ORDER_INFO", requestBody));
        try {
            return task.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.error("SQL timeout ", e);
            executorService.submit(() -> sendMessage(getReceiversList(), "Яндекс CPA: таймаут 2 сек"));
            return getResponse("GET_ORDER_INFO", requestBody);
        }
    }

    static Response createOrder(String requestBody) {
        return getResponse("CREATE_ORDER", requestBody);
    }

    static Response changeOrderStatus(String requestBody) {
        return getResponse("CHANGE_ORDER_STATUS", requestBody);
    }

    static Set<String> getAuthKeys() throws SQLException {
        Set<String> keys = new HashSet<>();
        try (Connection dbConnection = pool.getConnection();
             PreparedStatement ps = dbConnection.prepareStatement("SELECT token FROM market_auth")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    keys.add(rs.getString("token"));
                }
            }
        }

        return keys;
    }
}
