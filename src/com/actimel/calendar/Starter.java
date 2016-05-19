package com.actimel.calendar;

import java.io.FileNotFoundException;

public class Starter {
	
	public static void main(String[] args) {
		new Starter(args);
	}
	
	public Starter(String[] args) {
		try {
			CalendarApp app = new CalendarApp();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
