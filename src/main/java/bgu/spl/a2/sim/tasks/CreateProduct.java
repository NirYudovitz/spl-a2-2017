package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * Created by Nirdun on 28.12.2016.
 */
public class CreateProduct extends Task<Product> {
    //private final Product product;
    private Warehouse warehouse;
    private final ManufactoringPlan plan;
    private Product product;
    private boolean allToolsAvailable;
    private boolean allPartsAvailable;
    private CountDownLatch countDownLatch;

    public CreateProduct(Product product, Warehouse wh, CountDownLatch countDownLatch) {
        this.product = product;
        this.warehouse = wh;
        plan = warehouse.getPlan(product.getName());
        allToolsAvailable = plan.getTools().length == 0;
        allPartsAvailable = plan.getParts().length == 0;
        this.countDownLatch = countDownLatch;
        if (product.getParts().size() == 0) {
            addPartsToProduct();
        }
    }

    private void addPartsToProduct() {
        String[] parts = plan.getParts();
        long newPartId = product.getStartId() + 1;
        for (String partName : parts) {
            Product subPart = new Product(newPartId, partName);
            product.addPart(subPart);
        }
    }

    @Override
    protected void start() {
        if (!allPartsAvailable) {
            createParts();
        } else if (!allToolsAvailable) {
            getTools();
        } else {
            complete(product);
            //countDownLatch.countDown();
        }
    }

    private void getTools() {
        List<Task<Deferred<Tool>>> toolsTasks = new ArrayList<>();
        for (String tool : plan.getTools()) {
            Deferred<Tool> deferredTool = warehouse.acquireTool(tool);
            Runnable useTool = () -> {
                long usageId = deferredTool.get().useOn(product);
                warehouse.releaseTool(deferredTool.get());
                product.setFinalId(product.getFinalId() + usageId);
            };
            deferredTool.whenResolved(useTool);
            Task<Deferred<Tool>> getTool = new UseTool(product, deferredTool);
            toolsTasks.add(getTool);
        }
        spawn(toolsTasks.toArray(new Task<?>[toolsTasks.size()]));
        whenResolved(toolsTasks, () -> {
            allToolsAvailable = true;
            // TODO: 28/12/2016 check if continuing task
        });
    }

    private void createParts() {
        List<Task<Product>> partsTasks = new ArrayList<>();
        for (Product subPart : product.getParts()) {
            Task<Product> createSubPart = new CreatePart(subPart, warehouse);
            partsTasks.add(createSubPart);
        }
        spawn(partsTasks.toArray(new Task<?>[partsTasks.size()]));
        whenResolved(partsTasks, () -> {
            //allPartsAvailable = true;
            //this.start();
            //getTools();
        });
    }
}

//        String[] parts = plan.getParts();
//        String[] tools = plan.getTools();
//        Boolean haveTools = true;
//        int numOfTools = tools.length;
//        if (parts.length == 0) {
//            for (String tool : tools) {
//                Deferred<Tool> defferedTool = warehouse.acquireTool(tool);
//                haveTools &= defferedTool.isResolved();
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






