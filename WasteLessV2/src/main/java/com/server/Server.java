package com.server;

//A Java program for a Server 
import java.net.*;

import java.util.ArrayList;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.data.Item;
import com.data.ItemRepository;
import com.data.Notification;
import com.data.NotificationRepository;

import java.io.*; 

@SpringBootApplication(scanBasePackages= "com")
@EntityScan("com.data")
@EnableJpaRepositories("com.data")

public class Server implements CommandLineRunner
{ 
//initialize socket and input stream 
private Socket          socket   = null; 
private ServerSocket    server   = null; 
private ObjectInputStream in       =  null; 
private ObjectOutputStream out = null;
private int port = 5000;
private boolean connected = false;
private int calorieGoal = 2000;
@Autowired
NotificationRepository notRep;

@Autowired
ItemRepository itemRep;

// constructor with port

private void handleGetItems() throws IOException{
	List <List <String>> details = new ArrayList <List <String>> ();
	List <Item> items = itemRep.findAll();
	
	Iterator <Item> it = items.iterator();
	
	while(it.hasNext()) {
		List <String> newList = new ArrayList <String> ();
		Item currentItem = it.next();
		newList.add(currentItem.getName());
		newList.add(Integer.toString(currentItem.getQuantity()));
		newList.add(Integer.toString(currentItem.getCalories()));
		newList.add(Long.toString(currentItem.getPurchase().getTime()));
		newList.add(Long.toString(currentItem.getExpiration().getTime()));
		details.add(newList);
	}

	out.writeObject(details);
}

private void handleGetNotifications() throws IOException {
	List <String> messages = new ArrayList <String> ();
	List <Notification> notifications = notRep.findAll();
	Iterator <Notification> it = notifications.iterator();
	
	while(it.hasNext()) {
		messages.add(it.next().getMessage());
	}
	
	out.writeObject(messages);
}

private void handleNewItems() throws IOException, ClassNotFoundException {
	List <Object> newData = (List<Object>) in.readObject();
	
	Iterator <Object> it = newData.iterator();
	
	while(it.hasNext()) {
		String name = (String) it.next();
		int quantity = (int) it.next();
		int calories = (int) it.next();
		java.util.Date udate = (java.util.Date) it.next();
		java.sql.Date purchaseDate = new java.sql.Date(udate.getTime());
		udate = (java.util.Date) it.next();
		java.sql.Date expirationDate = new java.sql.Date(udate.getTime());
		
		Item newItem = new Item();
		newItem.setName(name);
		newItem.setQuantity(quantity);
		newItem.setCalories(calories);
		newItem.setPurchase(purchaseDate);
		newItem.setExpiration(expirationDate);
		
		itemRep.save(newItem);
	}
}

private void handleGenerateNotifications() {
	
	List <Item> items = itemRep.findAll();
	
	Iterator <Item> it = items.iterator();
	float totalDailyCalories = 0;
	
	while(it.hasNext()) {
		Item currItem = it.next();
		if(currItem.getExpiration().compareTo(new Date(System.currentTimeMillis() + 86400000 * 2)) < 0 && currItem.getExpiration().compareTo(new Date(System.currentTimeMillis())) > 0) {
			Notification newNotification = new Notification();
			newNotification.setMessage("Item " + currItem.getName() + " is about to expire!");
			Notification newNotification2 = new Notification();
			newNotification2.setMessage("Please consider donating " + currItem.getName());
			notRep.save(newNotification);
			notRep.save(newNotification2);
		}
		else if(currItem.getExpiration().compareTo(new Date(System.currentTimeMillis())) < 0){
			Notification newNotification = new Notification();
			newNotification.setMessage("Item " + currItem.getName() + " has expired!");
			notRep.save(newNotification);
		}
		else totalDailyCalories += this.getIdealBurndownRatio(currItem);
	}
	
	if(this.calorieGoal < totalDailyCalories) {
		Notification newNotification = new Notification();
		newNotification.setMessage("There is a risk of having waste! Excess daily calories: " + (totalDailyCalories - this.calorieGoal));
		notRep.save(newNotification);
	}
	
}

private float getIdealBurndownRatio(Item item) {
	
	long diffInMillies = Math.abs(item.getExpiration().getTime() - (new Date(System.currentTimeMillis())).getTime());
    int daysUntilExpiration = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    
    return ((float) item.getCalories() / (float)(daysUntilExpiration + 1)) * item.getQuantity();
}

private void handleSetCalorieGoal() throws ClassNotFoundException, IOException {
	Integer newCalorieGoal = (Integer) in.readObject();
	this.calorieGoal = newCalorieGoal;
}

private void handleGetComboBoxContent() throws IOException {
	
	List <Item> items = itemRep.findAll();
	List <String> content = new ArrayList <String> ();
	Iterator <Item> it = items.iterator();
	
	while(it.hasNext()) {
		Item currentItem = it.next();
		
		content.add(currentItem.getName() + " (" + currentItem.getQuantity() + ")");
	}
	
	out.writeObject(content);
}

private void handleDeleteAtIndex() throws IOException, ClassNotFoundException {
	Integer index = (Integer) in.readObject();
	
	System.out.println("DELETING INDEX + " + index);
	
	List <Item> items = itemRep.findAll();
	Item itemToDelete = items.get(index);
	
	itemRep.delete(itemToDelete);
	
	System.out.println("DELETION SUCCESS");
}

public static void main(String[] args) {
	SpringApplicationBuilder builder = new SpringApplicationBuilder(Server.class);
	builder.headless(false);
	ConfigurableApplicationContext context = builder.run(args);
}

public void run(String... args) throws Exception 
{       
	 try
	   { 
	       server = new ServerSocket(port); 

	       System.out.println("Server started"); 

	       System.out.println("Waiting for a client ..."); 

	       socket = server.accept();
	 
	       System.out.println("Client accepted"); 

	       out = new ObjectOutputStream(socket.getOutputStream());
	       in = new ObjectInputStream(socket.getInputStream()); 
	       
	       
	       connected = true;

	       // close connection 
	      // socket.close(); 
	      // in.close(); 
	   } 
	   catch(IOException i) 
	   { 
	       System.out.println(i); 
	   } 
	 
	String line = ""; 
	
	while(true) {
	
	    while (!line.equals("Over")) 
	    { 
	    	
	        try
	        { 
	            line = (String) in.readObject(); 
	            System.out.println("RECEIVED: " + line);
	            if(line.equals("getItems")) {
	            	System.out.println("RECEIVED!");
	         	   handleGetItems();
	         
	            }
	            else if(line.equals("newItem")) {
	            	handleNewItems();
	            }
	            else if(line.equals("wipe")) {
	            	System.out.println("RECEIVED WIPE REQUEST");
	            	notRep.deleteAll();
	            }
	            else if(line.equals("getNotifications")) {
	            	System.out.println("RECEIVED NOTIFICATIONS REQUEST!");
	            	handleGetNotifications();
	            }
	            else if(line.equals("generateNotifications")) {
	            	handleGenerateNotifications();
	            }
	            else if(line.equals("setCalorieGoal")) {
	            	handleSetCalorieGoal();
	            }
	            else if(line.equals("getComboBoxContent")) {
	            	handleGetComboBoxContent();
	            }
	            else if(line.equals("deleteAtIndex")) {
	            	handleDeleteAtIndex();
	            }
	            else if(line.equals("getItemCount")) {
	            	handleGetItemCount();
	            }
	            else if(line.equals("getCalorieCount")) {
	            	handleGetCalorieCount();
	            }
	            else if(line.equals("getExpiredCount")) {
	            	handleGetExpiredCount();
	            }
	            else if(line.equals("getDailyCalories")) {
	            	handleGetDailyCalories();
	            }
	            else if(line.equals("getPurchasedItems")) {
	            	handleGetPurchasedItems();
	            }
	
	        } 
	        catch(IOException i) 
	        { 
	            //socket.close();
	            //in.close();
	            //return;
	        	socket = server.accept(); 
	        	in = new ObjectInputStream(socket.getInputStream()); 
	     	       
	     	       out = new ObjectOutputStream(socket.getOutputStream());
	        } 
	    } 
	}
   // System.out.println("Closing connection"); 
}

private void handleGetItemCount() throws IOException {
	List <Item> items = itemRep.findAll();
	Iterator <Item> it = items.iterator();
	
	int itemCount = 0;
	
	while(it.hasNext()) {
		itemCount += it.next().getQuantity();
	}
	
	System.out.println("SENDING ITEM COUNT");
	out.writeObject(new Integer(itemCount));
	
}

private void handleGetCalorieCount() throws IOException {
	List <Item> items = itemRep.findAll();
	Iterator <Item> it = items.iterator();
	
	long calorieCount = 0;
	
	while(it.hasNext()) {
		calorieCount += it.next().getCalories();
	}
	
	System.out.println("SENDING CALORIE COUNT");
	out.writeObject(new Long(calorieCount));
}

private void handleGetExpiredCount() throws IOException {
	List <Item> items = itemRep.findAll();
	Iterator <Item> it = items.iterator();
	
	int expiredCount = 0;
	
	while(it.hasNext()) {
		
		Item currItem = it.next();
		
		if(currItem.getExpiration().compareTo(new Date(System.currentTimeMillis())) < 0) {
			expiredCount += currItem.getQuantity();
		}
	}
	
	System.out.println("SENDING EXPIRED COUNT");
	out.writeObject(new Integer(expiredCount));
}

private void handleGetPurchasedItems() throws ClassNotFoundException, IOException {
	java.util.Date firstDay = (java.util.Date) in.readObject();
	
	List <Item> items = itemRep.findAll();
	Iterator <Item> it = items.iterator();
	
	int purchasedCount = 0;
	
	while(it.hasNext()) {
		
		Item currItem = it.next();
		
		if(currItem.getPurchase().compareTo(convertUtilToSql(firstDay)) > 0) {
			purchasedCount ++;
		}
	}
	
	System.out.println("SENDING PURCHASED COUNT");
	out.writeObject(new Integer(purchasedCount));
}

private void handleGetDailyCalories() throws IOException {
	
	List <Item> items = itemRep.findAll();
	Iterator <Item> it = items.iterator();
	
	int calorieCount = 0;
	
	while(it.hasNext()) {
		
		Item currItem = it.next();
		
		calorieCount += this.getIdealBurndownRatio(currItem);
	}
	
	System.out.println("SENDING DAILY CALORIE COUNT");
	out.writeObject(new Float(calorieCount));	
}

private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
    java.sql.Date sDate = new java.sql.Date(uDate.getTime());
    return sDate;
}


}