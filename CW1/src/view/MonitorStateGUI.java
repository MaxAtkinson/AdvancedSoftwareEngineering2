package view;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
import model.FileManagerIO;
import model.Server;
import order.Order;


public class MonitorStateGUI extends JFrame implements Observer {
	
	/**
	 * CustomerQueue Singleton.
	 */
	private CustomerQueue cq;
	/**
	 * JList of Strings to display the customer queue. 
	 */
	private JList<String> queueList;
	/**
	 * ArrayList of JLists to display the order being processed by the server.
	 */
	private ArrayList<JList<String>> serverDisplays;
	/**
	 * Declaration of button variable.
	 */
	public JButton addServerBtn;
	/**
	 * Declaration of button variable.
	 */
	public JButton removeServerBtn;
	/**
	 * Declaration of button variable.
	 */
	public JButton startSimulation;
	/**
	 * Declaration of slider  variable.
	 */
	public JSlider speedSlider;
	/**
	 * Declaration of JLabe for slider display.
	 */
	public JLabel sliderLable;

	/**
	 * Static constant corresponding to the minimum JSlider value. 
	 */
	private static final int SPEED_MIN = 1;
	/**
	 * Static constant corresponding to the maximum JSlider value.
	 */
	private static final int SPEED_MAX = 10;
	/**
	 * Static constant corresponding to the value of the JSlider inverval.
	 */
	private static final int SPEED_INT = 1;
	/**
	 * String array for displaying the initial display. 
	 */
	private static final String[] INITIAL_DISPLAY = new String[] {"Till Not In Use"};

	
	/**
	 * Constructor 
	 */
	public MonitorStateGUI() {
		
		//initialisation 
		cq = CustomerQueue.getInstance();
		cq.addObserver(this);
		queueList = new JList<String>();
		updateQueueDisplay(cq);
		serverDisplays = new ArrayList<>();
		
		JList<String> serverDisplay1= new JList<String>();
		JList<String> serverDisplay2= new JList<String>();
		JList<String> serverDisplay3= new JList<String>();
		JList<String> serverDisplay4= new JList<String>();
		
		serverDisplay1.setListData(INITIAL_DISPLAY);
		serverDisplay2.setListData(INITIAL_DISPLAY);
		serverDisplay3.setListData(INITIAL_DISPLAY);
		serverDisplay4.setListData(INITIAL_DISPLAY);

		serverDisplays.add(serverDisplay1);
		serverDisplays.add(serverDisplay2);
		serverDisplays.add(serverDisplay3);
		serverDisplays.add(serverDisplay4);

		GridBagConstraints c = new GridBagConstraints();

		JPanel pane = new JPanel(new GridBagLayout());
		add(pane);

		// QUEUE LIST formating
		JScrollPane queue = new JScrollPane(queueList);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.weightx = 0.0;
		pane.add(queue, c);

		// SERVER ONE formating
		JScrollPane server1 = new JScrollPane(serverDisplay1);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		pane.add(server1, c);

		// SERVER TWO formatting 
		JScrollPane server2 = new JScrollPane(serverDisplay2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		pane.add(server2, c);

		// SERVER THREE formating
		JScrollPane server3 = new JScrollPane(serverDisplay3);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		pane.add(server3, c);

		// SERVER FOUR formating
		JScrollPane server4 = new JScrollPane(serverDisplay4);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1;
		pane.add(server4, c);
		getContentPane().add(pane, BorderLayout.NORTH);
		setTitle("Monitor Queue");
		setLocationRelativeTo(null);
		setVisible(true);
		setSize(800, 425);
		setResizable(false);
		
		// ADD SERVER button formating
		addServerBtn = new JButton("Add Server");
		addServerBtn.setEnabled(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 1;
		c.gridy = 9;
		pane.add(addServerBtn, c);
		c.fill = GridBagConstraints.NONE;
		
		// REMOVE SERVER button formating
		removeServerBtn = new JButton("Remove Server");
		removeServerBtn.setEnabled(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 2;
		c.gridy = 9;
		pane.add(removeServerBtn, c);
		c.fill = GridBagConstraints.NONE;
		
		// START SUMULATION button formating
		startSimulation = new JButton("Start Sumulation");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 3;
		c.gridy = 9;
		pane.add(startSimulation, c);
		c.fill = GridBagConstraints.NONE;

		// SPEED SLIDER JSlider formating
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


		// JLABLE formating
		sliderLable = new JLabel("Speed per item: " + Integer.toString(Server.getThreadSleepTime()/1000)+ " seconds");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 3;
		c.gridy = 3;
		pane.add(sliderLable, c);
		c.fill = GridBagConstraints.NONE;
	}
	
	
	
	/**
	 * Method enables the both ADD and REMOVE server buttons on the GUI.
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	public void enableServerBtns() {
		addServerBtn.setEnabled(true);
		removeServerBtn.setEnabled(true);
	}
	
	/**
	 * Method adds actionListner to the ADD SERVER button.
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	public void addServer(ActionListener e) {
		addServerBtn.addActionListener(e);
	}
	
	/**
	 * Method adds actionListner to the START SUMULATION button.
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	public void startSim(ActionListener e) {
		startSimulation.addActionListener(e);
	}
	
	/**
	 * Method adds actionListner to the REMOVE SERVER button.
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	public void removeServer(ActionListener e) {
		removeServerBtn.addActionListener(e);
	}
	
	/**
	 * Method disables the the START SIMULATION button.
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	public void disableStartSim() {
		startSimulation.setEnabled(false);
	}

	/**
	 * Method adds changeListner to the SPEED SLIDER.
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	public void addSpeedListener(ChangeListener e){
		speedSlider.addChangeListener(e);	
	}

	/**
	 * Method updates the the view.
	 * 
	 * @param sender - Observable object .
	 * @param data - Object .
	 * @return Method does not return a value.
	 */
	@Override
	public void update(Observable sender, Object data) {
		if (sender == cq) {
			if (cq.isFinished()) {
				try {
					FileManagerIO.getInstance().writeReport("Report.txt");
					FileManagerIO.getInstance().dumpLogs();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
			updateQueueDisplay(cq);
		} else if (sender.getClass() == Server.class) {
			Server s = (Server) sender;
			updateServerDisplay(s);
		}
	}

	/**
	 * Method update the queue display on the GUI.
	 * 
	 * @param takes in the CustomerQueue object.
	 * @return Method does not return a value.
	 */
	private void updateQueueDisplay(CustomerQueue queue) {
		String[] custIDs = queue.getCustomerIDs();
		queueList.setListData(custIDs);
	}
	
	/**
	 * Method updates the GUI server displays. 
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	private void updateServerDisplay(Server s) {
		String[] display = s.displayOrder();
		if (!s.isActive()) display = INITIAL_DISPLAY;
		serverDisplays.get(s.getId()).setListData(display);
	}
}