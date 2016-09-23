package market.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
public class CartDelivery {
    private DeliveryOptions pickup;
    private DeliveryOptions courier;
    private Map<String, ItemDelivery> items;

    @JsonProperty("self")
    public DeliveryOptions getPickup() {
        return pickup;
    }

    public void setPickup(DeliveryOptions pickup) {
        this.pickup = pickup;
    }

    public DeliveryOptions getCourier() {
        return courier;
    }

    public void setCourier(DeliveryOptions courier) {
        this.courier = courier;
    }

    public Map<String, ItemDelivery> getItems() {
        return items;
    }

    public void setItems(Map<String, ItemDelivery> items) {
        this.items = items;
    }
}
