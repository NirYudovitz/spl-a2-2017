package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;

/**
 * A class represents a tool of type RandomSumPliers
 */
public class RandomSumPliers implements Tool {
    private String type;

    /**
     * RandomSumPliers C-tor
     */
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
    public long useOn(Product p){
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getFinalId()));

        }
        return value;
    }
    /**
     * Sums up id % 10,000 random numbers
     *
     * @param id - The Product's id
     * @return the value of the usage with the tool
     */
    private long func(long id){
        Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }
}
