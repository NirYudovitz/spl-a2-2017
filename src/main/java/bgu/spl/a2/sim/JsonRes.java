
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
     * @return List of JsonTools.
     */
    public List<JsonTools> getTools() {
        return tools;
    }

    /**
     * @return List<JsonPlans> of plans.
     */
    public List<JsonPlans> getPlans() {
        return plans;
    }

    /**
     * @return List of Json waves represnts waves.
     */
    public List<List<JsonWaves>> getWaves() {
        return waves;
    }
}
