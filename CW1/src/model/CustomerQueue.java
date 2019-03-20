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
	private static ArrayList<ArrayList<Order>> queue;
		private final Object lock = new Object();
	private static long lastCustID = 0;
	private static int endOfPriorityIndex = 0;
	

	private CustomerQueue() {}

	public static CustomerQueue getInstance() {
		if(firstInstance == null) {
			firstInstance = new CustomerQueue();
			queue = new ArrayList<>();
			//--------------
		}
		return firstInstance;
	}

		
	public ArrayList<ArrayList<Order>> getQueue() {
		return queue;
	}
	
	
	public int getQueueSize() {
		return queue.size();
	}
	
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
			if (queue.get(0).get(0).getPriority() == 1) {
				endOfPriorityIndex--;
			}
			ArrayList<Order> o = queue.remove(0);
			notifyUpdate();
			return o;
		}
	}
	
	
	public void notifyUpdate() {
		setChanged();
		notifyObservers(this);
		clearChanged();
	}
}
