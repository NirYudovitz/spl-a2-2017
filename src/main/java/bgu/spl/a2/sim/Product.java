package bgu.spl.a2.sim;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that represents a product produced during the simulation
 */
public class Product implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    // represent the start id of product.
    private final long startId;
    // represent id after changes.
    private AtomicLong finalId;
    private String name;
    private List<Product> partList;
    // represnt the order to print product in collection of product.
    private int orderToPrint;

    /**
     * @return the order to print the prosuct.
     */
    public int getOrderToPrint() {
        return orderToPrint;
    }


    /**
     * Constructor
     *
     * @param startId - Product start id
     * @param name    - Product name
     */
    public Product(long startId, String name) {
        this.startId = startId;
        this.finalId = new AtomicLong(0);
        this.name = name;
        partList = new LinkedList<>();
        //if product doesnt have order - this case possibly when sub part of other product
        this.orderToPrint = -1;
    }

    /**
     * Product C-tor.
     *
     * @param startId    - Product start id
     * @param name       - Product name
     * @param orderPrint - represnt the order to print product in collection of product.
     */
    public Product(long startId, String name, int orderPrint) {
        this.startId = startId;
        this.finalId = new AtomicLong(0);
        this.name = name;
        partList = new LinkedList<>();
        this.orderToPrint = orderPrint;
    }

    /**
     * @return The product name as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The product start ID as a long. start ID should never be changed.
     */
    public long getStartId() {
        return startId;
    }

    /**
     * @return The product final ID as a long.
     * final ID is the ID the product received as the sum of all UseOn();
     */
    public long getFinalId() {
        return finalId.get();
    }

    /**
     * @return Returns all parts of this product as a List of Products
     */
    public List<Product> getParts() {
        return partList;
    }

    /**
     * Add a new part to the product
     *
     * @param p - part to be added as a Product object
     */
    public void addPart(Product p) {
        partList.add(p);
    }

    /**
     * @param id is the final id to be set to this product.
     */
    public void setFinalId(long id) {
        finalId.addAndGet(id);
    }

    /**
     * @return string represnting product and his parts.
     */
    @Override
    public String toString() {
        String toString = "";
        for (Product subPart : getParts()) {
            toString += subPart.toString();
        }
        return "ProductName: " + getName() + "  Product Id = " + getFinalId() + "\nPartsList {\n" + toString + "}\n";
    }
}
