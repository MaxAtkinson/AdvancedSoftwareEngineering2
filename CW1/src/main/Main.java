package main;


import controller.FileManagerController;
import controller.MonitorStateController;
import customExceptions.InvalidProductIdentifierException;
import customExceptions.InvalidProductPriceException;
import model.CustomerQueue;
import model.FileManagerIO;
import model.Server;
import view.AddOrdersView;
import view.MonitorStateGUI;

public class Main {
	//Main method instantiating single instance of FileManagerIO and running GUI
	public static void main(String[] args) throws InvalidProductPriceException, InvalidProductIdentifierException {
		FileManagerIO f = FileManagerIO.getInstance();
		f.readFromProductsFile("Products.csv");
		f.readFromOrderFile("Orders.csv");
		f.readFromNewOrderFile("NewOrders.csv");
		
		
		
		//MCV Set-up
		MonitorStateGUI view = new MonitorStateGUI();
		Server model = new Server(1);
		MonitorStateController controler = new MonitorStateController(view, model);
		
		
		AddOrdersView gui = new AddOrdersView();
		
		//MonitorStateGUI gui2 = new MonitorStateGUI();
					
		
//		FileManagerController fc = new FileManagerController(gui);
//		System.out.println(f.customerQueue.size());
//		for (int i=0; i<f.customerQueue.size(); i++) {
//			System.out.println(f.customerQueue.get(i).toString());
//		}
	}
}
