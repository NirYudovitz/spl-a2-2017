
package bgu.spl.a2;

import java.util.List;

import bgu.spl.a2.sim.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public List<JsonTools> getTools() {
        return tools;
    }

    public void setTools(List<JsonTools> tools) {
        this.tools = tools;
    }

    public List<JsonPlans> getPlans() {
        return plans;
    }

    public void setPlans(List<JsonPlans> plans) {
        this.plans = plans;
    }

    public List<List<JsonWaves>> getWaves() {
        return waves;
    }

    public void setWaves(List<List<JsonWaves>> waves) {
        this.waves = waves;
    }

}
