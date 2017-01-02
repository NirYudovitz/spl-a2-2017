
package bgu.spl.a2.sim;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represent JsonRes class.
 * class is getting and setting threads tools plans and waves.
 */
public class JsonRes {

    @SerializedName("threads")
    @Expose
    private Integer threads;
    @SerializedName("tools")
    @Expose
    private List<JsonTools> tools = null;
    @SerializedName("plans")
    @Expose
    private List<JsonPlans> plans = null;
    @SerializedName("waves")
    @Expose
    private List<List<JsonWaves>> waves = null;

    /**
     * @return number of threads.
     */
    public Integer getThreads() {
        return threads;
    }

    /**
     * @param threads - is the number of threads to be set.
     */
    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    /**
     * @return List of JsonTools.
     */
    public List<JsonTools> getTools() {
        return tools;
    }

    /**
     * @param tools is the tools to be set.
     */
    public void setTools(List<JsonTools> tools) {
        this.tools = tools;
    }

    /**
     * @return List<JsonPlans> of plans.
     */
    public List<JsonPlans> getPlans() {
        return plans;
    }

    /**
     * @param plans is a List of planst to be set.
     */
    public void setPlans(List<JsonPlans> plans) {
        this.plans = plans;
    }

    /**
     * @return List of Json waves represnts waves.
     */
    public List<List<JsonWaves>> getWaves() {
        return waves;
    }

    /**
     * @param waves is List of JsonWaves List represent waves to be set.
     */
    public void setWaves(List<List<JsonWaves>> waves) {
        this.waves = waves;
    }

}
