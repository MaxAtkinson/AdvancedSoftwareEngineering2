package model;

import java.util.ArrayList;
import java.util.Observable;

import order.Order;

public class Server extends Observable implements Runnable {
	private static int THREAD_SLEEP_TIME = 1000;
	private CustomerQueue cq = CustomerQueue.getInstance();
	private int threadID;
	private ArrayList<Order> currentOrder;
	private boolean active;
	
	public Server (int threadID) {
		this.threadID = threadID;
		currentOrder = new ArrayList<>();
		active = true;
	}
	
	public int getId() {
		return threadID;
	}
	
	public String[] displayOrder() {
		String[] list = new String[currentOrder.size()+1];
		list[0] = "Customer ID: " + currentOrder.get(0).getCustID();
		list[1] = "";
		for (int i = 0; i < currentOrder.size(); i++) {
			list[i+1] = currentOrder.get(i).getProduct().getId() + " " +currentOrder.get(i).getPriority();
		}
		return list;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void run() {
		while(active) {
			try {
				currentOrder = cq.getNextCustomer();
				notifyUpdate();
				// TODO process order
				Thread.sleep(THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		Server.THREAD_SLEEP_TIME = threadSleepTime * 1000;
	}
	
	public static int getThreadSleepTime(){
		return THREAD_SLEEP_TIME;
		
	}

	public boolean isActive() {
		return active;
	}
}

