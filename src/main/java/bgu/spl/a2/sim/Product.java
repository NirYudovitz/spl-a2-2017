package bgu.spl.a2.sim;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class that represents a product produced during the simulation
 */
public class Product {
    private final long startId;
    private AtomicLong currentId;
    private String name;
    private List<Product> partList;

    public int getOrderToPrint() {
        return orderToPrint;
    }

    private int orderToPrint;

    /**
     * Constructor
     *
     * @param startId - Product start id
     * @param name    - Product name
     */
    public Product(long startId, String name) {
        this.startId = startId;
        this.currentId = new AtomicLong(0);
        this.name = name;
        partList = new LinkedList<>();
        this.orderToPrint = -1;
    }

    public Product(long startId, String name, int orderPrint) {
        this.startId = startId;
        this.currentId = new AtomicLong(0);
        this.name = name;
        partList = new LinkedList<>();
        this.orderToPrint = orderPrint;
    }

    /**
     * @return The product name as a string
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
        return currentId.get();
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
        // TODO: 26/12/2016 maybe sync?
    }

    public void setFinalId(long id) {
        currentId.addAndGet(id);
    }

    @Override
    public String toString() {
        String toString = "";
        for (Product subPart : getParts()) {
            toString += subPart.toString();
        }
        return "ProductName: " + getName() + "  Product Id = " + getFinalId() + "\nPartsList {\n" + toString + "}\n";
    }
}
