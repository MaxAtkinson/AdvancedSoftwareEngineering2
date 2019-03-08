package model;

import java.util.ArrayList;
import java.util.Observable;

import order.Order;

public class Server extends Observable implements Runnable {
	CustomerQueue cq = CustomerQueue.getInstance();
	int threadID;
	
	ArrayList<Order> currentOrder;
	
	public Server (int threadID)
	{
		this.threadID = threadID;
		currentOrder = new ArrayList<>();
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

	@Override
	public void run() {
		while(1 != 0) {
			try {
				currentOrder = cq.getNextCustomer();
				System.out.println(currentOrder + " " + threadID);
				notifyUpdate();
				// TODO process order
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void notifyUpdate() {
		setChanged();
		notifyObservers(this);
		clearChanged();
	}
}
