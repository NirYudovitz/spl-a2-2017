package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

/**
 * A class represents a tool of type NextPrimeHammer
 */
public class NextPrimeHammer implements Tool {
    private String type;

    /**
     * NextPrimeHammer C-tor
     */
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
        long value = 0;
        for (Product part : p.getParts()) {
            value += Math.abs(func(part.getFinalId()));

        }
        return value;
    }

    /**
     * calculate the next prime number bigger then the produc's Id
     *
     * @param id - The Product's id
     * @return the value of the usage with the tool
     */
    private long func(long id) {

        long v = id + 1;
        while (!isPrime(v)) {
            v++;
        }

        return v;
    }

    /**
     * @param value is the value to check if is prime.
     * @return true if the recieved value is a prime number
     * false otherwise
     */
    private boolean isPrime(long value) {
        if (value < 2) return false;
        if (value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++) {
            if (value % i == 0) {
                return false;
            }
        }

        return true;
    }
}
