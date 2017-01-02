
package bgu.spl.a2.sim;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represent JsonPlans class.
 */
public class JsonPlans {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("tools")
    @Expose
    private List<String> tools = null;
    @SerializedName("parts")
    @Expose
    private List<String> parts = null;

    /**
     * @return product represnt as string
     */
    public String getProduct() {
        return product;
    }

    /**
     * @return List of strings represents the tools in the plan.
     */
    public List<String> getTools() {
        return tools;
    }

    /**
     * @return List pf string represents name of parts.
     */
    public List<String> getParts() {
        return parts;
    }

}
