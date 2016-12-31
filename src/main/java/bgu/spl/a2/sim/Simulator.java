/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.*;
import bgu.spl.a2.sim.conf.ManufacturingPlan;
import bgu.spl.a2.sim.tasks.CreateProduct;
import bgu.spl.a2.sim.tools.ToolsFactory;

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

    static {
        wareHouse=new Warehouse();
        waves = new LinkedList<>();
    }


    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {
        ConcurrentLinkedQueue<Product> manufacturedProducts = new ConcurrentLinkedQueue<>();
        threadPool.start();
        int j = 0;
        for (List<JsonWaves> jwave:waves){

            int totalProducts= 0;
            System.out.println("Starting a new wave: " + j);
            for (int i = 0; i<jwave.size(); i++) {

                totalProducts += jwave.get(i).getQty();
            }
            CountDownLatch countDownLatch = new CountDownLatch(totalProducts);
            //System.out.println("the length of the wave is: " + jwave.size());
            for(JsonWaves wave:jwave) {
                System.out.println("wave starting to create: " + wave.getQty() + " " + wave.getProduct());
                //Product product;

                for (int i=0; i<wave.getQty(); i++){

                    Product product = new Product(wave.getStartId() + i, wave.getProduct());
                    //System.out.println("starting task" + wave.getProduct());
                    Task<Product> task = new CreateProduct(product, wareHouse);
                    String name = product.getName();
                    threadPool.submit(task);
                    task.getResult().whenResolved(() -> {
                        //System.out.print("created a: " + name + " changing count to: ");
                        //System.out.println(countDownLatch.getCount());
                        countDownLatch.countDown();
                        manufacturedProducts.add(product);
                    });
                }
            }
            try {
                //System.out.println("I'm waiting.......");
                countDownLatch.await();
            }
            catch (InterruptedException e){
                // TODO: 28/12/2016 Do something
                e.printStackTrace();
            }
            j++;
        }
        try {
            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return manufacturedProducts;
    }

    public static void addToolsToWarehouse(List<JsonTools> jTools){
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
    public static void addPlansToWarehouse(List<JsonPlans> jPlans) {
        for(JsonPlans jPlan:jPlans){
            String newProduct=jPlan.getProduct();
            String newParts[]=jPlan.getParts().toArray(new String[jPlan.getParts().size()]);
            String newTools[]=jPlan.getTools().toArray(new String[jPlan.getTools().size()]);
            wareHouse.addPlan(new ManufacturingPlan(newProduct,newParts,newTools));
        }
    }
    public static void getWavesFromJson(List<List<JsonWaves>> waves){
        Simulator.waves = waves;
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
        //Simulator simulator = new Simulator();

        // ***** Parsing the Json file *****
        JsonParser jsonParser = new JsonParser();
        jsonParser.parse(args);

        // ***** Adding parts and plans to the warehouse *****
        Simulator.addToolsToWarehouse(jsonParser.getTools());
        Simulator.addPlansToWarehouse(jsonParser.getPlans());

        // ***** Adding waves *****
        Simulator.getWavesFromJson(jsonParser.getWaves());

        //***** Creating a threadpool with the number of threads from the json file *****
        attachWorkStealingThreadPool(new WorkStealingThreadPool(jsonParser.getNumOfThreds()));
        ConcurrentLinkedQueue<Product> manufacturedProducts = Simulator.start();
        for (Product product : manufacturedProducts){
            System.out.println(product.toString());
        }
//        return 0; //todo main int
    }
}
