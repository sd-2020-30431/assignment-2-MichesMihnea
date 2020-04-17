package com.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Rectangle;

public class ViewGroceriesWindow extends JFrame{
	private JTable table;
	DefaultTableModel tableModel;
	private List <String> names;
	private List <Integer> quantities;
	private List <Integer> calories;
	private List <Date> purchaseDates;
	private List <Date> expirationDates;
	
	public ViewGroceriesWindow() {
		this.setSize(450, 300);
		getContentPane().setLayout(null);
		
		String col[] = {"Name", "Quantity", "Calories", "Purchase Date", "Expiration Date"};

		tableModel = new DefaultTableModel(col, 0);
		
		table = new JTable(tableModel);
		table.setBounds(10, 11, 414, 239);
		getContentPane().add(table);
				
	}
	
	public void setNames(List <String> names) {
		this.names = names;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}

	public void setCalories(List<Integer> calories) {
		this.calories = calories;
	}

	public void setPurchaseDates(List<Date> purchaseDates) {
		this.purchaseDates = purchaseDates;
	}

	public void setExpirationDates(List<Date> expirationDates) {
		this.expirationDates = expirationDates;
	}
	
	public void display() {
		Iterator <String> it1 = names.iterator();
		Iterator <Integer> it2 = quantities.iterator();
		Iterator <Integer> it3 = calories.iterator();
		Iterator <Date> it4 = purchaseDates.iterator();
		Iterator <Date> it5 = expirationDates.iterator();
		
		while(it1.hasNext()) {
			Object[] row = {it1.next(), it2.next(), it3.next(), it4.next().toString(), it5.next().toString()};
			tableModel.addRow(row);
		}
	}
	
}
