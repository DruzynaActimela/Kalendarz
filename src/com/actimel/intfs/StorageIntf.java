package com.actimel.intfs;

import java.util.List;

import com.actimel.models.CalendarEvent;

public interface StorageIntf {
	
	public List<CalendarEvent> loadEvents();
	public void saveEvents(List<CalendarEvent> events);
}
