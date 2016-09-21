package market.model;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class Delivery {
    private Address address;
    private Region region;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
