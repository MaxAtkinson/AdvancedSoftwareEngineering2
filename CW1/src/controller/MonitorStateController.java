package controller;

import view.MonitorStateGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Qthread;
import model.Server;
import model.ServerList;


/**
 * MVC Controller for controlling the addition and removal of servers,
 * altering the processing speed, and adding observers to servers for
 * the MonitorStateGUI to observe.
 *
 */
public class MonitorStateController {
	private MonitorStateGUI view;
	private ServerList serverList;
	
	/**
	 * Constructor of the controller
	 * @param view The GUI displaying server processes and the queue
	 * @param serverList the list of servers to be controlled
	 */
	public MonitorStateController(MonitorStateGUI view, ServerList serverList){
		this.view = view;
		this.serverList = serverList;
		this.view.addSpeedListener(new SpeedListener());
		this.view.addServer(new AddServerListener());
		this.view.removeServer(new RemoveServerListener());
		this.view.startSim(new StartSimulationListner());
	}
	
	
	/**
	 * Begin gradually adding customers from the new orders file
	 * by responding to start simulation button press. 
	 * A thread is created to handle this.
	 */
	public class StartSimulationListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				Qthread q = new Qthread();
				Thread qThread = new Thread(q);
				qThread.start();
				view.disableStartSim();
				view.enableServerBtns();
		}
	}
	
	/**
	 * Add a server to the server list and allow the view to
	 * observe the server.
	 * Limit the number of servers to 4.
	 */
	public class AddServerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int numOfServers = serverList.getServers().size();
			if (numOfServers < 4) {
				Server s = new Server(numOfServers);
				serverList.addServer(s);
				s.addObserver(view);
				Thread serverThread = new Thread(s);
				serverThread.start();
			}
		}
	}
	
	/**
	 * Remove a server from the server list.
	 */
	public class RemoveServerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			serverList.removeServer();
		}
	}
	
	/**
	 * SpeedListener responds to user control of the slider on the GUI.
	 * Controls the speed of a single item being processed by a server.
	 */
	public class SpeedListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int speed = view.speedSlider.getValue();
			Server.setThreadSleepTime(speed);
			view.sliderLable.setText("Speed per item: " + Integer.toString(Server.getThreadSleepTime()/1000)+ " seconds"); 
		}
	}
}
