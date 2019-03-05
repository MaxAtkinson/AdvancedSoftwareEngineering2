package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MonitorStateGUI extends JFrame {
	public MonitorStateGUI() {
		JList<String> dummy= new JList<String>();
		JList<String> dummy1= new JList<String>();
		JList<String> dummy2= new JList<String>();
		JList<String> dummy3= new JList<String>();
		JList<String> dummy4= new JList<String>();
		GridBagConstraints c = new GridBagConstraints();

		dummy.setListData(new String[] {"Gordon", "Connor", "Max"});
		dummy1.setListData(new String[] {"Gordon", "Connor", "Max"});
		dummy2.setListData(new String[] {"Gordon", "Connor", "Max"});
		dummy3.setListData(new String[] {"Gordon", "Connor", "Max"});
		dummy4.setListData(new String[] {"Gordon", "Connor", "Max"});
		
		JPanel pane = new JPanel(new GridBagLayout());
		add(pane);
		
		JScrollPane queue = new JScrollPane(dummy);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.weightx = 0.0;
		pane.add(queue, c);
		
		JScrollPane server1 = new JScrollPane(dummy1);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server1, c);

		JScrollPane server2 = new JScrollPane(dummy2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server2, c);

		JScrollPane server3 = new JScrollPane(dummy3);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server3, c);

		JScrollPane server4 = new JScrollPane(dummy4);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.5;
		pane.add(server4, c);
		getContentPane().add(pane, BorderLayout.NORTH);
		setTitle("Monitor Queue");
		setLocationRelativeTo(null);
		setVisible(true);
		setSize(600, 425);
		setResizable(false);
		
		
	}
	
	
	

}
