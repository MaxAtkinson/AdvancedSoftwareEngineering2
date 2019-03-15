package main;


import controller.MonitorStateController;
import customExceptions.InvalidProductIdentifierException;
import customExceptions.InvalidProductPriceException;
import model.CustomerQueue;
import model.FileManagerIO;
import model.Server;
import model.ServerList;
import view.AddOrdersView;
import view.MonitorStateGUI;

public class Main {
	//Main method instantiating single instance of FileManagerIO and running GUI

	public static void main(String[] args) throws InvalidProductPriceException, InvalidProductIdentifierException {
		//MVC Set-up
		new MonitorStateController(new MonitorStateGUI());
		
		// Init
		FileManagerIO f = FileManagerIO.getInstance();
		f.readFromProductsFile("Products.csv");
		f.readFromOrderFile("Orders.csv");
		f.readFromNewOrderFile("NewOrders.csv");
		
		new AddOrdersView();
	}
}
