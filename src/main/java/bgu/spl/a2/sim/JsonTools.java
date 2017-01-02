
package bgu.spl.a2.sim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonTools {

    @SerializedName("tool")
    @Expose
    private String tool;
    @SerializedName("qty")
    @Expose
    private Integer qty;

    /**
     * @return String represent tool.
     */
    public String getTool() {
        return tool;
    }

    /**
     * @return the quantity of products to be produce.
     */
    public Integer getQty() {
        return qty;
    }
}
