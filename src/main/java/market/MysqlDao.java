package market;

import market.model.Remains;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class MysqlDao extends DbDao {
    MysqlDao() {
        super("jdbc/mysql");
    }

    Map<String, Integer> getPrices(String itemIds, Integer regionId) throws SQLException {
        String sql = "SELECT item_id, value" +
                "       FROM price" +
                "      WHERE item_id IN (" + itemIds + ")" +
                "        AND price_type_id = 1" +
                "        AND region_id = ?";

        Map<String, Integer> prices = new HashMap<>();
        execute(sql, (res ->
                prices.put(res.getString("item_id"), res.getInt("value"))), regionId);

        return prices;
    }

    Map<String, List<Remains>> getRemains(String itemIds, Integer regionId) throws SQLException {
        String sql = "SELECT item_id, store_id, type_remain, count" +
                "       FROM item_store" +
                "      WHERE region_id = ? AND item_id IN (" + itemIds + ")";

        Map<String, List<Remains>> remains = new HashMap<>();
        execute(sql, (res -> {
            String itemId = res.getString("item_id");
            remains.putIfAbsent(itemId, new ArrayList<>());
            remains.get(itemId).add(new Remains(res.getInt("store_id"), res.getInt("type_remain"), res.getInt("count")));
        }), regionId);

        return remains;
    }
}
