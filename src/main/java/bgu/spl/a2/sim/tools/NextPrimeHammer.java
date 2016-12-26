package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by Nir1612 on 26/12/2016.
 */
public class NextPrimeHammer implements Tool {
    /**
     * @return tool name as string
     */
    @Override
    public String getType() {
        return "NextPrimeHammer";
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
        long HammerID = (productID.nextProbablePrime()).longValue();
        return HammerID;
    }
}
