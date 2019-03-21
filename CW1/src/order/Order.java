package order;

/**
 * This class holds a single order which consists of one product
 * of type Food, Drink, or Memorabilia, a timestamp, and a customer ID.
 */
public class Order {

	private long timestamp;
	private Product product;
	private String custID;
	private int priority;
	
	/**
	 * Constructor
	 * @param timestamp: The timestamp for the order
	 * @param product: The product being purchased
	 * @param custID: The customer ID for the person who ordered it
	 * @param priority: The priority - 1 for online, 0 otherwise
	 */
	public Order(long timestamp, Product product, String custID, int priority) {
		this.timestamp = timestamp;
		this.product = product;
		this.custID = custID;
		this.priority = priority;
	}
	
	/**
	 * Getter for priority
	 * @return The order's priority
	 */
	public int getPriority() {
		return priority;
	}

	
	/**
	 * Getter for timestamp
	 * @return The order's timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	
	/**
	 * Getter for product
	 * @return The order's product
	 */
	public Product getProduct() {
		return product;
	}

	
	/**
	 * Getter for customer id
	 * @return The order's customer id
	 */
	public String getCustID() {
		return custID;
	}

	
	/**
	 * String serialisation used in order display
	 * @return String to represent the order
	 */
	public String toString() {
		return custID + " " + product.getId();
	}
}
