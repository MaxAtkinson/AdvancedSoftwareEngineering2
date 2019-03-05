package view;

import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MonitorStateGUI extends JFrame {
	public MonitorStateGUI() {
		JList<String> dummy= new JList<String>();
		dummy.setListData(new String[] {"Gordon", "Connor", "Max"});
		JPanel pane = new JPanel(new GridBagLayout());
		add(pane);
		JScrollPane queue = new JScrollPane(dummy);
		JScrollPane server1 = new JScrollPane(dummy);
		JScrollPane server2 = new JScrollPane(dummy);
		JScrollPane server3 = new JScrollPane(dummy);
		JScrollPane server4 = new JScrollPane(dummy);
		add(queue);
		add(server1);
		add(server2);
		add(server3);
		add(server4);
		
	}
	
	
	

}
