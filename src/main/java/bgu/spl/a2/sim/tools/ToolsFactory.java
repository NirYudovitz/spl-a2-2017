package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;

/**
 * Represent ToolsFactory class
 * Tool factory is a class the manage to get type of tool as String to create and create this tool.
 */
public class ToolsFactory {

    /**
     * getting a type of tool as String to create and return this tool.
     *
     * @param type is a String represent tool.
     * @return new Tool according to input.
     */
    public static Tool createTool(String type) {
        Tool newTool;
        switch (type) {
            case "gs-driver":
                newTool = new GcdScrewDriver();
                break;
            case "np-hammer":
                newTool = new NextPrimeHammer();
                break;
            case "rs-pliers":
                newTool = new RandomSumPliers();
                break;
            default:
                newTool = null;
        }
        return newTool;
    }
}
