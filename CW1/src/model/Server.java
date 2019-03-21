package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import order.Basket;
import order.Order;
import order.Product;

public class Server extends Observable implements Runnable {
	private static int THREAD_SLEEP_TIME = 1000;
	private static int LAST_SLEEP_TIME = THREAD_SLEEP_TIME;
	private CustomerQueue cq = CustomerQueue.getInstance();
	private FileManagerIO f = FileManagerIO.getInstance();
	private int threadID;
	private ArrayList<Order> currentOrder;
	ArrayList<Product> products;
	private boolean active;
	
	public Server (int threadID) {
		this.threadID = threadID;
		currentOrder = new ArrayList<>();
		products = new ArrayList<>();
		active = true;
	}
	
	public int getId() {
		return threadID;
	}
	
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
	
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void run() {
		while(active) {
			try {
				currentOrder = cq.getNextCustomer();
				products.clear();
				LAST_SLEEP_TIME = THREAD_SLEEP_TIME * currentOrder.size();
				for (Order o : currentOrder) {
					products.add(o.getProduct());
				}
				notifyUpdate();
				f.store(currentOrder);
				Thread.sleep(LAST_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		notifyUpdate();
	}
	
	public void notifyUpdate() {
		setChanged();
		notifyObservers(this);
		clearChanged();
	}

	public static void setThreadSleepTime(int threadSleepTime) {
		THREAD_SLEEP_TIME = threadSleepTime * 1000;
	}
	
	public static int getThreadSleepTime(){
		return LAST_SLEEP_TIME;
	}

	public boolean isActive() {
		return active;
	}
}

