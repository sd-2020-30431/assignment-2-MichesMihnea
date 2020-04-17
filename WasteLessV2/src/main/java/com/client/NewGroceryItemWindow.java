package com.client;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class NewGroceryItemWindow extends JFrame{
	
	private JTextField textField;
	private boolean validInput = false;
	private String errorMessage = "";
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JSpinner spinner_2;
	private JSpinner spinner_3;
	
	public NewGroceryItemWindow() {
		setBounds(new Rectangle(450, 300, 0, 0));
		getContentPane().setLayout(null);
		this.setSize(450, 300);
		
		textField = new JTextField();
		textField.setBounds(83, 37, 117, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		spinner = new JSpinner();
		spinner.setBounds(83, 99, 117, 20);
		getContentPane().add(spinner);
		
		spinner_1 = new JSpinner();
		spinner_1.setBounds(83, 68, 117, 20);
		getContentPane().add(spinner_1);
		
		spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerDateModel(new Date(1584655200000L), null, null, Calendar.DAY_OF_YEAR));
		spinner_2.setBounds(83, 130, 117, 28);
		getContentPane().add(spinner_2);
		
		spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerDateModel(new Date(1584655200000L), null, null, Calendar.DAY_OF_YEAR));
		spinner_3.setBounds(83, 169, 117, 28);
		getContentPane().add(spinner_3);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(235, 40, 46, 14);
		getContentPane().add(lblName);
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(235, 71, 46, 14);
		getContentPane().add(lblQuantity);
		
		JLabel lblCalories = new JLabel("Calories");
		lblCalories.setBounds(235, 102, 46, 14);
		getContentPane().add(lblCalories);
		
		JLabel lblPurchaseDate = new JLabel("Purchase Date");
		lblPurchaseDate.setBounds(235, 133, 70, 14);
		getContentPane().add(lblPurchaseDate);
		
		JLabel lblExpirationDate = new JLabel("Expiration Date");
		lblExpirationDate.setBounds(234, 176, 84, 14);
		getContentPane().add(lblExpirationDate);
		
		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				validInput = validateInput();
				if(!validInput) {
					JOptionPane.showMessageDialog(new JFrame(), "The input is bad! Please check: "+ errorMessage, "Dialog",
					        JOptionPane.ERROR_MESSAGE);
					errorMessage = "";
				}else {
					NewGroceryItemWindow.this.dispatchEvent(new WindowEvent(NewGroceryItemWindow.this, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		btnAddItem.setBounds(175, 215, 89, 23);
		getContentPane().add(btnAddItem);
	}
	
	private boolean validateInput() {
		
		if(this.textField.getText().length() < 1) {
			errorMessage += "name, ";
		}
		
		if(this.textField.getText().length() > 44) {
			errorMessage += "find a shorter name, ";
		}
		
		if((Integer) this.spinner.getValue() <= 0) {
			errorMessage += "quantity, ";
		}
		
		if((Integer) this.spinner.getValue() > 100) {
			errorMessage += "please don't stockpile, ";
		}
		
		if((Integer) this.spinner_1.getValue() <= 0) {
			errorMessage += "calories, ";
		}
		
		if((Integer) this.spinner_1.getValue() > 2000) {
			errorMessage += "eat healthier man, ";
		}
		
		Date purchaseDate = (Date) spinner_2.getValue();
		
		if(purchaseDate.compareTo(new Date(System.currentTimeMillis())) > 0) {
			errorMessage += "purchase date, ";
		}
		
		Date expirationDate = (Date) spinner_3.getValue();
		
		if(expirationDate.compareTo(new Date(System.currentTimeMillis())) < 0) {
			errorMessage += "expiration date, ";
		}
		
		if(errorMessage.length() > 0) {
			errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
			return false;
		}
		
		return true;
	}
	
	public boolean getInputState() {
		return this.validInput;
	}
	
	public void reset() {
		this.textField.setText("");
		this.validInput = false;
		this.errorMessage = "";
	}
	
	public String getItem() {
		return this.textField.getText();
	}
	
	public int getQuantity() {
		return (int) this.spinner.getValue();
	}
	
	public int getCalories() {
		return (int) this.spinner_1.getValue();
	}
	
	public Date getPurchaseDate() {
		return (Date) this.spinner_2.getValue();
	}
	
	public Date getExpirationDate() {
		return (Date) this.spinner_3.getValue();
	}
}
