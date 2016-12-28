package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.ToolsFactory;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.HashMap;
import java.util.Map;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the warehouse in your simulation
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!
 */
public class Warehouse {
    private AtomicInteger amountOfScrewDrivers;
    private AtomicInteger amountOfHammers;
    private AtomicInteger amountOfPliers;
    private Map<String, ManufactoringPlan> productPlansMap;
    private Map<String, AtomicInteger> amoutOfTool;
    private ConcurrentLinkedQueue<Deferred<Tool>> defferesScerawDriversWaitingResolve;
    private ConcurrentLinkedQueue<Deferred<Tool>> defferesHammersWaitingResolve;
    private ConcurrentLinkedQueue<Deferred<Tool>> defferesPliersWaitingResolve;

    /**
     * Constructor
     */
    public Warehouse() {
        // TODO: 26/12/2016 maybe use concurrent deque or something...
        amountOfScrewDrivers = new AtomicInteger(0);
        amountOfHammers = new AtomicInteger(0);
        amountOfPliers = new AtomicInteger(0);
        productPlansMap = new HashMap<>();
        amoutOfTool = new HashMap<>();
        amoutOfTool = new HashMap<String, AtomicInteger>() {{
            put("GcdScrewDriver", new AtomicInteger(0));
            put("NextPrimeHammer", new AtomicInteger(0));
            put("RandomSumPliers", new AtomicInteger(0));
        }};
        defferesHammersWaitingResolve = new ConcurrentLinkedQueue<>();
        defferesHammersWaitingResolve = new ConcurrentLinkedQueue<>();
        defferesHammersWaitingResolve = new ConcurrentLinkedQueue<>();
    }


    /**
     * Tool acquisition procedure
     * Note that this procedure is non-blocking and should return immediatly
     *
     * @param type - string describing the required tool
     * @return a deferred promise for the  requested tool
     */
    public Deferred<Tool> acquireTool(String type) {
        // TODO: 26/12/2016 think on some smarter way to do this
        // TODO: 26/12/2016 handle catch
        // TODO: 26/12/2016 try without sync

        Deferred<Tool> deferredTool = new Deferred<>();
        if (amoutOfTool.get(type).get() > 0) {
            amoutOfTool.get(type).decrementAndGet();
            deferredTool.resolve(ToolsFactory.createTool(type));
        } else {
            defferesScerawDriversWaitingResolve.add(deferredTool);
        }

        return deferredTool;
    }

//
//        switch (type) {
//            case "GcdScrewDriver":
//                synchronized (this) {
//                    if (amountOfScrewDrivers.get() > 0) {
//                        amountOfScrewDrivers.decrementAndGet();
//                        deferredTool.resolve(new GcdScrewDriver());
//                    } else {
//                        defferesScerawDriversWaitingResolve.add(deferredTool);
//                    }
//
//                    break;
//                }
//            case "NextPrimeHammer":
//                synchronized (this) {
//                    if (amountOfHammers.get() > 0) {
//                        amountOfHammers.decrementAndGet();
//                        deferredTool.resolve(new NextPrimeHammer());
//                    } else {
//                        defferesHammersWaitingResolve.add(deferredTool);
//                    }
//                    break;
//                }
//            case "RandomSumPliers":
//                synchronized (this) {
//                    if (amountOfPliers.get() > 0) {
//                        amountOfPliers.decrementAndGet();
//                        deferredTool.resolve(new RandomSumPliers());
//                    } else {
//                        defferesPliersWaitingResolve.add(deferredTool);
//                    }
//                    break;
//                }
//        }
//    }

    /**
     * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
     *
     * @param tool - The tool to be returned
     */
    public void releaseTool(Tool tool) {
        // TODO: 26/12/2016 maybe sync?
        amoutOfTool.get(tool.getType()).incrementAndGet();
    }

    /**
     * Getter for ManufactoringPlans
     *
     * @param product - a string with the product name for which a ManufactoringPlan is desired
     * @return A ManufactoringPlan for product
     */
    public ManufactoringPlan getPlan(String product) {
        return productPlansMap.get(product);
    }

    /**
     * Store a ManufactoringPlan in the warehouse for later retrieval
     *
     * @param plan - a ManufactoringPlan to be stored
     */
    public void addPlan(ManufactoringPlan plan) {
        productPlansMap.put(plan.getProductName(), plan);
    }


    /**
     * Store a qty Amount of tools of type tool in the warehouse for later retrieval
     *
     * @param tool - type of tool to be stored
     * @param qty  - amount of tools of type tool to be stored
     */
    public void addTool(Tool tool, int qty) {
        // TODO: 26/12/2016 think on some smarter way to do this
        amoutOfTool.get(tool.getType()).getAndAdd(qty);

    }

}


