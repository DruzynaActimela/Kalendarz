package com.actimel.models;

import com.google.gson.annotations.SerializedName;

public class CalendarEvent {
	
	@SerializedName("_id")
	private int id;
	
	private String title;
	private long start;
	private long end;
	private boolean allday;
	private boolean is_public;
	private int owner_id;
	private int parent_group_id;
	
	public CalendarEvent(int id, String name, long stamp_start, long stamp_end, boolean allday, boolean is_public) {
		this.id = id;
		this.title = name;
		this.start = stamp_start;
		this.end = stamp_end;
		this.allday = allday;
		this.is_public = is_public;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public long getStampStart() {
		return start;
	}

	public void setStampStart(long stamp_start) {
		this.start = stamp_start;
	}

	public long getStampEnd() {
		return end;
	}

	public void setStampEnd(long stamp_end) {
		this.end = stamp_end;
	}

	public boolean isAllDay() {
		return allday;
	}

	public void setAllDay(boolean allday) {
		this.allday = allday;
	}
	
	public boolean isPublic() {
		return is_public;
	}
	
	public void setPublic(boolean p) {
		this.is_public = p;
	}
	
	public void setOwnerId(int id) {
		owner_id = id;
	}
	
	public int getOwnerId() {
		return owner_id;
	}
	
	public void setParentGroupId(int id) {
		parent_group_id = id;
	}
	
	public int getParentGroupId() {
		return parent_group_id;
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
