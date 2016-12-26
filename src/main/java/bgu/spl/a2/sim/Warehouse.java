package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.HashMap;
import java.util.Map;



import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!
 *
 */
public class Warehouse {
	private LinkedBlockingQueue<Tool> screwDrivers;
	private LinkedBlockingQueue<Tool> hammers;
	private LinkedBlockingQueue<Tool> pliers;
	private Map<String,ManufactoringPlan> productPlansMap;

	/**
	* Constructor
	*/
    public Warehouse(){
		// TODO: 26/12/2016 maybe use concurrent deque or something...
		screwDrivers = new LinkedBlockingQueue<>();
		hammers = new LinkedBlockingQueue<>();
		pliers = new LinkedBlockingQueue<>();
		productPlansMap = new HashMap<>();
	}

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
    public Deferred<Tool> acquireTool(String type){
	// TODO: 26/12/2016 think on some smarter way to do this
        // TODO: 26/12/2016 handle catch
        // TODO: 26/12/2016 try without sync
        switch (type) {
			case "GcdScrewDriver":
				synchronized (this) {
					try{
					    Deferred<Tool> deferredScrewDriver = new Deferred<>();
                        deferredScrewDriver.resolve(screwDrivers.take());
					    return deferredScrewDriver;
                    }
                    catch (InterruptedException e){
					    e.printStackTrace();
                    }
					break;
				}
			case "NextPrimeHammer":
				synchronized (this) {
                    try{
                        Deferred<Tool> deferredHammer = new Deferred<>();
                        deferredHammer.resolve(screwDrivers.take());
                        return deferredHammer;
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
					break;
				}
			case "RandomSumPliers":
				synchronized (this){
                    try{
                        Deferred<Tool> deferredPliers = new Deferred<>();
                        deferredPliers.resolve(screwDrivers.take());
                        return deferredPliers;
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
					break;
				}
        }
        return new Deferred<>();
        //todo handle this ^
	}
	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/
    public void releaseTool(Tool tool){
        // TODO: 26/12/2016 maybe sync?
       switch (tool.getType()) {
           case "GcdScrewDriver":
               screwDrivers.add(tool);
               break;
           case "NextPrimeHammer":
               hammers.add(tool);
               break;
           case "RandomSumPliers":
               pliers.add(tool);
               break;
       }
    }
	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
    public ManufactoringPlan getPlan(String product){
    	return productPlansMap.get(product);
	}
	
	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
    public void addPlan(ManufactoringPlan plan){
    	productPlansMap.put(plan.getProductName(),plan);
	}

    
	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
    public void addTool(Tool tool, int qty){
		// TODO: 26/12/2016 think on some smarter way to do this
		switch (tool.getType()){
			case "GcdScrewDriver":
				for (int i=0; i<qty; i++) {
					screwDrivers.add(new GcdScrewDriver());
				}
					//addToolsToVector(screwDriversVector, new GcdScrewDriver(), qty);
				break;
			case "NextPrimeHammer":
				for (int i=0; i<qty; i++) {
					hammers.add(new NextPrimeHammer());
				}// add to vector
				break;
			case "RandomSumPliers":
				for (int i=0; i<qty; i++) {
					pliers.add(new RandomSumPliers());
				}
				// add to vector
				break;
		}
	}
//	private void addToolsToVector(Vector<Tool> vector,Tool tool, int qty){
//    	for (int i=0; i<qty; i++){
//    		vector.add(tool);
//		}
//	}

}
