package bgu.spl.a2.sim;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * class represent JsonParser.
 */
public class JsonParser {
    private int numOfThreds;
    private List<JsonTools> tools;
    private List<JsonPlans> plans;
    private List<List<JsonWaves>> waves;

    /**
     * JsonParser C-tor.
     */
    public JsonParser() {
        plans = new LinkedList<>();
        tools = new LinkedList<>();
        waves = new LinkedList<>();
    }

    /**
     * pharse json file to num of thareds,tools,plans and waves with Gson.
     *
     * @param args is the json file input.
     */
    public void parse(String[] args) {
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

    /**
     * @return number of threads according to input in json file.
     */
    public int getNumOfThreds() {
        return numOfThreds;
    }

    /**
     * @return list of tools according to input in json file.
     */
    public List<JsonTools> getTools() {
        return tools;
    }

    /**
     * @return lists of plans according to input in json file.
     */
    public List<JsonPlans> getPlans() {
        return plans;
    }

    /**
     * @return list of list og jsonwaves according to input in json file.
     */

    public List<List<JsonWaves>> getWaves() {
        return waves;
    }

}
