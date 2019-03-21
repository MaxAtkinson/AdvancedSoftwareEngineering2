package model;

import java.util.Observable;
import model.FileManagerIO;

/**
 * Thread which reads in the orders currently queued up on file.
 * Has thread sleeping inside the method to simulate gradual
 * additions to the queue.
 */
public class Qthread extends Observable implements Runnable {

	FileManagerIO f = FileManagerIO.getInstance();

	@Override
	public void run() {
		f.readFromNewOrderFile("NewOrders.csv");
	}
}
