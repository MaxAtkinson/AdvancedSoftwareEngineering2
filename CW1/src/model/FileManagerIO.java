package model;

import java.util.*;

import customExceptions.InvalidProductIdentifierException;
import customExceptions.InvalidProductPriceException;
import order.Basket;
import order.Drink;
import order.Food;
import order.Memoribilia;
import order.Order;
import order.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
/**
 * This class is responsible for reading in CSV files and creating objects used throughout
 * the rest of the program.  
 */
public class FileManagerIO {
	//Making this a singleton class
	private static volatile FileManagerIO instance = null;
	
	/**
	 * Empty Constructor for Singleton
	 */
	private FileManagerIO() {}
	
	/** 
	 * Get the Singleton instance or create a new one first call
	 * @return instance of FileManagerIO
	 */
	public static FileManagerIO getInstance() {
		if(instance == null) {
			synchronized (FileManagerIO.class) {
				instance = new FileManagerIO();
			}
		}
		return instance;
	}

	private ArrayList<Order> processedOrders = new ArrayList<>();
	private CustomerQueue customerQueue = CustomerQueue.getInstance();
	private ProductsList productsList = ProductsList.getInstance();
	private StringBuilder logs = new StringBuilder();

	/**
	 * Get the size of existing orders read from file plus those added from GUI
	 * @return size of existing orders
	 */
	public int getSizeOfExistingOrders() {
		return processedOrders.size();
	}

	/**
	 * Append an event description to a string builder of logs to store later.
	 * @param eventDescription string describing event
	 */
	public void logEvent(String eventDescription) {
		logs.append(eventDescription + "\n");
	}
	
	/**
	 * Write logs to logs file.
	 * @throws IOException
	 */
	public void dumpLogs() throws IOException {
		FileWriter fw = new FileWriter ("Logs.txt"); 
		fw.write(logs.toString());
		fw.close();
	}

	/**
	 * Used to read in a CSV with product information. 
	 * Passes this read information to the processMenuLine() method.
	 * @param fileName products file
	 * @throws InvalidProductPriceException
	 * @throws InvalidProductIdentifierException
	 */
	public void readFromProductsFile(String fileName) throws InvalidProductPriceException, InvalidProductIdentifierException {
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);	
			String inputLine = scanner.nextLine(); // skip headers line
			// reset products list to prevent repeating info on each file read
			productsList.clearProductsList();
			while (scanner.hasNextLine()) {
				inputLine = scanner.nextLine();
				processMenuLine(inputLine);
			}
			scanner.close();
		}
		catch (FileNotFoundException e) {
			System.out.print("File: " + fileName + " cannot be found.");
		}
	}
	
	/**
	 * Takes in lines from the CSV and creates objects of type Drink, Food, or Memorabilia
	 * @param inputLine line containing the product details
	 * @throws NumberFormatException
	 * @throws InvalidProductPriceException
	 * @throws InvalidProductIdentifierException
	 */
	private void processMenuLine(String inputLine) throws NumberFormatException, InvalidProductPriceException, InvalidProductIdentifierException {
		String part[] = inputLine.split(",");
		String id = part[part.length-1];
		String name = part[0];
		String desc = part[1];
		try {
			float price = Float.parseFloat(part[2]);
			String cat = part[3];
			if (cat.contentEquals("Food")) {
				Food p = new Food(name, desc, price, id);
				productsList.addToProductsList(p);
			} else if (cat.contentEquals("Beverage")) {
				Drink p = new Drink(name, desc, price, id);
				productsList.addToProductsList(p);
			} else if (cat.contentEquals("Memorabilia")) {
				Memoribilia p = new Memoribilia(name, desc, price, id);
				productsList.addToProductsList(p);
				// no else for readability
			}
		} catch(NumberFormatException a) {
			throw new InvalidProductPriceException("Product ID " + id + " has an invalid price");

		}
	}
	
	/**
	 * Used to read in a CSV with previous orders information. 
	 * Passes this read information to the processOrderLine() method.
	 * @param fileName existing orders file
	 */
	public void readFromOrderFile(String fileName) {
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			String inputLine = scanner.nextLine(); // skip headers line
			// reset list of orders to prevent repeating info on each file read
			processedOrders = new ArrayList<>();
			while (scanner.hasNextLine()) {
				inputLine = scanner.nextLine();
				processOrderLine(inputLine);
			}
			setLastCustomerID();
			scanner.close();
		}
		catch (FileNotFoundException e) {
			System.out.print("File: " + fileName + " cannot be found.");
		}
	}

	/**
	 * Takes in lines from the CSV and creates objects of type Order
	 * placing them in the ArrayList existing orders.
	 * @param inputLine line containing the order details
	 */
	private void processOrderLine(String inputLine) {
		try {
			String part[] = inputLine.split(",");
			long timeStamp = Long.parseLong(part[0]);
			Product product = productsList.findProduct(part[2]);
			String custID = part[1];
			int priority = Integer.parseInt(part[3]);
			Order o = new Order(timeStamp, product, custID, priority);
			processedOrders.add(o);


		}
		catch(NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	/* */	
	/**
	 * Used to read in new orders ready to join the queue. 
	 * Adds new order to queue uses CustomerQueue Singleton.
	 * Sleeps after every line read to simulate gradual joining of queue.
	 * @param fileName new orders file
	 */
	public void readFromNewOrderFile(String fileName) {
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			String inputLine = scanner.nextLine(); // skip headers line
			int priority = 0; 
			ArrayList<Product> oneWholeOrder = new ArrayList<>();
			while (scanner.hasNextLine()) {
				inputLine = scanner.nextLine();
				if (inputLine.isEmpty()) {
					// one customer order finished
					customerQueue.addCustomer(priority, oneWholeOrder);
					Thread.sleep(Server.getThreadSleepTime());
					oneWholeOrder = new ArrayList<>();
				} else {
					String part[] = inputLine.split(",");
					Product product = productsList.findProduct(part[0]);
					priority = Integer.parseInt(part[1]);
					oneWholeOrder.add(product);
				}
			}
			// add final order in file if there is not an empty line at end
			customerQueue.addCustomer(priority, oneWholeOrder);
			scanner.close();
		}
		catch (FileNotFoundException e) {
			System.out.print("File: " + fileName + " cannot be found.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	/* Creates a new customer idea for new orders passed from the GUI.*/
	/**
	 * Retrieve the last customer ID from the processed orders and set
	 * the customer queue's last ID
	 */
	private void setLastCustomerID() {
		if (processedOrders.size()!=0) {
			Order lastOrder = processedOrders.get(processedOrders.size()-1);
			String lastCustomerID = lastOrder.getCustID();
			long lastCustID = Long.parseLong(lastCustomerID.substring(3));
			customerQueue.setLastCustomerID(lastCustID);
		}
	}

	/**
	 * Write all processed orders to file.
	 * @param o Order object written as line to file.
	 * @throws IOException
	 */
	public void store(Order o) throws IOException {
		FileWriter fw = new FileWriter("Orders.csv", true);
		String timestamp = Long.toString(o.getTimestamp());
		String customerID = o.getCustID();
		Product product = o.getProduct();
		String productID = product.getId();
		fw.write(timestamp + "," + customerID + "," + productID + "," + o.getPriority() +"\n");
		processedOrders.add(o);
		fw.close();
	}
	
	/**
	 * Write a full order to file using the store(Order o) method.
	 * @param orders ArrayList<Order> a customer's full order
	 * @throws IOException
	 */
	public synchronized void store(ArrayList<Order> orders) throws IOException {
		for (Order o : orders) {
			store(o);
			logEvent(String.format("Processing order of %s for %s", o.getProduct().getId(), o.getCustID()));		
		}
	}


	/**
	 * Used by writeReport() to find the number of times a product was ordered
	 * @param p Product to search for.
	 * @return
	 */
	private int timesProductWasOrdered(Product p) {
		int timesOrdered = 0;
		for(Order o: processedOrders) {
			if(o.getProduct() == p) {
				timesOrdered++;
			}
		}
		return timesOrdered;
	}

	
	/**
	 * Calculate the number of online orders.
	 * @return number of online orders.
	 */
	private int numberOfOnlineOrders() {
		int priorityOrders = 0;
		for(Order p: processedOrders) {
			if(p.getPriority() == 1) {
				priorityOrders++;
			}
		}
		return priorityOrders;
	}
	
	/**
	 * Used by WriteReport() method to calculate income.
	 * @return total income
	 */
	private float totalIncome() {
		float totalIncome = 0;
		ArrayList<Product> oneCustomer = new ArrayList<>();
		String custID = "";
		for(int i=0; i < processedOrders.size(); i++) {
			Order o = processedOrders.get(i);
			if (o.getCustID().equals(custID)) {
				oneCustomer.add(o.getProduct());
				if (i == processedOrders.size()-1) {
					totalIncome += Basket.calculateDiscountedTotal(oneCustomer);
				}
			} else {
				totalIncome += Basket.calculateDiscountedTotal(oneCustomer);
				oneCustomer.clear();
				oneCustomer.add(o.getProduct());
				custID = o.getCustID();
				if (i == processedOrders.size()-1) {
					totalIncome += o.getProduct().getPrice();
				}
			}
		}
		return totalIncome;
	}

	/**
	 * Write the report to file, containing information of products, number of times
	 * they are ordered, total income, and number of online orders.
	 * @param filename report file to write to
	 * @throws IOException
	 */
	public void writeReport(String filename) throws IOException {
		FileWriter fw = new FileWriter (filename); 
		fw.write("These are all the products on offer:\n");
		String tableHeading = String.format("|%-25s|%-10s|%-65s|%25s|\n", "Name Of Product", "Price", "Description", "Times Product Was Ordered");
		fw.write(tableHeading);
		for (Product p: productsList.getProducts()) {
			String tableLine = String.format("|%-25s|%-10.2f|%-65s|%25d|\n", p.getName(), p.getPrice(), p.getDesc(), timesProductWasOrdered(p));
			fw.write(tableLine);
		}
		String totalIncome = String.format("The total income was %.2f\n", totalIncome());
		fw.write(totalIncome);
		String onlineOrders = String.format("The total number of online orders was %d\n", numberOfOnlineOrders());
		fw.write(onlineOrders);
		fw.close();
	}
}