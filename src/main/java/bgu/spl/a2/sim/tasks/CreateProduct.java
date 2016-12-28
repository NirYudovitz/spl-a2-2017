package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Nirdun on 28.12.2016.
 */
public class CreateProduct extends Task<Product> {
    //private final Product product;
    private String productName;
    Warehouse warehouse;
    private final ManufactoringPlan plan;
    private long startId;
    private Product product;
    private boolean allToolsAvailable;
    private boolean allPartsAvailable;
    private List<Tool> tools;

    public CreateProduct(String productName, long id, Warehouse wh) {
        this.product = new Product(id, productName);
        this.warehouse = wh;
        plan = warehouse.getPlan(productName);
        addPartsToProduct();
        allToolsAvailable = plan.getTools().length == 0;
        allPartsAvailable = plan.getParts().length == 0;

    }

    private void addPartsToProduct() {
        String[] parts = plan.getParts();

        for (String part : parts) {
            product.addPart();

        }

    }

    private Product newProduct(){
        long newId=product.getStartId();
        for(Product p: product.getParts()){
            newId+=p.getFinalId();
        }
        for(Tool t: tools){
            newId+=t.useOn(product);
            warehouse.releaseTool(t);
        }
        complete(product);

    }
    @Override
    protected void start() {
        if(!allPartsAvailable){
            //get all parts
        }else if(!allToolsAvailable){

        }
        else {


        }







        String[] parts = plan.getParts();
        String[] tools = plan.getTools();
        Boolean haveTools = true;
        int numOfTools = tools.length;
        if (parts.length == 0) {
            for (String tool : tools) {
                Deferred<Tool> defferedTool = warehouse.acquireTool(tool);
                haveTools &= defferedTool.isResolved();
            }

            if (haveTools) {
                for ()
                    Product finalProduct = complete(finalProduct);
            }

        } else {
            List<Task<Product>> partsTasks = new ArrayList<>();
            for (String part : parts) {
                Task<Product> subProduct = new CreateProduct(part, warehouse, );
                partsTasks.add(subProduct);

            }
            this.spawn(partsTasks.toArray(new Task<?>[partsTasks.size()]));
            //todo check if ? in task<> is ok

            whenResolved(partsTasks, () -> {

            });
        }


    }
}
