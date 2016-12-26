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
	/**
	* Constructor 
	* @param startId - Product start id
	* @param name - Product name
	*/
    public Product(long startId, String name){
    	this.startId = startId;
    	this.currentId = new AtomicLong(startId);
    	this.name = name;
		partList = new LinkedList<>();
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
    public long getStartId(){
    	return startId;
	}

	/**
	* @return The product final ID as a long. 
	* final ID is the ID the product received as the sum of all UseOn(); 
	*/
    public long getFinalId(){
    	return currentId.get();
	}

	/**
	* @return Returns all parts of this product as a List of Products
	*/
    public List<Product> getParts(){
    	return partList;
	}

	/**
	* Add a new part to the product
	* @param p - part to be added as a Product object
	*/
    public void addPart(Product p){
    	partList.add(p);
		// TODO: 26/12/2016 maybe sync?
	}


}
