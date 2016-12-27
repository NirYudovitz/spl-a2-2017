/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
    static WorkStealingThreadPool threadPool;
    private int numOfThreds;
    private List<JsonTools> jTools;
    private List<JsonPlans> jPlans;
    private List<List<JsonWaves>> jWaves;
    private Warehouse wareHouse;

    public Simulator() {
        jPlans = new LinkedList<>();
        jTools = new LinkedList<>();
        jWaves = new LinkedList<>();
        wareHouse=new Warehouse();

    }


    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {
        ConcurrentLinkedQueue<Product> manufactoredProducts = new ConcurrentLinkedQueue<>();

    }

    public void readFromJson(String[] args) {
        Gson gson = new Gson();
        String file = args[0];
        try (FileReader fileReader = new FileReader(file);) {
            JsonRes jasonRes = gson.fromJson(fileReader, JsonRes.class);
            numOfThreds = jasonRes.getThreads();
            jTools=jasonRes.getTools();
            jPlans=jasonRes.getPlans();
            jWaves=jasonRes.getWaves();

            //Todo function to switch case

            for(JsonTools jtool:jTools) {
                switch (jtool.getTool()) {
                    case "gs-driver":
                        wareHouse.addTool(new GcdScrewDriver(),jtool.getQty());
                        break;
                    case "np-hammer":
                        wareHouse.addTool(new NextPrimeHammer(),jtool.getQty());
                        break;
                    case "rs-pliers":
                        wareHouse.addTool(new RandomSumPliers(),jtool.getQty());
                        break;
                }
            }

            for(JsonPlans jPlan:jPlans){
                String newProduct=jPlan.getProduct();
                String newParts[]=jPlan.getParts().toArray(new String[jPlan.getParts().size()]);
                String newTools[]=jPlan.getTools().toArray(new String[jPlan.getTools().size()]);
                wareHouse.addPlan(new ManufactoringPlan(newProduct,newParts,newTools));
            }


        } catch (IOException ioExc) {

        }

    }

    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        threadPool = myWorkStealingThreadPool;
    }

    public static int main(String[] args) {
    }
}
