package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import order.Order;
import order.Product;

public class CustomerQueue extends Observable {
	private static CustomerQueue firstInstance = null;
	private static LinkedList<ArrayList<Order>> queue;
	private final Object lock = new Object();
	private static long lastCustID = 0;
	private static int endOfPriorityIndex = 0;
	
	private CustomerQueue() {}
	public static CustomerQueue getInstance() {
		if(firstInstance == null) {
			firstInstance = new CustomerQueue();
			queue = new LinkedList<>();
		}
		return firstInstance;
	}

	public LinkedList<ArrayList<Order>> getQueue() {
		return queue;
	}
	
	public int getQueueSize() {
		return queue.size();
	}
	
	public synchronized String[] getCustomerIDs() {
		String[] custIDs = new String[getQueueSize()];
		int index = 0;
		for (Iterator<ArrayList<Order>> i = queue.iterator(); i.hasNext();) {
			custIDs[index] = i.next().get(0).getCustID();
			index++;
		}
		return custIDs;
	}
	
	public synchronized void addCustomer(int priority, ArrayList<Product> orderList) {
		Date date = new Date();
		long timeStamp = date.getTime();
		ArrayList<Order> wholeOrder = new ArrayList<>();
		String customerID = "CUS" + lastCustID;
		lastCustID++;
		for (Product p : orderList) {
			Order o = new Order(timeStamp, p, customerID, 1);
			wholeOrder.add(o); // insert after online orders
		}
		if (priority == 1) {
			queue.add(endOfPriorityIndex, wholeOrder);
			endOfPriorityIndex++;
		} else {
			queue.add(wholeOrder);
		}
		notifyUpdate();
	}
	
	public void setLastCustomerID(long lastCustID) {
		CustomerQueue.lastCustID = lastCustID;
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public ArrayList<Order> getNextCustomer() throws InterruptedException {
		synchronized (lock) {
			while(queue.isEmpty()) {
				lock.wait();
			}
			ArrayList<Order> o = queue.removeFirst();
			notifyUpdate();
			return o;
		}
	}
	
//	public synchronized ArrayList<Order> getNextCustomer() {
//		ArrayList<Order> o = queue.removeFirst();
//		notifyUpdate();
//		return o;
//	}
	
	public void notifyUpdate() {
		setChanged();
		notifyObservers(this);
		clearChanged();
	}
}