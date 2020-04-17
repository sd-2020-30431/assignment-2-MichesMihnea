package com.client;

//A Java program for a Client 
import java.net.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.itextpdf.text.DocumentException;
import java.io.*; 

public class Client implements Observer
{ 
// initialize socket and input output streams 
private Socket socket            = null; 
protected ObjectInputStream  input   = null; 
protected ObjectOutputStream out     = null; 

// constructor to put ip address and port 
public Client(String address, int port) 
{	
	   // establish a connection 
	   try
	   { 
	       socket = new Socket(address, port); 
	       System.out.println("Connected on port " + socket.getPort());
	       out    = new ObjectOutputStream(socket.getOutputStream()); 
	       input  = new ObjectInputStream(socket.getInputStream());  
	       
	   } 
	   catch(UnknownHostException u) 
	   { 
	       System.out.println(u); 
	   } 
	   catch(IOException i) 
	   { 
	       System.out.println(i); 
	   }
	
	} 
	
	public List <List <String>> getItems() throws IOException, ClassNotFoundException{
		out.writeObject(new String("getItems"));
		@SuppressWarnings("unchecked")
		List <List <String>> items = (List<List<String>>) input.readObject();
	
		return items;
	}
	
	public void addNewItem (List <Object> newData) throws IOException {
		System.out.println("SENDING NEW ITEM");
		out.writeObject(new String("newItem"));
		out.writeObject(newData);
	}
	
	public void wipeNotifications() throws IOException {
		System.out.println("SENDING WIPE REQUEST");
		
		out.writeObject(new String("wipe"));
	}
	
	public void generateNotifications() throws IOException{
		System.out.println("SENDING GENERATE NOTIFICATIONS REQUEST");
		
		out.writeObject("generateNotifications");
	}
	
	public List<String> getNotifications() throws IOException, ClassNotFoundException {
		System.out.println("REQUESTING NOTIFICATIONS");
		
		out.writeObject("getNotifications");
		
		List <String> messages = (List<String>) input.readObject();
		
		return messages;
	}
	
	public void setCalorieGoal(int calorieGoal) throws IOException {
		System.out.println("SENDING NEW CALORIE GOAL");
		
		out.writeObject(new String("setCalorieGoal"));
		out.writeObject(new Integer(calorieGoal));
	}
	
	public List <String> getComboBoxContent() throws IOException, ClassNotFoundException {
		System.out.println("REQUESTING COMBO BOX CONTENT");
		
		out.writeObject(new String("getComboBoxContent"));
		
		List <String> content = (List <String>) input.readObject();
		
		return content;
	}
	
	public void deleteItemAtIndex(int index) throws IOException {
		System.out.println("SENDING DELETE REQUEST FOR INDEX + " + index);
		
		out.writeObject(new String("deleteAtIndex"));
		out.writeObject(new Integer(index));
	}
	
	public void generateReport(int type) throws DocumentException, IOException, ClassNotFoundException {
		System.out.println("STARTING REPORT GENERATION");
		
		System.out.println("REQUESTING OBJECT COUNT");
		out.writeObject(new String("getItemCount"));
		
		Integer itemCount = (Integer) input.readObject();
		
		System.out.println("REQUESTING TOTAL CALORIE COUNT");
		out.writeObject(new String("getCalorieCount"));
		
		Long calorieCount = (Long) input.readObject();
		
		System.out.println("REQUESTING TOTAL EXPIRED ITEMS");
		out.writeObject(new String("getExpiredCount"));
		
		Integer expiredItemCount = (Integer) input.readObject();
		
		System.out.println("REQUESTING TOTAL DAILY CALORIES");
		out.writeObject(new String("getDailyCalories"));
		
		Float dailyCalorieCount = (Float) input.readObject();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		if(type == 0)
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		else cal.set(Calendar.DAY_OF_MONTH, 1);
		
		
		
		System.out.println("REQUESTING TOTAL PURCHASED ITEMS");
		out.writeObject(new String("getPurchasedItems"));
		if(type == 0)
			out.writeObject(new Date(cal.getTimeInMillis() + 86400000));
		else out.writeObject(new Date(cal.getTimeInMillis()));
		
		Integer purchasedItemsCount = (Integer) input.readObject();
		
		ReportGenerator.generateReport(type, itemCount, calorieCount, expiredItemCount, dailyCalorieCount, purchasedItemsCount);
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			wipeNotifications();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			generateNotifications();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 