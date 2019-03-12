package controller;

import view.MonitorStateGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import model.Server;
import model.ServerList;


public class MonitorStateController {
	private MonitorStateGUI view;
	private ServerList serverList;
	
	public MonitorStateController(MonitorStateGUI view){
		this.view = view;
		this.serverList = new ServerList();
		this.view.addSpeedListener(new SpeedListener());
		this.view.addServer(new ActionListener() {
			@Override
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
			
		});
		this.view.removeServer(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverList.removeServer();
			}
			
		});
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
