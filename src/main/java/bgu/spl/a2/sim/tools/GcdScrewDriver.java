package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * A class represents a tool of type GcdScrewDriver
 */
public class GcdScrewDriver implements Tool {
    /**
     * GcdScrewDriver C-tor
     */
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

    /**
     * calculate the gcd of the id with it's reverse
     *
     * @param id - The Product's id
     * @return the value of the usage with the tool
     */
    private long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        long value= (b1.gcd(b2)).longValue();
        return value;
    }

    /**
     *
     * @param n A long number represnts the Product Id
     * @return the reversed Id
     */
    private long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n%10;
            n = n/10;
        }
        return reverse;
    }
}
