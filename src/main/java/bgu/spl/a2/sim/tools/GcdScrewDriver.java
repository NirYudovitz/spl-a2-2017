package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by Nir1612 on 26/12/2016.
 */
public class GcdScrewDriver implements Tool {

    public GcdScrewDriver() {
        type = "gs-driver";
    }

    private String type;
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

    private long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        long value= (b1.gcd(b2)).longValue();
        return value;
    }
    private long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
    }

//    public long useOn(Product p) {
//        long sumOfUsage = 0;
//        for (Product subPart : p.getParts()) {
//            BigInteger subPartID = BigInteger.valueOf(subPart.getFinalId());
//            long tempReverse = Long.reverse(subPart.getFinalId());
//            BigInteger reverseProductID = BigInteger.valueOf(tempReverse);
//            sumOfUsage += Math.abs(subPartID.gcd(reverseProductID).longValue());
//        }
//        return sumOfUsage;
//    }
}
