package com.client;

import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.data.Item;
import com.data.ItemRepository;
import com.data.Notification;
import com.data.NotificationRepository;
import com.itextpdf.text.DocumentException;

public class Controller extends Observable{
	private MainWindow mw;
	private List <String> messages = new ArrayList <String> ();
	private List <String> names = new ArrayList <String> ();
	private List <Integer> quantities = new ArrayList <Integer> ();
	private List <Integer> calories = new ArrayList <Integer> ();
	private List <Date> purchaseDates = new ArrayList <Date> ();
	private List <Date> expirationDates = new ArrayList <Date> ();
	private List<Integer> itemsToRemove = new ArrayList <Integer> ();
	private Client client;
	
	private ItemRepository itemRep;
	private NotificationRepository notRep;
	private int calorieGoal = 2000;
	
	@SuppressWarnings("deprecation")
	public Controller(MainWindow mw) {
		this.mw = mw;
		client = new Client("127.0.0.1", 5000); 
		this.addObserver(client);
		mw.nglw.btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("IN LISTENER!");
				names = mw.nglw.getNames();
				quantities = mw.nglw.getQuantities();
				calories = mw.nglw.getCalories();
				purchaseDates = mw.nglw.getPurchaseDates();
				expirationDates = mw.nglw.getExpirationDates();
				
				Iterator <String> namesIterator = names.iterator();
				Iterator <Integer> quantitiesIterator = quantities.iterator();
				Iterator <Integer> caloriesIterator = calories.iterator();
				Iterator <Date> purchaseDatesIterator = purchaseDates.iterator();
				Iterator <Date> expirationDatesIterator = expirationDates.iterator();
				
				while(namesIterator.hasNext()) {
					List <Object> newData = new ArrayList <Object> ();

					newData.add(namesIterator.next());
					newData.add(quantitiesIterator.next());
					newData.add(caloriesIterator.next());
					newData.add(purchaseDatesIterator.next());
					newData.add(expirationDatesIterator.next());
					
					try {
						client.addNewItem(newData);
						synchronized(client){
							client.notify();
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
						        JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mw.btnViewGroceries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<List<String>> items = null;
				try {
					items = Controller.this.client.getItems();
				} catch (ClassNotFoundException | IOException e) {
					JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
				}
				names = new ArrayList <String> ();
				quantities = new ArrayList <Integer> ();
				calories = new ArrayList <Integer> ();
				purchaseDates = new ArrayList <Date> ();
				expirationDates = new ArrayList <Date> ();
				
				Iterator <List <String>> it = items.iterator();
				
				while(it.hasNext()) {
					
					Iterator <String> sit = it.next().iterator();
					
					while(sit.hasNext()) {
					names.add(sit.next());
					quantities.add(Integer.parseInt(sit.next()));
					calories.add(Integer.parseInt(sit.next()));
					purchaseDates.add(new Date(Long.parseLong(sit.next())));
					expirationDates.add(new Date(Long.parseLong(sit.next())));
					}
				}
				
				mw.vgw.setNames(names);
				mw.vgw.setQuantities(quantities);
				mw.vgw.setCalories(calories);
				mw.vgw.setPurchaseDates(purchaseDates);
				mw.vgw.setExpirationDates(expirationDates);
				
				mw.vgw.display();
			}
		});
		
		mw.btnViewNotifications.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.wipeNotifications();
					client.generateNotifications();
					messages = client.getNotifications();
				} catch (ClassNotFoundException | IOException e) {
					JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
				}
				
				mw.vnw.setMessages(messages);
				mw.vnw.display();
			}
		});
		
		mw.btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int newGoal = mw.getNewCalorieGoal();
				try {
					client.setCalorieGoal(newGoal);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		mw.btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
				List<String> options =  client.getComboBoxContent();
				List <String> optionsToRemove = new ArrayList <String> ();
				itemsToRemove = new ArrayList <Integer> ();
				mw.dfw = new DonateFoodWindow(options);
				mw.dfw.setVisible(true);
				mw.dfw.addWindowListener(new WindowAdapter(){
	                public void windowClosing(WindowEvent e){
	                	
	                	}
				});
				mw.dfw.btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(mw.dfw.getSelectedIndex() != -1) {
							itemsToRemove.add(mw.dfw.getSelectedIndex());
							optionsToRemove.add(options.get(mw.dfw.getSelectedIndex()));
							options.remove(mw.dfw.getSelectedIndex());
							mw.dfw.setVisible(false);
							mw.dfw = new DonateFoodWindow(options);
							mw.dfw.btnNewButton.addActionListener(this);
							mw.dfw.btnNewButton_1.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									if(Controller.this.itemsToRemove.size() != 0) {
										System.out.println("IN DONATE LISTENER! ");
										if(Controller.this.itemsToRemove.size() != 0) {
											Iterator <Integer> it = itemsToRemove.iterator();
											while(it.hasNext()) {
												Integer currOption = it.next();
												try {
													client.deleteItemAtIndex(currOption);
												} catch (IOException e) {
													JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
													        JOptionPane.ERROR_MESSAGE);
												}
											}
											
											mw.dfw.dispatchEvent(new WindowEvent(mw.dfw, WindowEvent.WINDOW_CLOSING));
										}
									}
								}
							});
							mw.dfw.setVisible(true);
							
							Iterator <String> it = optionsToRemove.iterator();
							while(it.hasNext()) {
								String currOption = it.next();
								mw.dfw.addToTable(currOption);
							}
						}
						
					}
					
				});
				mw.dfw.btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("IN DONATE LISTENER! ");
						if(Controller.this.itemsToRemove.size() != 0) {
							Iterator <Integer> it = itemsToRemove.iterator();
							while(it.hasNext()) {
								Integer currOption = it.next();
								try {
									client.deleteItemAtIndex(currOption);
								} catch (IOException e) {
									JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
									        JOptionPane.ERROR_MESSAGE);
								}
							}
							
							mw.dfw.dispatchEvent(new WindowEvent(mw.dfw, WindowEvent.WINDOW_CLOSING));
						}
					}
				});
				} catch (ClassNotFoundException | IOException e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Error communicating with server!", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		mw.btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.generateReport(mw.getComboBoxIndex());
				} catch (ClassNotFoundException | DocumentException | IOException e) {
					JOptionPane.showMessageDialog(new JFrame(), "Error generating report!", "Dialog",
					        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
	}
	
	private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
	
	public List <Item> getItems() {
		List <Item> items = new ArrayList <Item> ();
		
		items = itemRep.findAll();
		
		return items;
	}
	
	public List <String> getNamesAndQuantities() {
		List <String> itemNames = new ArrayList <String> ();
		Iterator <Item> it = this.getItems().iterator();
		
		while(it.hasNext()) {
			Item currItem = it.next();
			itemNames.add(currItem.getName() + "(" + currItem.getQuantity() + ")");
		}
		
		return itemNames;
	}
	
	public List <Notification> getNotifications(){
		List <Notification> notifications = new ArrayList <Notification> ();
		
		notifications = notRep.findAll();
		
		return notifications;
	}
	
	public void wipe() {
		notRep.deleteAll();
	}
}
