package com.client;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;

public class NewGroceryListWindow extends JFrame{
	private JTable table;
	private NewGroceryItemWindow ngiw;
	public JButton btnFinish;
	DefaultTableModel tableModel;
	private List <String> names = new ArrayList <String> ();
	private List <Integer> quantities = new ArrayList <Integer> ();
	private List <Integer> calories = new ArrayList <Integer> ();
	private List <Date> purchaseDates = new ArrayList <Date> ();
	private List <Date> expirationDates = new ArrayList <Date> ();
	public NewGroceryListWindow() {
		setBounds(new Rectangle(450, 300, 0, 0));
		this.setSize(450, 300);
		getContentPane().setLayout(null);
		
		String col[] = {"Name", "Quantity"};

		tableModel = new DefaultTableModel(col, 0);
		
		table = new JTable(tableModel);
		table.setBounds(10, 11, 414, 185);
		getContentPane().add(table);
		
		JButton btnAddNewItem = new JButton("Add New Item");
		btnAddNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ngiw = new NewGroceryItemWindow();
				ngiw.setVisible(true);
				ngiw.addWindowListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                	if(ngiw.getInputState()) {
		                	names.add(ngiw.getItem());
		                	quantities.add(ngiw.getQuantity());
		                	calories.add(ngiw.getCalories());
		                	purchaseDates.add(ngiw.getPurchaseDate());
		                	expirationDates.add(ngiw.getExpirationDate());
		                    NewGroceryListWindow.this.addToTable(ngiw.getItem(), ngiw.getQuantity());
	                	}
	                }
	            });
			}
		});
		btnAddNewItem.setBounds(69, 227, 101, 23);
		getContentPane().add(btnAddNewItem);
		
		btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						
				for(int i = 0; i < names.size(); i ++)
					tableModel.removeRow(0);
				
				names = new ArrayList <String> ();
				quantities = new ArrayList <Integer> ();
				calories = new ArrayList <Integer> ();
				purchaseDates = new ArrayList <Date> ();
				expirationDates = new ArrayList <Date> ();
				String col[] = {"Name", "Quantity"};

				NewGroceryListWindow.this.setVisible(false);
			}
		});
		btnFinish.setBounds(266, 227, 101, 23);
		getContentPane().add(btnFinish);
	}
	
	private void addToTable(String name, int quantity) {
		Object[] objs = {name, quantity};

		tableModel.addRow(objs);
	}
	
	public boolean getInputState() {
		return this.names.size() > 0;
	}
	
	public List<String> getNames(){
		return this.names;
	}
	
	public List<Integer> getQuantities(){
		return this.quantities;
	}
	
	public List<Integer> getCalories(){
		return this.calories;
	}
	
	public List<Date> getPurchaseDates(){
		return this.purchaseDates;
	}
	
	public List<Date> getExpirationDates(){
		return this.expirationDates;
	}
}
