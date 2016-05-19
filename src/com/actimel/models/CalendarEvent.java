package com.actimel.models;

import net.fortuna.ical4j.model.component.VEvent;

public class CalendarEvent {
	
	private int id;
	private String name;
	private long stamp_start;
	private long stamp_end;
	private boolean allday;
	
	
	public CalendarEvent(int id, String name, long stamp_start, long stamp_end, boolean allday) {
		this.id = id;
		this.name = name;
		this.stamp_start = stamp_start;
		this.stamp_end = stamp_end;
		this.allday = allday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStampStart() {
		return stamp_start;
	}

	public void setStampStart(long stamp_start) {
		this.stamp_start = stamp_start;
	}

	public long getStampEnd() {
		return stamp_end;
	}

	public void setStampEnd(long stamp_end) {
		this.stamp_end = stamp_end;
	}

	public boolean isAllDay() {
		return allday;
	}

	public void setAllDay(boolean allday) {
		this.allday = allday;
	}
	
	
	
	
}

/*
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
		VEvent e = new VEvent();
		
 * 
 * 
 * 
 * java.util.Calendar cal = java.util.Calendar.getInstance();
cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
cal.set(java.util.Calendar.DAY_OF_MONTH, 25);

VEvent christmas = new VEvent(new Date(cal.getTime()), "Christmas Day");
// initialise as an all-day event..
christmas.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
            
Output:

BEGIN:VEVENT
DTSTAMP:20050222T044240Z
DTSTART;VALUE=DATE:20051225
SUMMARY:Christmas Day
END:VEVENT
 */
