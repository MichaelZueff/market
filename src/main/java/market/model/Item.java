package market.model;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class Item {
    private int feedId;
    private String offerId;
    private String feedCategoryId;
    private String offerName;
    private int count;

    public int getFeedId() {
        return feedId;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getFeedCategoryId() {
        return feedCategoryId;
    }

    public String getOfferName() {
        return offerName;
    }

    public int getCount() {
        return count;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setFeedCategoryId(String feedCategoryId) {
        this.feedCategoryId = feedCategoryId;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
