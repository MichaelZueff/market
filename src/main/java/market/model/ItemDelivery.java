package market.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDelivery {
    private String offerId;
    private boolean isPickup;
    private boolean isCourier;

    @JsonProperty("item_id")
    public String getOfferId() {
        return offerId;
    }

    @JsonProperty("self")
    public boolean isPickup() {
        return isPickup;
    }

    @JsonProperty("courier")
    public boolean isCourier() {
        return isCourier;
    }
}
