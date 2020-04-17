package com.client;

import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DonateFoodWindow extends JFrame{
	
	private JTable table;
	private JComboBox <String> comboBox;
	DefaultTableModel tableModel;
	private List <String> toDelete = new ArrayList <String> ();
	public JButton btnNewButton;
	public JButton btnNewButton_1;
	
	public DonateFoodWindow(List <String> options) {
		setBounds(new Rectangle(450, 300, 0, 0));
		getContentPane().setLayout(null);
		this.setSize(450, 350);
		
		String col[] = {"Item", "Quantity"};
		
		tableModel = new DefaultTableModel(col, 0);
		
		table = new JTable(tableModel);
		table.setBounds(10, 11, 414, 239);
		getContentPane().add(table);
		
		comboBox = new JComboBox(options.toArray());
		comboBox.setBounds(10, 272, 147, 26);
		getContentPane().add(comboBox);
		
		btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnNewButton.setBounds(167, 272, 89, 26);
		getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("Donate");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tableModel.getRowCount() == 0) {
					JOptionPane.showMessageDialog(new JFrame(), "No items to donate!", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBounds(309, 271, 89, 27);
		getContentPane().add(btnNewButton_1);
	}
	
	public void addToTable(String name) {
		Object[] objs = {name};

		tableModel.addRow(objs);
	}
	
	public int getSelectedIndex() {
		return this.comboBox.getSelectedIndex();
	}
	
}
