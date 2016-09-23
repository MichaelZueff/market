package market.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by PerevalovaMA on 22.09.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryOptionsResponse {
    private String type;
    private String serviceName;
    private int price;
    private Dates dates;
    private List<Outlet> outlets;

    public DeliveryOptionsResponse(String type, String serviceName, int price, String date, List<Integer> outletIds) {
        this.type = type;
        this.serviceName = serviceName;
        this.price = price;
        dates = new Dates(date);
        if (outletIds != null) {
            outlets = outletIds.stream().map(Outlet::new).collect(Collectors.toList());
        }
    }

    public int getPrice() {
        return price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getType() {
        return type;
    }

    public Dates getDates() {
        return dates;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }
}
