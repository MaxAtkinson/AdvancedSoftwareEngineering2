package customExceptions;

/**
 * Custom exception which is triggered by a product being read in whose product ID is wrong.
 *
 */
public class InvalidProductIdentifierException extends Exception {
	/**
	 * Creates the custom exception.
	 * 
	 * @param message a message that will be shown on the console should a product with 
	 * invalid details be read it
	 */
	public InvalidProductIdentifierException(String message) {
		super(message);
	}
}
