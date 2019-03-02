package controller;

import java.awt.event.ActionListener;

import model.FileManagerIO;
import view.AddOrdersView;

public class FileManagerController {
	FileManagerIO f = FileManagerIO.getInstance();
	AddOrdersView g;
	//View2
	
	public FileManagerController(AddOrdersView g) { //View2, v) 
		this.g = g;
		Thread thread = new Thread(f);
		thread.start();
		//this.v = v;
//		g.addStartButtonListener(
//				new addStartButtonController();
//				
//				);
		
	}
//	class addStartButtonController implements ActionListener {
//		g.disableStartButton();
//		Thread thread = new Thread(f);
//		thread.start();
	}
	
//}
