package model;

import java.util.Observable;
import model.FileManagerIO;

public class Qthread extends Observable implements Runnable {

	FileManagerIO f = FileManagerIO.getInstance();

	@Override
	public void run() {
		f.readFromNewOrderFile("NewOrders.csv");
	}
}
