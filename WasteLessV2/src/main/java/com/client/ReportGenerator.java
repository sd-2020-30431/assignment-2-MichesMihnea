package com.client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportGenerator {
	public static void generateReport(int type, Integer itemCount, Long calorieCount, Integer expiredItemCount, Float dailyCalorieCount, Integer purchasedItemsCount) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
		 
		document.open();
		Paragraph title = new Paragraph();
		
		switch(type) {
		case 0:
			title.add("Weekly WasteLess Report");
			break;
		
		case 1:
			title.add("Monthly WasteLess Report");
			break;
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		
		Paragraph dates = new Paragraph();

		
		switch(type) {
		
			case 0:
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				dates.add(formatter.format(new Date(cal.getTimeInMillis() + 86400000)));
				break;
		
			case 1:
				cal.set(Calendar.DAY_OF_MONTH, 1);
				dates.add(formatter.format(new Date(cal.getTimeInMillis())));
				break;
		}
		
		Paragraph message = new Paragraph();
        message.setAlignment(Element.ALIGN_CENTER);
		
		switch(type) {
			case 0:
				message.add("Current week: ");
				break;
		
			case 1:
				message.add("Current month: ");
				break;
	    }
		
		dates.add(" - " + formatter.format(new Date(System.currentTimeMillis())));
		
		title.setAlignment(Element.ALIGN_CENTER);
        dates.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph message2 = new Paragraph();
        message2.add("Total count of grocery items: " + itemCount);
        message2.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message3 = new Paragraph();
        message3.add("Total calorie count of owned items: " + calorieCount);
        message3.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message4 = new Paragraph();
        message4.add("Total number of expired items: " + expiredItemCount);
        message4.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message5 = new Paragraph();
        message5.add("Total burned calories per day: " + df.format(dailyCalorieCount));
        message5.setAlignment(Element.ALIGN_LEFT);
        
        Paragraph message6 = new Paragraph();
        message6.add("Total purchased items: " + purchasedItemsCount);
        message6.setAlignment(Element.ALIGN_LEFT);
        
        
        
        document.add(title);
        document.add(Chunk.NEWLINE);
        document.add(message);
        document.add(Chunk.NEWLINE);
        document.add(dates);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(message2);
        document.add(message3);
        document.add(message4);
        document.add(message5);
        document.add(message6);
		document.close();
	}
}
