package market.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private int feedId;
    private String offerId;
    private int orderCount;
    private List<Remains> remains;
    private int price;
    private boolean isDelivery;

    public int getCount() {
        int warehouseCount = 0;
        int storesCount = 0;
        for (Remains r: remains) {
            if (r.getStoreId() == 189) {
                warehouseCount += r.getType() == 1 ? r.getCount() - 1 : r.getCount();
            } else {
                storesCount += r.getType() == 1 ? r.getCount() - 1 : r.getCount();
            }
        }

        if (warehouseCount > 0) {
            return warehouseCount;
        } else {
            return storesCount;
        }
    }

    public ItemResponse getItemResponse() {
        return new ItemResponse(feedId, offerId, price, getCount(), isDelivery);
    }

    public int getFeedId() {
        return feedId;
    }

    public String getOfferId() {
        return offerId;
    }

    @JsonProperty("count")
    public int getOrderCount() {
        return orderCount;
    }

    public List<Remains> getRemains() {
        return remains;
    }

    public void setRemains(List<Remains> remains) {
        this.remains = remains;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }
}
