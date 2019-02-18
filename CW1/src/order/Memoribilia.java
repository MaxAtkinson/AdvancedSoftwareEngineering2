package order;

import customExceptions.InvalidProductIdentifierException;
import customExceptions.InvalidProductPriceException;

public class Memoribilia extends Product {

	public Memoribilia(String name, String desc, float price, String id) throws InvalidProductPriceException, InvalidProductIdentifierException {
		super(name, desc, price, id);
		if(!(id.startsWith("MEM"))) {
			throw new InvalidProductIdentifierException(String.format("Invalid ID on product with name: %s", name));
		}
	}

}
