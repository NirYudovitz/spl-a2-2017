package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Processor;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.tools.Tool;

/**
 * Created by Nir1612 on 28/12/2016.
 */
public class UseTool extends Task<Deferred<Tool>>{
    private Product product;
    private Deferred<Tool>  deferredTool;

    public UseTool(Product product, Deferred<Tool> deferredTool) {
        this.product = product;
        this.deferredTool = deferredTool;
    }

    /**
     * start handling the task - note that this method is protected, a handler
     * cannot call it directly but instead must use the
     * {@link #handle(Processor)} method
     */
    @Override
    protected void start() {

    }
}
