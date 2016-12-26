package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by Nir1612 on 26/12/2016.
 */
public class GcdScrewDriver implements Tool {

    public GcdScrewDriver() {
        type = "GcdScrewDriver";
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
    public long useOn(Product p) {
        BigInteger productID = BigInteger.valueOf(p.getStartId());
        long tempReverse = Long.reverse(p.getStartId());
        BigInteger reverseProductID = BigInteger.valueOf(tempReverse);
        long ScrewDriverID = (productID.gcd(reverseProductID)).longValue();
        return ScrewDriverID;
    }
}
