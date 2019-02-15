package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import exceptions.InvalidProductIdentifierException;
import order.Drink;

public class DrinkClassTests {
	
	private Drink d;
	
	private String name = "Coke";
	private String desc = "An ice-cold coke";
	private float price =  (float )1.99;
	private String cat = "Beverage" ;
	private String id = "BEV111";

	
	@Before
	public void setup() throws InvalidProductIdentifierException{
		d = new Drink(name, desc, price,cat,id);
	}
	
	@Test
	public void getNamePass() {

		assertEquals(name,d.getName());
		
	}
	
	@Test
	public void getDescPass() {
		
		assertEquals(desc,d.getDesc());
		
	}
	

	@Test
	public void getPricePass() {
		
		assertEquals(price, d.getPrice(),0.01);
		
	}

	
	@Test
	public void getCatPass() {
		
		assertEquals(cat,d.getCat());
	};
	
	@Test
	public void getIdPass() {
		
		assertEquals(id,d.getId());
	};

	@Test
	public void getMenuDisplayPass(){
		String x = name + " £" + price;
		assertEquals(x, d.getMenuDisplay());
		
	}
	
	@Test(expected = InvalidProductIdentifierException.class)
	public void throwsExceptionPass() throws InvalidProductIdentifierException {
		Drink exceptionDrink = new Drink("An exceptional Drink", "An exception Description", (float) 0.99, "Beverage", "DEV111");
	}
}
