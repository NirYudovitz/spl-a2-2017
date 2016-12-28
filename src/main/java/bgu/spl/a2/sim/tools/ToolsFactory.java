package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;

/**
 * Created by Nirdun on 28.12.2016.
 */
public class ToolsFactory {
    public static Tool createTool(String type){
        Tool newTool;
        switch (type) {
            case "GcdScrewDriver":
                newTool = new GcdScrewDriver();
            case "NextPrimeHammer":
                newTool = new NextPrimeHammer();
            case "RandomSumPliers":
                newTool = new RandomSumPliers();
            default:
                newTool= null;
        }
        return newTool;
    }
}
