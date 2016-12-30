package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;

/**
 * Created by Nir1612 on 26/12/2016.
 */
public class RandomSumPliers implements Tool {
    private String type;

    public RandomSumPliers() {
        type = "rs-pliers";
    }
    /**
     * @return tool name as string
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Tool use method
     *
     * @param p - Product to use tool on
     * @return a long describing the result of tool use on Product package
     */
    @Override
    public long useOn(Product p) {
        long sumOfUsage = 0;
        for (Product subPart : p.getParts()) {
            long sumOfProducts = (subPart.getFinalId()) % 10000;
            Random rnd = new Random(subPart.getFinalId());
            for (long i = 0; i < sumOfProducts; i++)
                sumOfUsage += rnd.nextInt();
            // TODO: 30/12/2016 math.abs?
        }
        return sumOfUsage;
    }
}
