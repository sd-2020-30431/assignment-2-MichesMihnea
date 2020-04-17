package com.client;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Rectangle;
import javax.swing.JComboBox;

public class MainWindow extends JFrame{
	private JTextField textField;
	public NewGroceryListWindow nglw;
	public ViewGroceriesWindow vgw;
	public ViewNotificationsWindow vnw;
	public DonateFoodWindow dfw;
	public JButton btnViewGroceries;
	public JButton btnViewNotifications;
	public JButton btnNewButton_2;
	public JButton btnNewButton_1;
	public JButton btnGenerateReport;
	private JComboBox comboBox;
	
	public MainWindow() {
		setBounds(new Rectangle(450, 300, 0, 0));
		this.setSize(450, 435);
		
		getContentPane().setLayout(null);
		
		nglw = new NewGroceryListWindow();
		vgw = new ViewGroceriesWindow();
		vnw = new ViewNotificationsWindow();
		
		JButton btnNewGroceryList = new JButton("New Grocery List");
		btnNewGroceryList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				nglw.setVisible(true);
				nglw.addWindowListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                }
	            });
			}
		});
		btnNewGroceryList.setBounds(158, 32, 143, 23);
		getContentPane().add(btnNewGroceryList);
		
		btnViewGroceries = new JButton("View Groceries");
		btnViewGroceries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				vgw.setVisible(true);
				vgw.addWindowListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                	vgw = new ViewGroceriesWindow();
	                }
	            });
			}
		});
		btnViewGroceries.setBounds(158, 67, 143, 25);
		getContentPane().add(btnViewGroceries);
		
		btnViewNotifications = new JButton("View Notifications");
		btnViewNotifications.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				vnw.setVisible(true);
				vnw.addWindowListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                	vnw = new ViewNotificationsWindow();
	                }
	            });
			}
		});
		btnViewNotifications.setBounds(158, 103, 143, 23);
		getContentPane().add(btnViewNotifications);
		
		btnNewButton_1 = new JButton("Donate Food");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnNewButton_1.setBounds(158, 141, 143, 23);
		getContentPane().add(btnNewButton_1);
		
		JLabel lblCalorieGoal = new JLabel("Calorie Goal:");
		lblCalorieGoal.setBounds(190, 176, 85, 16);
		getContentPane().add(lblCalorieGoal);
		
		textField = new JTextField("2000");
		textField.setBounds(158, 204, 67, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		btnNewButton_2 = new JButton("Change");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_2.setBounds(237, 201, 77, 26);
		getContentPane().add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("New label");
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("NewLogo.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lblNewLabel = new JLabel(new ImageIcon(myPicture));
		lblNewLabel.setBounds(69, 236, 310, 97);
		getContentPane().add(lblNewLabel);
		
		btnGenerateReport = new JButton("Generate Report");
		btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(new JFrame(), "Report generated!", "Dialog",
				        JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnGenerateReport.setBounds(102, 358, 127, 26);
		getContentPane().add(btnGenerateReport);
		
		comboBox = new JComboBox(new Object[] {"Weekly", "Monthly"});
		comboBox.setBounds(270, 358, 109, 26);
		getContentPane().add(comboBox);
		this.setVisible(true);
	}
	
	public int getNewCalorieGoal() {
		int newGoal = Integer.parseInt(textField.getText());
		
		if(newGoal <= 0 || newGoal > 10000) {
			JOptionPane.showMessageDialog(new JFrame(), "Bad value for the calorie goal!", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		
		return newGoal;
	}
	
	public int getComboBoxIndex() {
		return this.comboBox.getSelectedIndex();
	}
}
