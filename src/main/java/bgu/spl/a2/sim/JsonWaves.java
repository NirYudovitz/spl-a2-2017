
package bgu.spl.a2.sim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public long getStartId() {
        return startId;
    }

    public void setStartId(long startId) {
        this.startId = startId;
    }

}
