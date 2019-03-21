package view;

import order.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import customExceptions.InvalidProductIdentifierException;
import customExceptions.InvalidProductPriceException;
import model.*;


public class AddOrdersView extends JFrame {

	/**
	 * FileManagerIO Singleton.
	 */
	private static FileManagerIO f;
	/**
	 * ProductList Singleton.
	 */
	private static ProductsList productsList;
	/**
	 * CustomerQueue Singleton.
	 */
	private static CustomerQueue cq;
	/**
	 * Static string variable that holds the currently selected product in the GUI JTree.
	 */
	private static String currentSetSelection;
	/**
	 * Static Product variable that holds to the currently selected product in the GUI JList.
	 */
	private static Product curentListSelection;
	/**
	 * Basket Object.
	 */
	private static Basket b;
	/**
	 * An Integer value that indicated the priority of an order.
	 */
	private static int priority;


	
	/**
	 * Declaration of button variable.
	 */
	private JButton buttonAdd, buttonRemove, buttonConfirm, buttonQuit, buttonCancel;
	/**
	 * List to hold products currently added to the basket.
	 */
	private JList<Product> orderList;
	/**
	 * JTree used to display the order in a tree like structure
	 */
	private JTree menuTree;
	/**
	 * Declaration of Scroll Panes to which the JList and JTree will be added
	 */
	private JScrollPane menuPane, orderPane;
	/**
	 * Declaration of JLables which will be used to display both the running total and discount of an order.
	 */
	private static JLabel discount, total;
	/**
	 * Declaration of JCheckBox which will be used to determine the priority of an order.
	 */
	private JCheckBox prioritySelection;

	/** 
	 * Constructor
	 */
	public AddOrdersView() {
		f = FileManagerIO.getInstance();
		productsList = ProductsList.getInstance();
		cq = CustomerQueue.getInstance();
		b = new Basket();
		createView();
		initBtnActions();
		setTitle("ASE Coffee Shop");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(600, 425);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	
	/**
	 * Method instantiates the GUI view
	 */
	private void createView() {

		// Creating Main GUI Panel
		JPanel panelMain = new JPanel();
		getContentPane().add(panelMain);

		
		// Creating GUI pane upon which functionality can be added
		JPanel panelForm = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		panelMain.add(panelForm);

	
		 // Here we use "c" and the coordinate system implemented by
		 // GridBagConstriants to specify the location of our buttons and
		 // JScrollPanes ect.

		
		//JTREE
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Products");
		createNodes(root);
		menuTree = new JTree(root);
		menuPane = new JScrollPane(menuTree);

		// enforcing single item selection
		menuTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// adding Tree Selection listener
		menuTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();

				// if nothing is selected 
				if (node == null)
					return;
				
				// retrieve the node that was selected and store string in static varable 
				currentSetSelection = menuTree.getLastSelectedPathComponent().toString();
			}
		});

		//formating
		menuPane.setPreferredSize(new Dimension(200, 250));
		menuPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Menu",
				TitledBorder.LEFT, TitledBorder.TOP));
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 10, 0);
		c.gridheight = 6;
		c.gridx = 0;
		c.gridy = 0;
		panelForm.add(menuPane, c);
		c.insets = new Insets(0, 0, 0, 0);
		
		//JList
		orderList = new JList<Product>();
		orderPane = new JScrollPane(orderList);

		// enforcing single item selection
		orderList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// adding List Selection listener
		orderList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {

				//retrieve the object that was selected
				orderList.getSelectedValue();
				curentListSelection = orderList.getSelectedValue();

			}
		});

		//format
		orderPane.setPreferredSize(new Dimension(300, 200));
		orderPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Order",
				TitledBorder.LEFT, TitledBorder.TOP));
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(0, 15, 0, 0);
		c.gridheight = 3;
		c.gridx = 3;
		c.gridy = 0;
		panelForm.add(orderPane, c);
		c.insets = new Insets(0, 0, 0, 0);

		//DISCOUNT JLABLE formating
		discount = new JLabel("Discounts: ");
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 15, 10, 0);
		panelForm.add(discount, c);
		c.insets = new Insets(0, 0, 0, 0);
		
		//TOTAL JLABLE formating
		total = new JLabel("Total: ");
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.insets = new Insets(0, 15, 10, 0);
		panelForm.add(total, c);
		c.insets = new Insets(0, 0, 0, 0);
		
		//CANCEL BUTTON formating 
		buttonCancel = new JButton("Cancel Order");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 0;
		c.gridy = 9;
		panelForm.add(buttonCancel, c);
		c.fill = GridBagConstraints.NONE;

		// CONFIRM BUTTON formating
		buttonConfirm = new JButton("Confirm Order");
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 3;
		c.gridy = 6;
		panelForm.add(buttonConfirm, c);

		///ADD BUTTON formating
		buttonAdd = new JButton("Add to Order");
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0, 0, 0, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		panelForm.add(buttonAdd, c);
		c.fill = GridBagConstraints.NONE;

		//REMOVE BUTTON Layout
		buttonRemove = new JButton("Remove Item");
		c.gridx = 3;
		c.gridy = 6;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(0, 15, 0, 0);
		panelForm.add(buttonRemove, c);
		c.insets = new Insets(0, 0, 0, 0);
		
		//QUIT BUTTON Format
		buttonQuit = new JButton("   Quit   ");
		c.gridx = 3;
		c.gridy = 9;
		c.insets = new Insets(35, 0, 0, 0);
		c.anchor = GridBagConstraints.LAST_LINE_END;
		panelForm.add(buttonQuit, c);
		
		//Checkbox formatting
		prioritySelection = new JCheckBox("Is this an internet order? ");
		c.gridx = 3;
		c.gridy = 9;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.insets = new Insets(0, 15, 10, 0);
		panelForm.add(prioritySelection, c);

	}
	
	
	/**
	 * Method initialises and assigns actionListners to the GUI buttons
	 */
	private void initBtnActions() {
		
		
		//adding actionListner to CHECK BOX
		prioritySelection.addActionListener(new ActionListener()
				{
					/**
					 * Method is executed when the check box is selected.
					 * The method sets the static variable to the highest priority of one.
					 * 
					 * 
					 * 
					 * @return Method does not return a value.
					 */
					@Override
					public void actionPerformed(ActionEvent e) {
						priority = 1;		
					}
				}); 
		
		
		
		//adding actionListner to CANCEL button
		buttonCancel.addActionListener(new ActionListener() {
			/**
			 * Methods executed when the CANCEL button is pressed
			 * This method removes all items from the basket and clears the JList display.
			 * 
			 * @param Action event: e - button press
			 * @return Method does not return a value.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				b.clearBasket();
				setDiscountAndTotal();
				displayBasket();
				
				JOptionPane.showMessageDialog(null, "Order has been sucessfully cancelled");;
			}
		});
		
		//adding actionListner to CONFIRM ORDER
		buttonConfirm.addActionListener(new ActionListener() {
			
			/**
			 * Methods executed when the CONFIRM ORDER button is pressed
			 * Method confirms the order and clears the JTree.
			 * 
			 * @param Action event: e - button press
			 * @return Method does not return a value.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (b.getProducts().size()!=0) {
					cq.addCustomer(priority,b.getProducts());
					JOptionPane.showMessageDialog(null, "Order Confirmed");

					setDiscountAndTotal();
					b.clearBasket();
					prioritySelection.setSelected(false);
					displayBasket();
					priority=0;
				}
			}
		});
		
		//adding actionListner to ADD ORDER 
		buttonAdd.addActionListener(new ActionListener() {
			/**
			 * Methods executed when the ADD ORDER button is pressed
			 * This method adds the selected JTree items to the basket.
			 * 
			 * @param Action event: e - button press
			 * @return Method does not return a value.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//If leaf node not selected, display message and return
				if(currentSetSelection == null){
					JOptionPane.showMessageDialog(null, "Please select a product to add to the basket");
				}
				
				else if (currentSetSelection.contains("Products") || currentSetSelection.contains("Food") || currentSetSelection.contains("Drink")|| currentSetSelection.contains("Memorabilia"))  {
					JOptionPane.showMessageDialog(null, "Please select a product to add to the basket");
					
					return;
				} 
				
				else{
				for (Product p : productsList.getProducts()) {
					if (p.getName().equals(currentSetSelection)) {
						b.addProduct(p);
					}
				}
				setDiscountAndTotal();
				displayBasket();
			}}
		});
		
		//adding actionListner to REMOVE BUTTON
		buttonRemove.addActionListener(new ActionListener() {
			/**
			 * Methods executed when the REMOVE button is pressed
			 * This method remove the selected item from the Basket.
			 * 
			 * @param Action event: e - button press
			 * @return Method does not return a value.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				if (curentListSelection == null) {
					JOptionPane.showMessageDialog(null, "Please Select an Item to Remove");
					return;
				}

				b.removeProduct(curentListSelection);
				setDiscountAndTotal();
				displayBasket();
				
			}
		});
		
		// adding actionListner to QUIT button
		buttonQuit.addActionListener(new ActionListener() {
			/**
			 * Methods executed when the QUIT button is pressed
			 * Writes the report to a CSV file and terminates the program
			 * 
			 * @param Action event: e - button press
			 * @return Method does not return a value.
			 */
			@Override
			public void actionPerformed(ActionEvent e) throws NullPointerException {
				try {
					f.writeReport("Report.txt");
					f.dumpLogs();
					System.exit(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
	}
	
	/**
	 * Method to populated JTree with products. 
	 * 
	 * 
	 * @param root- Previously defined root node labelled "Products"
	 * @return Method does not return a value.
	 * */
	private void createNodes(DefaultMutableTreeNode root) {
		Set<Product> products = productsList.getProducts();
		
		DefaultMutableTreeNode food = new DefaultMutableTreeNode("Food");
		DefaultMutableTreeNode drink = new DefaultMutableTreeNode("Drink");
		DefaultMutableTreeNode mem = new DefaultMutableTreeNode("Memorabilia");
		root.add(food);
		root.add(drink);
		root.add(mem);

		for (Product p : products) {
			DefaultMutableTreeNode menuItem = new DefaultMutableTreeNode(p.getName());
			if (p.getId().contains("FOOD")) {
				food.add(menuItem);
			} else if (p.getId().contains("BEV")){
				drink.add(menuItem);
			} else if (p.getId().contains("MEM")){
				mem.add(menuItem);
			}
		}
	}
	
	
	/**
	 * Method sets the values on the "Total" and "Discount JLables" 
	 * 
	 * @return Method does not return a value.
	 */
	private void setDiscountAndTotal() {
		ArrayList<Product> pl = b.getProducts();
		total.setText("Total: £" + roundTwoDP(Basket.calculateDiscountedTotal(pl)));
		discount.setText("Discount: -£" + roundTwoDP(Basket.calculateTotalPrice(pl)-Basket.calculateDiscountedTotal(pl)));
	}
	

	/**
	 * Method displays products in basket JList
	 * 
	 * 
	 * @return Method does not return a value.
	 */
	private void displayBasket() {
		Product[] array = b.getProducts().toArray(new Product[b.getProducts().size()]);
		orderList.setListData(array);
	}
	
	
	/**
	 * Method rounds floating point values to two decimal places
	 * 
	 * @param d - any float value
	 * @return float rounded to two decimal places 
	 */
	private static float roundTwoDP(float d) { 
		DecimalFormat twoDForm = new DecimalFormat("#.##"); 
		return Float.valueOf(twoDForm.format(d)); }
}