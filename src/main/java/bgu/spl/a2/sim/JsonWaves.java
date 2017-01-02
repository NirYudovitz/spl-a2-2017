
package bgu.spl.a2.sim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represent JsonWaves class.
 * setting and getting wave prosucts properties from json file.
 */
public class JsonWaves {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("startId")
    @Expose
    private long startId;

    /**
     * @return String represnt prosuct name.
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product is string represent product to be set.
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return number of products .
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * @param qty is number of products to be set.
     */
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    /**
     * getting the stard is of a product.
     *
     * @return the stard id of product.
     */
    public long getStartId() {
        return startId;
    }

    /**
     * @param startId is the id to set to start id of product.
     */
    public void setStartId(long startId) {
        this.startId = startId;
    }

}
