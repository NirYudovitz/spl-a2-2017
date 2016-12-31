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
    public long useOn(Product p){
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(func(part.getFinalId()));

        }
        return value;
    }

    public long func(long id){
        Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }



//    public long useOn(Product p) {
//        long sumOfUsage = 0;
//        for (Product subPart : p.getParts()) {
//            long sumOfProducts = (subPart.getFinalId()) % 10000;
//            Random rnd = new Random(subPart.getFinalId());
//            for (long i = 0; i < sumOfProducts; i++)
//                sumOfUsage += Math.abs(rnd.nextInt());
//        }
//        return sumOfUsage;
//    }
}
