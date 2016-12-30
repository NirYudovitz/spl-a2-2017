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
    public long useOn(Product p) {
        long sumOfUsage = 0;
        for(Product subPart : p.getParts()) {
            BigInteger partID = BigInteger.valueOf(subPart.getFinalId());
            sumOfUsage += Math.abs(partID.nextProbablePrime().longValue());
        }
        return sumOfUsage;
    }
}
