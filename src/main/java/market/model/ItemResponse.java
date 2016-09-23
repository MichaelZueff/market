package market.model;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
public class ItemResponse {
    private String offerId;
    private int feedId;
    private int count;
    private boolean delivery;
    private int price;

    public ItemResponse(int feedId, String offerId, int price, int count, boolean delivery) {
        this.count = count;
        this.delivery = delivery;
        this.feedId = feedId;
        this.offerId = offerId;
        this.price = price;
    }

    public String getOfferId() {
        return offerId;
    }

    public int getFeedId() {
        return feedId;
    }

    public int getCount() {
        return count;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public int getPrice() {
        return price;
    }
}
