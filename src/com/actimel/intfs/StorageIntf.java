package com.actimel.intfs;

import java.util.List;

import com.actimel.models.CalendarEvent;
import com.actimel.models.User;

public interface StorageIntf {
	
	public List<CalendarEvent> loadEvents();
	public void saveEvents(List<CalendarEvent> events);
	
	public User loadUser(String username);
	public void storeUser(User user);
}
