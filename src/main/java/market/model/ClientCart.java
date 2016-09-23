package market.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
@JsonRootName("cart")
public class ClientCart {
    private String currency;
    private List<Item> items;
    private ClientDeliveryAddress delivery;

    public String getCurrency() {
        return currency;
    }

    public List<Item> getItems() {
        return items;
    }

    public ClientDeliveryAddress getDelivery() {
        return delivery;
    }
}
