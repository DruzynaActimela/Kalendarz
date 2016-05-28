package com.actimel.calendar;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import com.actimel.utils.HtmlTemplate;
import com.actimel.utils.Utils;

public class Starter {
	
	public static void main(String[] args) {
		new Starter(args);
	}
	
	public Starter(String[] args) {
		URL res = Starter.class.getClassLoader().getResource(File.separator);
		if(res != null) {
			try {
	            final File apps = new File(res.toURI());
	            for (File app : apps.listFiles()) {
	                System.out.println(app);
	            }
	        } catch (URISyntaxException ex) {
	        	ex.printStackTrace();
	        }
		} else {
			Utils.log("Something went wrong");
		}
	
		HtmlTemplate index = HtmlTemplate.loadFromResource("dashboard.html", false);
		Utils.log("-- render -- ");
		index.putYieldVar("current_username", "test");
		//index.putYieldVar("zmienna", "wartosc zmiennej");
		String renderedTemplate = index.render();
		
		Utils.log(renderedTemplate);
		
		
		
		
		
		try {
			CalendarApp app = new CalendarApp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
