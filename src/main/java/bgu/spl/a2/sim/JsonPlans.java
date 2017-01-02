
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
     * @param product is the name of the product(String) to be set.
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return List of strings represents the tools in the plan.
     */
    public List<String> getTools() {
        return tools;
    }

    /**
     * @param tools is a List of string represents name of tools to be set to plan.
     */
    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    /**
     * @return List pf string represents name of parts.
     */
    public List<String> getParts() {
        return parts;
    }

    /**
     * @param parts is a List of string represents name of parts to be set to plan.
     */
    public void setParts(List<String> parts) {
        this.parts = parts;
    }

}
