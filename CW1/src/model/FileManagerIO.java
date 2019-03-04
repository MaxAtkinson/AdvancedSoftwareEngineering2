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
public class FileManagerIO implements Runnable {
	//Making this a singleton class
	private static FileManagerIO firstInstance = null;
	private static int numberOfServers = 2;
	private final Object lock = new Object();
	private FileManagerIO() {}
	public static FileManagerIO getInstance() {
		if(firstInstance == null) {
			firstInstance = new FileManagerIO();
		}
		return firstInstance;
	}

	private ArrayList<Order> processedOrders = new ArrayList<>();
	private Set<Product> products = new HashSet<Product>();
	public LinkedList<Order> customerQueue = new LinkedList<>(); 
	private int endOfPriorityIndex = 0;
	private long lastCustID = 0;

	/* Getters are used in Junit tests to ensure the effectiveness of private methods elsewhere in the class. */
	public int getSizeOfExistingOrders() 
	{
		return processedOrders.size();
	}

	public int getNumberOfProducts() 
	{
		return products.size();
	}

	public Set<Product> getProducts() {
		return products;
	}

	/* Used to read in a CSV with product information. Passes this read information to the processMenuLine() method.*/
	public void readFromProductsFile(String fileName) throws InvalidProductPriceException, InvalidProductIdentifierException
	{
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);	
			String inputLine = scanner.nextLine(); // skip headers line
			// reset products list to prevent repeating info on each file read
			products = new HashSet<Product>();
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

	/* Takes in lines from the CSV and creates objects of type Drink, Food, or Memorabilia */
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
				products.add(p);
			} else if (cat.contentEquals("Beverage")) {
				Drink p = new Drink(name, desc, price, id);
				products.add(p);
			} else if (cat.contentEquals("Memorabilia")) {
				Memoribilia p = new Memoribilia(name, desc, price, id);
				products.add(p);
				// no else for readability
			}
		} catch(NumberFormatException a) {
			throw new InvalidProductPriceException("Product ID " + id + " has an invalid price");

		}
	}
	/* Used to read in a CSV with previous orders information. Passes this read information to the processOrderLine() method.*/	
	public void readFromOrderFile(String fileName) 
	{
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

	/* Takes in lines from the CSV and creates objects of type Order placing them in the ArrayList existing orders.*/
	private void processOrderLine(String inputLine) {
		try {
			String part[] = inputLine.split(",");
			long timeStamp = Long.parseLong(part[0]);
			Product product = findProduct(part[2]);
			String custID = part[1];
			int priority = Integer.parseInt(part[3]);
			Order o = new Order(timeStamp, product, custID, priority);
			processedOrders.add(o);


		}
		catch(NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	/* Used to read in a CSV with previous orders information. Passes this read information to the processOrderLine() method.*/	
	public void readFromNewOrderFile(String fileName) 
	{
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
					addOrderToQueue(priority, oneWholeOrder);
					oneWholeOrder = new ArrayList<>();
				} else {
					String part[] = inputLine.split(",");
					Product product = findProduct(part[0]);
					priority = Integer.parseInt(part[1]);
					oneWholeOrder.add(product);
				}
			}
			// add final order in file if there is not an empty line at end
			addOrderToQueue(priority, oneWholeOrder);
			scanner.close();
		}
		catch (FileNotFoundException e) {
			System.out.print("File: " + fileName + " cannot be found.");
		}
	}
	
	private void addOrderToQueue(int priority, ArrayList<Product> pList) {
		if (!pList.isEmpty()) {
			Date date = new Date();
			long timeStamp = date.getTime();
			String customerID = "CUS" + lastCustID;
			lastCustID++;
			for (Product p : pList) {
				Order o = new Order(timeStamp, p, customerID, priority);
				if (priority == 1) {
					customerQueue.add(endOfPriorityIndex, o); // insert after online orders
					endOfPriorityIndex++;
				} else {
					customerQueue.add(o); // add at end
				}
			}
		}
	}

	/* Creates a new customer idea for new orders passed from the GUI.*/
	private void setLastCustomerID() {
		if (processedOrders.size()!=0) {
			Order lastOrder = processedOrders.get(processedOrders.size()-1);
			String lastCustomerID = lastOrder.getCustID();
			lastCustID = Long.parseLong(lastCustomerID.substring(3));
			lastCustID++;
		}
	}

	/* Adds new orders to the existing orders array list passed from the GUI*/
	//////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////
	// TODO REFACTOR/REMOVE BECAUSE ORDERS ARE NOT PROCESSED IMMEDIATELY ANYMORE - NEEDS TO ADD TO QUEUE
	public void addCurrentOrder(ArrayList<Product> pList) {
		Date date = new Date();
		long timeStamp = date.getTime();
		String customerID = "CUS" + lastCustID;
		lastCustID++;
		for (Product p : pList) {
			Order o = new Order(timeStamp, p, customerID, 0);
			//TODO In GUI check if adding priority customer.
			try {
				store(o);
				processedOrders.add(o);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	///////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////

	/* Used to find the Product objects which are stored in orders*/
	private Product findProduct(String productID) {
		Product thisProduct = null;
		for (Product a : products) {
			if (a.getId().equals(productID)) {
				return a;
			}
		}
		return thisProduct;
	}

	/* Writes new orders to the Orders.csv*/
	public void store(Order o) throws IOException {
		FileWriter fw = new FileWriter("Orders.csv", true);
		String timestamp = Long.toString(o.getTimestamp());
		String customerID = o.getCustID();
		Product product = o.getProduct();
		String productID = product.getId();
		fw.write(timestamp + "," + customerID + "," + productID + "," + 0 +"\n");
		fw.close();
	}

	/* Used by writeReport() to find the number of times a product was ordered*/
	private int timesProductWasOrdered(Product p) {
		int timesOrdered = 0;
		for(Order o: processedOrders) {
			if(o.getProduct() == p) {
				timesOrdered++;
			}
		}
		return timesOrdered;
	}

	/* Used by writeReport() to calculate the total income.*/
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

	/* Writes the final report to file. Called by the GUI on clicking the 'QUIT' button.*/
	public void writeReport(String filename) throws IOException {
		FileWriter fw = new FileWriter (filename); {
			fw.write("These are all the products on offer:\n");
			for (Product p: products) {
				fw.write(p.getName() + "£" + p.getPrice() + " " + p.getDesc() + 
						". This item was ordered a total of " + timesProductWasOrdered(p) + " times.\n");
			}
			fw.write("The total income was: " + totalIncome() + "\n");
			fw.close();
		}
	}

	@Override
	public void run() {
		for(int x=0; x<numberOfServers; x++) {
			Server s = new Server(x);
			Thread server = new Thread(s);
			server.start();
		}
	}

	public ArrayList<Order> pop() throws InterruptedException {
		synchronized (lock) {
			while(customerQueue.isEmpty()) {
				lock.wait();
			}
			ArrayList<Order> wholeOrder = new ArrayList<>();
			String custId = customerQueue.getFirst().getCustID();
			while (!customerQueue.isEmpty() && custId.equals(customerQueue.getFirst().getCustID())) {
				wholeOrder.add(customerQueue.removeFirst());
			}
			return wholeOrder;
		}
	}
}