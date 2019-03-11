package controller;

import view.MonitorStateGUI;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import model.Server;


public class MonitorStateController {
	
	private MonitorStateGUI view;
	private Server model;
	
	
	public MonitorStateController(MonitorStateGUI view, Server model){
		
		this.view = view;
		this.model = model;
		view.addSpeedListener(new SpeedListener());
		
	}
	
	
	// inner class SpeedListenr responds when user sets the Speed via the slider
		public class SpeedListener implements ChangeListener {
			

			@Override
			public void stateChanged(ChangeEvent e) {
			int speed = view.speedSlider.getValue();
			Server.setThreadSleepTime(speed);
			view.sliderLable.setText("Thread Processing Speed: " + Integer.toString(Server.getThreadSleepTime()/1000)+ " Seconds"); 
			System.out.println(speed);		
		
				
			}
			
			
			
			
		}


}
