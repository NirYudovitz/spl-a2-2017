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
 * Created by Nirdun on 28.12.2016.
 */
public class CreateProduct extends Task<Product> {
    //private final Product product;
    private Warehouse warehouse;
    private final ManufacturingPlan plan;
    private Product product;
    //private boolean allToolsAvailable;
    //private boolean allPartsAvailable;
    //private CountDownLatch countDownLatch;

    public CreateProduct(Product product, Warehouse warehouse) {
        this.product = product;
        this.warehouse = warehouse;
        plan = warehouse.getPlan(product.getName());
        //allToolsAvailable = plan.getTools().length == 0;
        //allPartsAvailable = plan.getParts().length == 0;
//        if (product.getParts().size() == 0) {
//            addPartsToProduct();
//        }
        addSubPartsToProduct();
    }

    private void addSubPartsToProduct() {
        String[] parts = plan.getParts();
        long subPartId = product.getStartId() + 1;
        for (String subPartName : parts) {
            Product subPart = new Product(subPartId, subPartName);
            product.addPart(subPart);
        }
    }

    @Override
    protected void start() {
        //System.out.println(Thread.currentThread().getId() + " is starting to create: " + product.getName() + " +++++++++++++++++");
        createSubParts();
//        if (!allPartsAvailable) {
//            createSubParts();
//        } else if (!allToolsAvailable) {
//            getTools();
//        } else {
//            complete(product);
//            //countDownLatch.countDown();
//        }
    }

    private void getTools() {
        //List<Task<Deferred<Tool>>> acquireToolsTasks = new ArrayList<>();
        AtomicInteger numOfToolsNeeded = new AtomicInteger(plan.getTools().length);
        AtomicLong sumOfUsage = new AtomicLong(0);

        if (numOfToolsNeeded.compareAndSet(0, 0)) {
            product.setFinalId(product.getStartId());
            complete(product);
        } else {
            for (String tool : plan.getTools()) {
                //System.out.println(Thread.currentThread().getId() + " is trying to get " + tool);
                Deferred<Tool> deferredTool = warehouse.acquireTool(tool);
                Runnable useTool = () -> {
                    sumOfUsage.addAndGet(deferredTool.get().useOn(product));
                    warehouse.releaseTool(deferredTool.get());
                    numOfToolsNeeded.getAndDecrement();

                    if (numOfToolsNeeded.compareAndSet(0, 0)) {
                        //System.out.println(Thread.currentThread().getId() + " Finished to create: " + product.getName() + "+++++++++++++");
                        product.setFinalId(product.getStartId() + sumOfUsage.get());
                        complete(product);
                    }
                };

                deferredTool.whenResolved(useTool);

                //Task<Deferred<Tool>> getTool = new UseTool(product, deferredTool);
                // acquireToolsTasks.add(getTool);
            }
        }

//        if (!acquireToolsTasks.isEmpty()) {
//            spawn(acquireToolsTasks.toArray(new Task<?>[acquireToolsTasks.size()]));
//            whenResolved(acquireToolsTasks, () -> {
//                //allToolsAvailable = true;
//                // TODO: 28/12/2016 check if continuing task
//                System.out.println(Thread.currentThread().getId() + "is compleating: " + product.getName());
//                complete(product);
//            });
//        }
//        else{
//            complete(product);
//        }
    }

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
                //allPartsAvailable = true;
                //this.start();
                getTools();
            });
        }

    }
}

//        String[] parts = plan.getParts();
//        String[] tools = plan.getTools();
//        Boolean haveTools = true;
//        int numOfTools = tools.length;
//        if (parts.length == 0) {
//            for (String tool : tools) {
//                Deferred<Tool> deferredTool = warehouse.acquireTool(tool);
//                haveTools &= deferredTool.isResolved();
//            }
//
//            if (haveTools) {
//                for ()
//                    Product finalProduct = complete(finalProduct);
//            }
//
//        } else {
//            List<Task<Product>> partsTasks = new ArrayList<>();
//            for (String part : parts) {
//                Task<Product> subProduct = new CreateProduct(part, warehouse, );
//                partsTasks.add(subProduct);
//
//            }
//            this.spawn(partsTasks.toArray(new Task<?>[partsTasks.size()]));
//            //todo check if ? in task<> is ok
//
//            whenResolved(partsTasks, () -> {
//
//            });
//        }






