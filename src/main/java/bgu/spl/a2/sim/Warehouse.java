package bgu.spl.a2.sim;

import bgu.spl.a2.sim.conf.ManufacturingPlan;
import bgu.spl.a2.sim.tools.ToolsFactory;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.Deferred;

import java.util.HashMap;
import java.util.Map;


import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the warehouse
 */
public class Warehouse {
    private Map<String, ManufacturingPlan> productPlansMap;
    public Map<String, AtomicInteger> amoutOfTool;
    public Map<String, ConcurrentLinkedQueue<Deferred<Tool>>> defferedWaitingResolve;

    /**
     * Warehouse C - tor
     */
    public Warehouse() {
        productPlansMap = new HashMap<>();
        amoutOfTool = new HashMap<String, AtomicInteger>() {{
            put("gs-driver", new AtomicInteger(0));
            put("np-hammer", new AtomicInteger(0));
            put("rs-pliers", new AtomicInteger(0));
        }};
        defferedWaitingResolve = new HashMap<String, ConcurrentLinkedQueue<Deferred<Tool>>>() {{
            put("gs-driver", new ConcurrentLinkedQueue<>());
            put("np-hammer", new ConcurrentLinkedQueue<>());
            put("rs-pliers", new ConcurrentLinkedQueue<>());
        }};
    }

    /**
     * Tool acquisition procedure
     *
     * @param type - string describing the required tool
     * @return a deferred promise for the  requested tool
     */
    public synchronized Deferred<Tool> acquireTool(String type) {
        Deferred<Tool> deferredTool = new Deferred<>();
        if (amoutOfTool.get(type).get() > 0) {
            amoutOfTool.get(type).decrementAndGet();
            deferredTool.resolve(ToolsFactory.createTool(type));
        } else {
            defferedWaitingResolve.get(type).add(deferredTool);
        }
        return deferredTool;
    }

    /**
     * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
     *
     * @param tool - The tool to be returned
     */
    public synchronized void releaseTool(Tool tool) {
        if (!(defferedWaitingResolve.get(tool.getType()).isEmpty())) {
            Deferred<Tool> deferredTool = defferedWaitingResolve.get(tool.getType()).poll();
            deferredTool.resolve(tool);
        } else {
            amoutOfTool.get(tool.getType()).getAndIncrement();
        }
    }

    /**
     * Getter for ManufactoringPlans
     *
     * @param product - a string with the product name for which a ManufacturingPlan is desired
     * @return A ManufacturingPlan for product
     */
    public ManufacturingPlan getPlan(String product) {
        return productPlansMap.get(product);
    }

    /**
     * Store a ManufacturingPlan in the warehouse for later retrieval
     *
     * @param plan - a ManufacturingPlan to be stored
     */
    public void addPlan(ManufacturingPlan plan) {
        productPlansMap.put(plan.getProductName(), plan);
    }

    /**
     * Store a qty Amount of tools of type tool in the warehouse for later retrieval
     *
     * @param tool - type of tool to be stored
     * @param qty  - amount of tools of type tool to be stored
     */
    public void addTool(Tool tool, int qty) {
        amoutOfTool.get(tool.getType()).getAndAdd(qty);

    }
}


