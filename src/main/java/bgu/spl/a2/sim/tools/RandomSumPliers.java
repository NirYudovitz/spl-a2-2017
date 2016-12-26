package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;

/**
 * Created by Nir1612 on 26/12/2016.
 */
public class RandomSumPliers implements Tool {
    /**
     * @return tool name as string
     */
    @Override
    public String getType() {
        return "RandomSumPliers";
    }

    /**
     * Tool use method
     *
     * @param p - Product to use tool on
     * @return a long describing the result of tool use on Product package
     */
    @Override
    public long useOn(Product p) {
        long sumOfProducts = (p.getStartId()) % 10000;
        long plierID = 0;
        Random rnd = new Random();
        for (long i = 0; i < sumOfProducts; i++)
            plierID += rnd.nextInt();
        return plierID;
    }
}
