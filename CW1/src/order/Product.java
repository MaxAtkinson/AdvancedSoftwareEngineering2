package order;

/**
 * This class is a base class for all products in a menu file.
 * Derived classes Food, Drink, and Memorabilia extend from this.
 */
public class Product {

	private String name;
	private String desc;
	private float price;
	private String id;
	
	/**
	 * Constructor
	 * @param name: The name of the product
	 * @param desc: The product description
	 * @param price: The price of the product
	 * @param id: The product ID
	 */
	public Product(String name, String desc, float price, String id) {
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.id = id;
	}
	
	/**
	 * Getter for name
	 * @return: The product name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for description
	 * @return: The product description
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Getter for price
	 * @return: The product price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Getter for ID
	 * @return: The product ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * String serialization
	 * @return: A string serialization of the product
	 */
	public String toString() { 
	    return name;   		
	} 
}
