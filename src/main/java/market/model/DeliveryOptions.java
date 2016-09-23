package market.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.List;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
public class DeliveryOptions {
    @JsonProperty("has")
    private boolean isAvl;
    private int cost;
    private String date;
    private HashSet<String> payments;
    private List<String> stores;
    private List<Integer> outlets;

    public DeliveryOptionsResponse getDeliveryOptionsResponse(String deliveryType, String deliveryName) {
        if (isAvl() && date != null && !date.isEmpty()) {
            return new DeliveryOptionsResponse(deliveryType, deliveryName, cost, date, outlets);
        } else {
            return null;
        }
    }

    public boolean isAvl() {
        return isAvl;
    }

    public int getCost() {
        return cost;
    }

    public String getDate() {
        return date;
    }

    public HashSet<String> getPayments() {
        return payments;
    }

    public List<String>  getStores() {
        return stores;
    }

    public void setOutlets(List<Integer> outlets) {
        this.outlets = outlets;
    }
}
