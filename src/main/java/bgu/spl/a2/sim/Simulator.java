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

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * A class describing the simulator for part 2 of the assignment.
 */
public class Simulator {
    private static WorkStealingThreadPool threadPool;
    private static Warehouse wareHouse;
    private static List<List<JsonWaves>> waves;

    /**
     * initialize wareHouse and waves.
     */
    static {
        wareHouse = new Warehouse();
        waves = new LinkedList<>();
    }


    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    /**
     * Begin the simulation
     *
     * @return ConcurrentLinkedQueue of Products
     */
    public static ConcurrentLinkedQueue<Product> start() {
        ConcurrentLinkedQueue<Product> manufacturedProducts = new ConcurrentLinkedQueue<>();
        threadPool.start();

        // going over the waves.
        for (List<JsonWaves> jwave : waves) {

            int totalProducts = 0;

            //Summing number of products to be created.
            for (int i = 0; i < jwave.size(); i++) {

                totalProducts += jwave.get(i).getQty();
            }

            // array of products in current wave - array is sorting bi indexing order in product.
            Product[] waveProducts = new Product[totalProducts];
            // represent the number of products to be printed.
            int numOfProductsToPrint = totalProducts - 1;
            //represent the current order print of product.
            int orderPrint = totalProducts;
            // represents the number of tools that need to be finish before going to next wave.
            CountDownLatch countDownLatch = new CountDownLatch(totalProducts);
            //going over products and create them.
            for (JsonWaves wave : jwave) {
                //going over number of product according to quantity.
                for (int i = 0; i < wave.getQty(); i++) {
                    //creating new product and stting order to print.
                    Product product = new Product(wave.getStartId() + i, wave.getProduct(), --orderPrint);
                    Task<Product> task = new CreateProduct(product, wareHouse);
                    String name = product.getName();
                    threadPool.submit(task);

                    // when product is ready to be finish/
                    task.getResult().whenResolved(() -> {
                        waveProducts[numOfProductsToPrint - product.getOrderToPrint()] = product;
                        countDownLatch.countDown();
                    });
                }
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            manufacturedProducts.addAll(Arrays.asList(waveProducts));
        }
        try {
            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //return ConcurrentLinkedQueue< of all products thet created.
        return manufacturedProducts;
    }


    /**
     * @param jTools is a list of tools from json files to added to warehouse.
     */
    public static void addToolsToWarehouse(List<JsonTools> jTools) {
        for (JsonTools jTool : jTools) {
            wareHouse.addTool(ToolsFactory.createTool(jTool.getTool()), jTool.getQty());

        }
    }

    /**
     * @param jPlans is a list of plans from json files to added to warehouse.
     */
    public static void addPlansToWarehouse(List<JsonPlans> jPlans) {
        for (JsonPlans jPlan : jPlans) {
            String newProduct = jPlan.getProduct();
            String newParts[] = jPlan.getParts().toArray(new String[jPlan.getParts().size()]);
            String newTools[] = jPlan.getTools().toArray(new String[jPlan.getTools().size()]);
            wareHouse.addPlan(new ManufacturingPlan(newProduct, newParts, newTools));
        }
    }

    /**
     * @param waves is the waves to get from json file and settin in simulator.
     */
    public static void getWavesFromJson(List<List<JsonWaves>> waves) {
        Simulator.waves = waves;
    }


    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator.
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        threadPool = myWorkStealingThreadPool;
    }


    /**
     * runnig a parse on json file and create products, finally write products to ser file.
     *
     * @param args is where the json fils is located.
     */
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


        //creating ser file and write products object into file.
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("result.ser");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(manufacturedProducts);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
