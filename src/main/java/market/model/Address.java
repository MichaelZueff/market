package market.model;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class Address {
    private String postcode;
    private String country;
    private String city;
    private String subway;
    private String street;
    private String house;
    private String floor;

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getSubway() {
        return subway;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public String getFloor() {
        return floor;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSubway(String subway) {
        this.subway = subway;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
