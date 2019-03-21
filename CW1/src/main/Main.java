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

/**
 * Main method for running the programme. Sets up the MVC classes of ServerList, MonitorStateGui,
 * and MonitorStateController.
 *
 */
public class Main {
	//Main method instantiating single instance of FileManagerIO and running GUI
/**
 * Main instantiates the singleton fileManager and reads in the product CSV to create the programmes menu.
 * Finally all previous orders are read in so that the final report will contain all orders and not just 
 * that days.
 * 
 * 
 * @param args
 * @throws InvalidProductPriceException 
 * @throws InvalidProductIdentifierException
 */
	public static void main(String[] args) throws InvalidProductPriceException, InvalidProductIdentifierException {
		//MVC Set-up
		ServerList serversModel = new ServerList();
		MonitorStateGUI monitorView = new MonitorStateGUI();
		new MonitorStateController(monitorView, serversModel);
		
		// Init
		FileManagerIO f = FileManagerIO.getInstance();
		f.readFromProductsFile("Products.csv");
		f.readFromOrderFile("Orders.csv");
		new AddOrdersView();
		
		
	}
}
