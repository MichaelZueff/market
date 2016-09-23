package market.model;

/**
 * Created by PerevalovaMA on 21.09.2016.
 */
public class Remains {
    private int storeId;
    private int type;
    private int count;

    public Remains() {
    }

    public Remains(int storeId, int type, int count) {
        this.storeId = storeId;
        this.type = type;
        this.count = count;
    }

    int getStoreId() {
        return storeId;
    }

    int getType() {
        return type;
    }

    int getCount() {
        return count;
    }
}
