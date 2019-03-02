package model;

import java.util.ArrayList;

import order.Order;
import order.Product;

public class Server implements Runnable {
	FileManagerIO f = FileManagerIO.getInstance();
	int threadID;
	
	public Server (int threadID)
	{
		this.threadID = threadID;
	}

	@Override
	public void run() {
		System.out.println(threadID);
		while(1 != 0) {
			try {
				ArrayList<Product> thisOrder = f.pop();
				System.out.println(thisOrder + " " + threadID);
				f.addCurrentOrder(thisOrder);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	
	
}
