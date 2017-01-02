package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

/**
 * A class that represents a task for releasing a tool
 */
public class ReleaseTool extends Task<Deferred<Tool>> {
    private Warehouse warehouse;
    private Deferred<Tool> deferredTool;

    /**
     * ReleaseTool C-tor
     *
     * @param warehouse    - Is the warehouse to be used
     * @param deferredTool - the Deferred object that holds the tool needed to be released
     */
    public ReleaseTool(Warehouse warehouse, Deferred<Tool> deferredTool) {
        this.warehouse = warehouse;
        this.deferredTool = deferredTool;
    }

    /**
     * starting the task that will release a tool and be completed
     */
    @Override
    protected void start() {
        warehouse.releaseTool(deferredTool.get());
        complete(null);

    }
}
