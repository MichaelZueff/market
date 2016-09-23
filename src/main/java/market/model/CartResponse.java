package market.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
@JsonRootName("cart")
public class CartResponse {
    private List<ItemResponse> items;
    private List<String> paymentMethods;
    private List<DeliveryOptionsResponse> deliveryOptions;

    public CartResponse(List<ItemResponse> items, List<String> paymentMethods, List<DeliveryOptionsResponse> deliveryOptions) {
        this.items = items;
        this.paymentMethods = paymentMethods;
        this.deliveryOptions = deliveryOptions;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public List<DeliveryOptionsResponse> getDeliveryOptions() {
        return deliveryOptions;
    }
}
