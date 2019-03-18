package model;

import java.util.HashSet;
import java.util.Set;

import order.Product;

public class ProductsList {
	
	private static volatile ProductsList instance = null;
	private Set<Product> products = new HashSet<Product>();
	
	private ProductsList() {};

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
	
	public int getNumberOfProducts() {
		return products.size();
	}
	

	public Set<Product> getProducts() {
		return products;
	}
	
	/* Used to find the Product objects which are stored in orders*/
	public Product findProduct(String productID) {
		Product thisProduct = null;
		for (Product a : products) {
			if (a.getId().equals(productID)) {
				return a;
			}
		}
		return thisProduct;
	}
	
	public void addToProductsList(Product p) {
		products.add(p);
	}
	
	public void clearProductsList() {
		products.clear();
	}

}
