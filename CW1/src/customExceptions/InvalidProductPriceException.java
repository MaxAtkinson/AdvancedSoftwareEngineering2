package customExceptions;

/**
 * Custom exception which is triggered by a product being read in whose product price is not correctly 
 * formatted in the file.
 *
 */
public class InvalidProductPriceException extends Exception {
	/**
	 * Creates the custom exception
	 * 
	 * @param message a message that will be shown on the console should a product with 
	 * invalid details be read it
	 */
	public InvalidProductPriceException(String message) {
		super(message);
	}
}

