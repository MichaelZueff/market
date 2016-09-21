package market.model;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class Item {
    private int feedId;
    private int offerId;
    private int feedCategoryId;
    private String offerName;
    private int count;

    public int getFeedId() {
        return feedId;
    }

    public int getOfferId() {
        return offerId;
    }

    public int getFeedCategoryId() {
        return feedCategoryId;
    }

    public String getOfferName() {
        return offerName;
    }

    public int getCount() {
        return count;
    }
}
