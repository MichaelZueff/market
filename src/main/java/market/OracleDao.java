package market;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by PerevalovaMA on 04.08.2016.
 */
public class OracleDao extends DbDao {
    OracleDao() {
        super("jdbc/oracle");
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
