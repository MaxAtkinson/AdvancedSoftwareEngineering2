package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import order.Order;
import order.Product;

/**
 * Wrapper class to hold the queue of customers to be processed by
 * the servers.
 */
public class CustomerQueue extends Observable {
	private static CustomerQueue firstInstance = null;
	private static ArrayList<ArrayList<Order>> queue;
	private static long lastCustID = 0;
	private static int endOfPriorityIndex = 0;
	

	/**
	 * Empty Constructor for Singleton
	 */
	private CustomerQueue() {}

	/**
	 * Get the Singleton instance or create a new one first call
	 * @return instance of CustomerQueue
	 */
	public static CustomerQueue getInstance() {
		if(firstInstance == null) {
			firstInstance = new CustomerQueue();
			queue = new ArrayList<>();
		}
		return firstInstance;
	}
	
	/**
	 * Get the ArrayList of whole orders
	 * @return queue of orders
	 */
	public ArrayList<ArrayList<Order>> getQueue() {
		return queue;
	}
	
	/**
	 * Get the size of the queue in terms of number of customers
	 * @return size of queue
	 */
	public int getQueueSize() {
		return queue.size();
	}
	
	/**
	 * Get the details of the queue including customer ID, number of items, and priority
	 * Synchronized to prevent concurrent access
	 * @return string array, each element containing ID, number of items, and priority
	 */
	public synchronized String[] getCustomerIDs() {
		String[] custIDs = new String[getQueueSize()];
		int index = 0;
		for (Iterator<ArrayList<Order>> i = queue.iterator(); i.hasNext();) {
			ArrayList<Order> o = i.next();
			String items = " items";
			String pri = "Online";
			if (o.size() == 1) { items = " item"; }
			if (o.get(0).getPriority() == 0) { pri = "In-Store"; }
			custIDs[index] = o.get(0).getCustID() + ", " + o.size() + items + ", " + pri;
			index++;
		}
		return custIDs;
	}
	
	/**
	 * Add a customer to the queue, inserting behind the last online customer if an online
	 * order is placed, otherwise added to the end of the queue.
	 * @param priority whether an order is placed in-store or online
	 * @param orderList the full order of Order objects that a single customer placed
	 */
	public synchronized void addCustomer(int priority, ArrayList<Product> orderList) {
		Date date = new Date();
		long timeStamp = date.getTime();
		ArrayList<Order> wholeOrder = new ArrayList<>();
		lastCustID++;
		String customerID = "CUS" + lastCustID;
		for (Product p : orderList) {
			Order o = new Order(timeStamp, p, customerID, priority);
			wholeOrder.add(o); // insert after online orders
		}
		if (priority == 1) {
			queue.add(endOfPriorityIndex, wholeOrder);
			endOfPriorityIndex++;
		} else {
			queue.add(wholeOrder);
		}
		FileManagerIO.getInstance().logEvent(String.format("Timestamp %d: %s was added to the queue (priority %d).", timeStamp, customerID, priority));
		notifyUpdate();
	}
	
	/**
	 * Sets the last customer customer ID on file.
	 * Called by FileManagerIO.
	 * @param lastCustID the last customer ID on file.
	 */
	public void setLastCustomerID(long lastCustID) {
		CustomerQueue.lastCustID = lastCustID;
	}

	/**
	 * Check if queue is empty.
	 * @return boolean of queue being empty
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	/**
	 * Get the whole order of the next customer in the queue.
	 * @return ArrayList<Order> of next customer
	 */
	public synchronized ArrayList<Order> getNextCustomer(){
		if (isEmpty()) {
			return new ArrayList<Order>();
		} else {
			if (queue.get(0).get(0).getPriority() == 1) {
				endOfPriorityIndex--;
			}
			ArrayList<Order> o = queue.remove(0);
			notifyUpdate();
			return o;
		}
	}
	
	
	/**
	 * Notify all observers of the queue.
	 */
	public void notifyUpdate() {
		setChanged();
		notifyObservers(this);
		clearChanged();
	}
}