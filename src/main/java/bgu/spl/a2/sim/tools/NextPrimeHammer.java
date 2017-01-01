package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by Nir1612 on 26/12/2016.
 */
public class NextPrimeHammer implements Tool {
    private String type;

    public NextPrimeHammer() {
        type = "np-hammer";
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

    private long func(long id) {

        long v =id + 1;
        while (!isPrime(v)) {
            v++;
        }

        return v;
    }
    private boolean isPrime(long value) {
        if(value < 2) return false;
        if(value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }


//    public long useOn(Product p) {
//        long sumOfUsage = 0;
//        for(Product subPart : p.getParts()) {
//            BigInteger partID = BigInteger.valueOf(subPart.getFinalId());
//            sumOfUsage += Math.abs(getNextPrime(partID.longValue()));
//        }
//        return sumOfUsage;
//    }
//    private Long getNextPrime(Long id){
//        long v =id + 1;
//        while (!isPrime(v)) {
//            v++;
//        }
//
//        return v;
//        }
//
//    private boolean isPrime(long value) {
//        if(value < 2) return false;
//        if(value == 2) return true;
//        long sq = (long) Math.sqrt(value);
//        for (long i = 2; i <= sq; i++) {
//            if (value % i == 0) {
//                return false;
//            }
//        }
//
//        return true;
//    }
}
