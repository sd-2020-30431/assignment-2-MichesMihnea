package com.client;

import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewNotificationsWindow extends JFrame{
	private JTable table;
	private List <String> messages;
	DefaultTableModel tableModel;
	public ViewNotificationsWindow() {
		getContentPane().setLayout(null);
		this.setSize(450, 300);
		
		String col[] = {"Message"};

		tableModel = new DefaultTableModel(col, 0);
		
		table = new JTable(tableModel);
		table.setBounds(10, 11, 414, 239);
		getContentPane().add(table);
	}
	
	public void display() {
		Iterator <String> it = messages.iterator();
		
		while(it.hasNext()) {
			Object[] row = {it.next()};
			tableModel.addRow(row);
		}
	}
	
	public void setMessages(List <String> messages) {
		this.messages = messages;
	}

}
