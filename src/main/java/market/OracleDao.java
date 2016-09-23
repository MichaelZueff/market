package market;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    List<Integer> getOutlets(List<String> stores) throws SQLException {
        if (stores == null) {
            return null;
        } else {
            List<Integer> outlets = new ArrayList<>();

            if (!stores.isEmpty()) {
                String storeIds = stores.stream().collect(Collectors.joining(", "));
                String sql = "SELECT ym_shop_id FROM stores " +
                        "      WHERE ym_shop_id IS NOT NULL" +
                        "        AND id IN (" + storeIds + ")";
                execute(sql, (res -> outlets.add(res.getInt("ym_shop_id"))));
            }

            return outlets;
        }
    }

    Integer getRegionId(int marketId) throws SQLException {
        return selectValue("SELECT id FROM regions WHERE market_id = ?", (rs) -> rs.getInt("id"), marketId);
    }

}
