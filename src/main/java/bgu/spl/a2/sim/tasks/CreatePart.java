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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Nirdun on 28.12.2016.
 */
public class CreatePart extends Task<Product> {
    //private final Product product;
    private String productName;
    private Warehouse warehouse;
    private final ManufactoringPlan plan;
    private long startId;
    private Product product;
    private boolean allToolsAvailable;
    private boolean allPartsAvailable;
    private List<Tool> tools;

    public CreatePart(Product product, Warehouse wh) {
        this.product = product;
        this.warehouse = wh;
        plan = warehouse.getPlan(productName);
        allToolsAvailable = plan.getTools().length == 0;
        allPartsAvailable = plan.getParts().length == 0;
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

    private void newProduct() {
        long newId = product.getStartId();
        for (Product p : product.getParts()) {
            newId += p.getFinalId();
        }
        for (Tool t : tools) {
            newId += t.useOn(product);
            warehouse.releaseTool(t);
        }
        complete(product);
    }

    @Override
    protected void start() {
        if (!allPartsAvailable) {
            createParts();
        } else if (!allToolsAvailable) {

        } else {


        }
    }

    private void createParts() {
        List<Task<Product>> partsTasks = new ArrayList<>();
        for (Product subPart : product.getParts()) {
            Task<Product> createSubPart = new CreatePart(subPart, warehouse);
            partsTasks.add(createSubPart);
        }
        spawn(partsTasks.toArray(new Task<?>[partsTasks.size()]));
        whenResolved(partsTasks, () -> {
            allPartsAvailable = true;
            spawn(this);
        });
    }
}


