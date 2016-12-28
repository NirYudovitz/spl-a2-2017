/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.CreateProduct;
import bgu.spl.a2.sim.tools.ToolsFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
    private static WorkStealingThreadPool threadPool;
    private static Warehouse wareHouse;
    private static List<List<JsonWaves>> waves;

    public Simulator() {
        // TODO: 28/12/2016 check if must recieve threadPool
        wareHouse=new Warehouse();
        waves = new LinkedList<>();
    }


    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {
        ConcurrentLinkedQueue<Product> manufactoredProducts = new ConcurrentLinkedQueue<>();
        threadPool.start();
        for (List<JsonWaves> jwave:waves){
            int totalProducts= 0;
            for (int i = 0; i<jwave.size(); i++) {
                totalProducts += jwave.get(i).getQty();
            }
            CountDownLatch countDownLatch = new CountDownLatch(totalProducts);
            for(JsonWaves wave:jwave) {
                Product product;

                for (int i=0; i<wave.getQty(); i++){
                    product = new Product(wave.getStartId() + i, wave.getProduct());
                    Task<Product> task = new CreateProduct(product, wareHouse, countDownLatch);
                    String name = product.getName();
                    threadPool.submit(task);
                    task.getResult().whenResolved(() -> {
                        System.out.println("created a: " + name);
                        countDownLatch.countDown();
                    });
                }
            }
            try {
                countDownLatch.await();
            }
            catch (InterruptedException e){
                // TODO: 28/12/2016 Do something
            }
        }

        return manufactoredProducts;
    }

    public void addToolsToWarehouse(List<JsonTools> jTools){
        for(JsonTools jTool:jTools) {

            wareHouse.addTool(ToolsFactory.createTool(jTool.getTool()),jTool.getQty());

//            switch (jTool.getTool()) {
//                case "gs-driver":
//                    wareHouse.addTool(new GcdScrewDriver(),jTool.getQty());
//                    break;
//                case "np-hammer":
//                    wareHouse.addTool(new NextPrimeHammer(),jTool.getQty());
//                    break;
//                case "rs-pliers":
//                    wareHouse.addTool(new RandomSumPliers(),jTool.getQty());
//                    break;
//            }

        }
    }
    public void addPlansToWarehouse(List<JsonPlans> jPlans) {
        for(JsonPlans jPlan:jPlans){
            String newProduct=jPlan.getProduct();
            String newParts[]=jPlan.getParts().toArray(new String[jPlan.getParts().size()]);
            String newTools[]=jPlan.getTools().toArray(new String[jPlan.getTools().size()]);
            wareHouse.addPlan(new ManufactoringPlan(newProduct,newParts,newTools));
        }
    }
    public void getWavesFromJson(List<List<JsonWaves>> waves){
        this.waves = waves;
    }


    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        threadPool = myWorkStealingThreadPool;
    }

    public static void main(String[] args) {
        Simulator simulator = new Simulator();

        // ***** Parsing the Json file *****
        JsonParser jsonParser = new JsonParser();
        jsonParser.parse(args);

        // ***** Adding parts and plans to the warehouse *****
        simulator.addToolsToWarehouse(jsonParser.getTools());
        simulator.addPlansToWarehouse(jsonParser.getPlans());

        // ***** Adding waves *****
        simulator.getWavesFromJson(jsonParser.getWaves());

        //***** Creating a threadpool with the number of threads from the json file *****
        attachWorkStealingThreadPool(new WorkStealingThreadPool(jsonParser.getNumOfThreds()));
        simulator.start();
//        return 0; //todo main int
    }
}
