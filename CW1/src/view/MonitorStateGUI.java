package view;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.MonitorStateController.SpeedListener;
import model.CustomerQueue;
import model.Server;
import order.Order;


public class MonitorStateGUI extends JFrame implements Observer {
	
	private CustomerQueue cq;
	private JList<String> queueList;
	private ArrayList<Server> servers;
	private ArrayList<JList<String>> serverDisplays;
	private JButton startSim;
	//Sim speed
	public JSlider speedSlider;
	public JLabel sliderLable;
	
	
	static final int SPEED_MIN = 1;
	static final int SPEED_MAX = 10;
	static final int SPEED_INT = 1;
	
	
	private int numOfServers;
	
	public MonitorStateGUI() {
//		initBtnActions();
		cq = CustomerQueue.getInstance();
		cq.addObserver(this);
		queueList = new JList<String>();
		updateQueueDisplay(cq);
		servers = new ArrayList<>();
		serverDisplays = new ArrayList<>();
		numOfServers = 2; // default, changed by slider later
		
		
		JList<String> serverDisplay1= new JList<String>();
		JList<String> serverDisplay2= new JList<String>();
		JList<String> serverDisplay3= new JList<String>();
		JList<String> serverDisplay4= new JList<String>();
		
		serverDisplay1.setListData(new String[] {"Gordon", "Connor", "Max"});
		serverDisplay2.setListData(new String[] {"Gordon", "Connor", "Max"});
		serverDisplay3.setListData(new String[] {"Gordon", "Connor", "Max"});
		serverDisplay4.setListData(new String[] {"Gordon", "Connor", "Max"});
		
		
		serverDisplays.add(serverDisplay1);
		serverDisplays.add(serverDisplay2);
		serverDisplays.add(serverDisplay3);
		serverDisplays.add(serverDisplay4);
		
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel pane = new JPanel(new GridBagLayout());
		add(pane);
		
		JScrollPane queue = new JScrollPane(queueList);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.weightx = 0.0;
		pane.add(queue, c);
		
		JScrollPane server1 = new JScrollPane(serverDisplay1);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server1, c);

		JScrollPane server2 = new JScrollPane(serverDisplay2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server2, c);

		JScrollPane server3 = new JScrollPane(serverDisplay3);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server3, c);

		JScrollPane server4 = new JScrollPane(serverDisplay4);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server4, c);
		getContentPane().add(pane, BorderLayout.NORTH);
		setTitle("Monitor Queue");
		setLocationRelativeTo(null);
		setVisible(true);
		setSize(800, 425);
		setResizable(false);
		
		
		startSim = new JButton("Start");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 0;
		c.gridy = 9;
		pane.add(startSim, c);
		c.fill = GridBagConstraints.NONE;
		
		startSim.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					startServers();
				
			}
		});
		
		
		speedSlider = new JSlider(JSlider.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_INT);
		speedSlider.setMajorTickSpacing(1);
		speedSlider.setPaintTicks(true);
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( SPEED_MIN), new JLabel("Fast") );
		labelTable.put( new Integer( SPEED_MAX ), new JLabel("Slow") );
		speedSlider.setLabelTable( labelTable );
		speedSlider.setPaintLabels(true);
		
		


		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(speedSlider, c);
		c.fill = GridBagConstraints.NONE;
		
		
	
		sliderLable = new JLabel("Thread Processing Speed: " + Integer.toString(Server.getThreadSleepTime()/1000)+ " Seconds");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 3;
		c.gridy = 3;
		pane.add(sliderLable, c);
		c.fill = GridBagConstraints.NONE;
		
		//startServers();
	}

	private void startServers() {
	for(int x=0; x<numOfServers ; x++) {
		try {
			Server s = new Server(x);
			s.addObserver(this);
			servers.add(s);
			Thread serverThread = new Thread(s);
			serverThread.start();
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	
	//@ Andy - adding change listener to slider - Functionality handled in controller
	public void addSpeedListener(ChangeListener e){
		speedSlider.addChangeListener(e);	
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 == cq) {
			CustomerQueue queue = (CustomerQueue) arg0;
			updateQueueDisplay(queue);
		} else if (arg0.getClass() == Server.class) {
			Server s = (Server) arg0;
 			updateServerDisplay(s);
		}
	}

	private void updateQueueDisplay(CustomerQueue queue) {
		String[] custIDs = queue.getCustomerIDs();
		queueList.setListData(custIDs);
	}
	
	private void updateServerDisplay(Server s) {
		int serverID = servers.indexOf(s);
		String[] displayOrder = s.displayOrder();
		serverDisplays.get(serverID).setListData(displayOrder);
	}
	

}