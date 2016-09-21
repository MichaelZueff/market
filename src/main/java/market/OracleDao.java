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
public class OracleDao {
    private DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(OracleDao.class);

    public OracleDao() {
        try {
            InitialContext ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
            if (pool == null) {
                throw new ServletException("Unknown DataSource 'jdbc/oracle'");
            }
        } catch (NamingException | ServletException ex) {
            LOG.error("Failed to create oracle connection pool", ex);
        }
    }

    String getResp(String sql, String req) throws SQLException {
        try (Connection dbConnection = pool.getConnection();
             CallableStatement cs = dbConnection.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.CLOB);
            cs.setString(2, req);
            cs.execute();
            return cs.getString(1);
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
