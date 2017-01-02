package bgu.spl.a2.sim.conf;

/**
 * a class that represents a manufacturing plan.
 **/
public class ManufacturingPlan {
    final private String product;
    private String parts[];
    private String tools[];

    /**
     * ManufacturingPlan constructor
     *
     * @param product - product name
     * @param parts   - array of strings describing the plans part names
     * @param tools   - array of strings describing the plans tools names
     */
    public ManufacturingPlan(String product, String[] parts, String[] tools) {
        this.product = product;
        this.parts = parts;
        this.tools = tools;
    }

    /**
     * @return array of strings describing the plan's part names
     */
    public String[] getParts() {
        return parts;
    }

    /**
     * @return string containing product name.
     */
    public String getProductName() {
        return product;
    }

    /**
     * @return array of strings describing the plans tools names
     */
    public String[] getTools() {
        return tools;
    }

}
