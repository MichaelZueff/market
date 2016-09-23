package market.model;

/**
 * Created by PerevalovaMA on 19.09.2016.
 */
public class Region {
    private int id;
    private String name;
    private String type;
    private Region parent;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Region getParent() {
        return parent;
    }
}
