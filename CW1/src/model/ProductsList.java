package model;

import java.util.HashSet;
import java.util.Set;

import order.Product;

/**
 * Wrapper class to hold all products which are read in from file.
 *
 */
public class ProductsList {
	
	private static volatile ProductsList instance = null;
	private Set<Product> products = new HashSet<Product>();
	
	/**
	 * Empty Constructor for Singleton
	 */
	private ProductsList() {};

	/** 
	 * Get the Singleton instance or create a new one first call
	 * @return instance of ProductsList
	 */
	public static ProductsList getInstance(){
	    if(instance == null){
	        synchronized (ProductsList.class) {
	            if(instance == null){
	                instance = new ProductsList();
	            }
	        }
	    }
	    return instance;
	}
	
	/**
	 * Get the number of products read from file into the list
	 * @return number of products
	 */
	public int getNumberOfProducts() {
		return products.size();
	}
	

	/**
	 * Get the Set of all unique products
	 * @return products HashSet
	 */
	public Set<Product> getProducts() {
		return products;
	}
	
	
	/**
	 * Used to find Product objects by their ID. 
	 * @param productID
	 * @return Product object
	 */
	public Product findProduct(String productID) {
		Product thisProduct = null;
		for (Product a : products) {
			if (a.getId().equals(productID)) {
				return a;
			}
		}
		return thisProduct;
	}
	
	/**
	 * Add a new product to the products Set
	 * @param p Product to add
	 */
	public void addToProductsList(Product p) {
		products.add(p);
	}

	/**
	 * Remove all products from the Set
	 */
	public void clearProductsList() {
		products.clear();
	}

}
