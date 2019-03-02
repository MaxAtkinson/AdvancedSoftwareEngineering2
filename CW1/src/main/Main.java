package main;


import controller.FileManagerController;
import customExceptions.InvalidProductIdentifierException;
import customExceptions.InvalidProductPriceException;
import model.FileManagerIO;
import view.AddOrdersView;

public class Main {
	//Main method instantiating single instance of FileManagerIO and running GUI
	public static void main(String[] args) throws InvalidProductPriceException, InvalidProductIdentifierException {
		FileManagerIO f = FileManagerIO.getInstance();
		f.readFromProductsFile("Products.csv");
		f.readFromOrderFile("Orders.csv");
		f.readFromNewOrderFile("NewOrders.csv");
		AddOrdersView gui = new AddOrdersView();
		FileManagerController fc = new FileManagerController(gui);
//		System.out.println(f.customerQueue.size());
//		System.out.println(f.customerQueue.toString());
//		for (int i=0; i<=f.customerQueue.size()+1; i++) {
//			System.out.println(f.customerQueue.remove().getCustID());
//		}
	}
}
