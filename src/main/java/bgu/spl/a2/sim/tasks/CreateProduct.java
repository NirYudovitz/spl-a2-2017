package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufacturingPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that represents a task for manufacturing a product
 */
public class CreateProduct extends Task<Product> {
    private Warehouse warehouse;
    private final ManufacturingPlan plan;
    private Product product;

    /**
     * CreateProduct C-tor
     *
     * @param product   - Is the product to be manufactured
     * @param warehouse - Is the warehouse to be used
     */
    public CreateProduct(Product product, Warehouse warehouse) {
        this.product = product;
        this.warehouse = warehouse;
        plan = warehouse.getPlan(product.getName());
        addSubPartsToProduct();
    }

    /**
     * Going over the sub parts needed for the product as seen in the product's plan
     * creating a Product instance for that sub part and adding it to the product's partList
     */
    private void addSubPartsToProduct() {
        String[] parts = plan.getParts();
        long subPartId = product.getStartId() + 1;
        for (String subPartName : parts) {
            Product subPart = new Product(subPartId, subPartName);
            product.addPart(subPart);
        }
    }


    /**
     * starting the task be calling a method for creating the product's sub Part
     */
    @Override
    protected void start() {
        createSubParts();
    }

    /**
     * if no toolas are needed - compute the useOn and complete the product
     * otherwise, iterate on the product's list of tools and for every tool
     * create a Deferrd object that would be resolved when a tool is available
     * When the Deferred object is resolved, calculate the useOn of the tool on the product
     * and create a task to release the tool.
     * when all the tools finished working, complete the product
     */
    private void getTools() {
        AtomicInteger numOfToolsNeeded = new AtomicInteger(plan.getTools().length);
        AtomicLong sumOfUsage = new AtomicLong(0);

        if (numOfToolsNeeded.compareAndSet(0, 0)) {
            product.setFinalId(product.getStartId());
            complete(product);
        } else {

            for (String tool : plan.getTools()) {
                Deferred<Tool> deferredTool = warehouse.acquireTool(tool);
                Runnable useTool = () -> {
                    sumOfUsage.addAndGet(deferredTool.get().useOn(product));
                    spawn(new ReleaseTool(warehouse, deferredTool));
                    AtomicInteger currentNumOfTools = new AtomicInteger(numOfToolsNeeded.decrementAndGet());

                    if (currentNumOfTools.get() == 0) {
                        product.setFinalId(product.getStartId() + sumOfUsage.get());
                        complete(product);
                    }
                };

                deferredTool.whenResolved(useTool);
            }
        }
    }

    /**
     * if the product doesn't need any parts - go to getTools
     * otherwise, iterate on the product's list of parts and for every part
     * create a CreateProduct Task and spawn those tasks.
     * when the tasks ore done go to getTools.
     */
    private void createSubParts() {
        if (plan.getParts().length == 0) {
            getTools();
        } else {
            List<Task<Product>> createSubPartsTasks = new ArrayList<>();

            for (Product subPart : product.getParts()) {
                Task<Product> createSubPart = new CreateProduct(subPart, warehouse);
                createSubPartsTasks.add(createSubPart);
            }

            spawn(createSubPartsTasks.toArray(new Task<?>[createSubPartsTasks.size()]));
            whenResolved(createSubPartsTasks, () -> {
                getTools();
            });
        }

    }
}






