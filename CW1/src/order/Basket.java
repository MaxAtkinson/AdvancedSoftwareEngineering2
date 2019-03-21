package order;

import java.util.ArrayList;

public class Basket {
	private ArrayList<Product> products;
	
	/**
	 * Creates the Basket
	 */
	public Basket() {
		products = new ArrayList<>();
	}
	
	/**
	 * Getter for the products list
	 * @return A list of products in the basket
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	/**
	 * Adds Products to underlying list
	 * @param p: The product to be added
	 */
	public void addProduct(Product p) {
		products.add(p);
	}
	
	/** 
	 * Removes Products from underlying list
	 * @param p: The product to be removed
	 */
	public void removeProduct(Product p) {
		products.remove(p);
	}
	
	/**
	 *  Clears products list
	 */
	public void clearBasket() {
		products.clear();
	}
	
	/**
	 *  Calculates the discounted total
	 * @param pList: The list of products
	 * @return The discounted total for the list of products
	 */
	public static float calculateDiscountedTotal(ArrayList<Product> pList) {
		float result = calculateTotalPrice(pList);
		int countMemoribilia = 0;
		int countDrinks = 0;
		int countFood = 0;
		
		// Count up instances of Drink and Mem
		for (Product p: pList) {
			if (p instanceof Memoribilia) countMemoribilia += 1;
			else if (p instanceof Drink) countDrinks += 1;
			else if (p instanceof Food) countFood +=1;
		}
		
		// If 2+ Mem and 1+ Drink, 20% off
		if (countMemoribilia >= 2 && countDrinks >= 1) {
			result -= (0.2 * result);
		}
		
		else if (countFood >= 2 && countDrinks >= 2) {
			result -= (0.3 * result);
		}
		
		else if (countFood == 1 && countDrinks == 1 && countMemoribilia == 1) {
			result -= (0.1 * result);
		}
		
		return result;
	}

	/**
	 *  Calculates the total before discounts
	 * @param pList: The list of products
	 * @return The total for the list of products before discounts are applied
	 */
	public static float calculateTotalPrice(ArrayList<Product> pList) {
		float total = 0;
		
		// Total up the prices from products
		for (Product p: pList) {
			total += p.getPrice();
		}
		
		return total;
	}
}