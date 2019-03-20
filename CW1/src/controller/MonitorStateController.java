package controller;

import view.MonitorStateGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Qthread;
import model.Server;
import model.ServerList;


public class MonitorStateController {
	private MonitorStateGUI view;
	private ServerList serverList;
	
	public MonitorStateController(MonitorStateGUI view, ServerList serverList){
		this.view = view;
		this.serverList = serverList;
		this.view.addSpeedListener(new SpeedListener());
		this.view.addServer(new AddServerListener());
		this.view.removeServer(new RemoveServerListener());
		this.view.startSim(new StartSimualationListner());
	}
	
	
	public class StartSimualationListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
				Qthread q = new Qthread();
				Thread qThread = new Thread(q);
				qThread.start();
				view.disableStartSim();
		}
	}
	
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
	
	
	
	
	public class RemoveServerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			serverList.removeServer();
		}
	}
	
	// inner class SpeedListener responds when user sets the Speed via the slider
	public class SpeedListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int speed = view.speedSlider.getValue();
			Server.setThreadSleepTime(speed);
			view.sliderLable.setText("Thread Processing Speed: " + Integer.toString(Server.getThreadSleepTime()/1000)+ " Seconds"); 
		}
		
	}

}
