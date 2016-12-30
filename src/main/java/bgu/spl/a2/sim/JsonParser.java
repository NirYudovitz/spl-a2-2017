package bgu.spl.a2.sim;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nir1612 on 28/12/2016.
 */
public class JsonParser {
    private int numOfThreds;
    private List<JsonTools> tools;
    private List<JsonPlans> plans;
    private List<List<JsonWaves>> waves;

    public JsonParser() {
        plans = new LinkedList<>();
        tools = new LinkedList<>();
        waves = new LinkedList<>();
    }
    public void parse(String[] args){
        Gson gson = new Gson();
        String file = args[0];
        try (FileReader fileReader = new FileReader(file);) {
            JsonRes jasonRes;
            jasonRes = gson.fromJson(new FileReader(file), JsonRes.class);
            numOfThreds = jasonRes.getThreads();
            tools = jasonRes.getTools();
            plans = jasonRes.getPlans();
            waves = jasonRes.getWaves();

        } catch (IOException ioExc) {
            System.out.println("cought an IO Exception: " + ioExc);
        }
    }
    public int getNumOfThreds() {
        return numOfThreds;
    }

    public List<JsonTools> getTools() {
        return tools;
    }

    public List<JsonPlans> getPlans() {
        return plans;
    }

    public List<List<JsonWaves>> getWaves() {
        return waves;
    }

}
