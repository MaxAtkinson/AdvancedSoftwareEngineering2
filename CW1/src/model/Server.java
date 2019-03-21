package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import order.Basket;
import order.Order;
import order.Product;

/**
 * Object to represent a single server in a multi-server system.
 * Each server runs on its own thread and processes orders.
 */
public class Server extends Observable implements Runnable {
	private static int THREAD_SLEEP_TIME = 1000;
	private CustomerQueue cq = CustomerQueue.getInstance();
	private FileManagerIO f = FileManagerIO.getInstance();
	private int threadID;
	private ArrayList<Order> currentOrder;
	ArrayList<Product> products;
	private boolean active;
	
	/**
	 * Constructor to provide the server with a unique ID
	 * @param threadID
	 */
	public Server (int threadID) {
		this.threadID = threadID;
		currentOrder = new ArrayList<>();
		products = new ArrayList<>();
		active = true;
	}
	
	/**
	 * Get the ID of this server thread
	 * @return ID of the server
	 */
	public int getId() {
		return threadID;
	}
	
	/**
	 * Generate a string array that can be used for GUI
	 * @return string array containing details of order or idle message
	 */
	public String[] displayOrder() {
		if (currentOrder.size() > 0) {
			String[] list = new String[currentOrder.size()+2];
			list[0] = "Processing Customer ID: " + currentOrder.get(0).getCustID();
			list[1] = "";
			for (int i = 0; i < currentOrder.size(); i++) {
				list[i+1] = currentOrder.get(i).getProduct().getName();
			}
			float discTotal = Basket.calculateDiscountedTotal(products);
			float disc = Basket.calculateTotalPrice(products)-discTotal;
			list[list.length-1] = "Total £" + String.format("%.2f", discTotal) + " (with £" + String.format("%.2f", disc) + " discount)";
			return list;
		} else {
			return new String[] {"Ready To Serve"};
		}
	}
	
	/**
	 * Setter to enable/disable the thread
	 * @param active boolean determine the state of the server
	 */
	public void setActive(boolean active) {
		this.active = active;
	}


	@Override
	public void run() {
		while(active) {
			try {
				currentOrder = cq.getNextCustomer();
				products.clear();
				for (Order o : currentOrder) {
					products.add(o.getProduct());
				}
				notifyUpdate();
				f.store(currentOrder);
				int sleep = currentOrder.size() > 0 ? currentOrder.size() : 1;
				sleep = THREAD_SLEEP_TIME * sleep;
				Thread.sleep(sleep);
				cq.incrementProcessedOrders();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Notify all observers of this object
	 */
	public void notifyUpdate() {
		setChanged();
		notifyObservers(this);
		clearChanged();
	}

	/**
	 * Set the time per item that all servers take to process
	 * @param threadSleepTime used as a factor for determining sleep time
	 */
	public static void setThreadSleepTime(int threadSleepTime) {
		THREAD_SLEEP_TIME = threadSleepTime * 1000;
	}
	
	/**
	 * Get the time it takes for all servers to process each item
	 * @return thread sleep time
	 */
	public static int getThreadSleepTime(){
		return THREAD_SLEEP_TIME;
	}

	/**
	 * Checker to determine the state of the server
	 * @return
	 */
	public boolean isActive() {
		return active;
	}
}

