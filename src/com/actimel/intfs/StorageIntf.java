package com.actimel.intfs;

import java.util.List;

import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.User;

public interface StorageIntf {
	
	public CalendarEvent loadEvent(int eventId);
	public void saveEvent(CalendarEvent event);
	public List<CalendarEvent> searchEvents(String query, String searchFieldName);
	
	public EventGroup loadEventGroup(int eventGroupId);
	public void saveEventGroup(EventGroup eventGroup);
	
	public List<EventGroup> searchEventsGroups(String query, String searchFieldName);
	
	
	public User loadUser(String username);
	public void storeUser(User user);
}
