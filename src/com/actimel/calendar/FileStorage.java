package com.actimel.calendar;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.actimel.intfs.StorageIntf;
import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.User;
import com.actimel.utils.Utils;
import com.google.gson.reflect.TypeToken;

public class FileStorage implements StorageIntf {
	
	private File eventsStorageFile;
	private File usersStorageFile;
	private File eventsGroupsStorageFile;
	
	private int highestUserId = 0;
	private int highestEventId = 0;
	private int highestEventGroupId = 0;
	
	private CalendarApp app;
	private HashMap<String, User> cachedUsers = new HashMap<String, User>();
	private HashMap<Integer, CalendarEvent> cachedEvents = new HashMap<Integer, CalendarEvent>();
	private HashMap<Integer, EventGroup> cachedEventGroups = new HashMap<Integer, EventGroup>();
	
	
	
	public FileStorage(CalendarApp appInstance, String eventsPath, String eventsGroupsPath, String usersPath) {
		this.app = appInstance;
		
		eventsStorageFile = new File(eventsPath);
		if(!eventsStorageFile.exists()) {
			Utils.log("Plik " + eventsStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		eventsGroupsStorageFile = new File(eventsGroupsPath);
		if(!eventsGroupsStorageFile.exists()) {
			Utils.log("Plik " + eventsGroupsStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		usersStorageFile = new File(usersPath);
		if(!usersStorageFile.exists()) {
			Utils.log("Plik " + usersStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		
		
		
		preloadUsers();		
	}
	
	public void preloadEvents() {
		Type t = new TypeToken<HashMap<Integer, CalendarEvent>>(){}.getType();
		String json = "{}";
		if(eventsStorageFile.exists()) json = Utils.readFile(eventsStorageFile);
		cachedEvents = app.getGson().fromJson(json, t);
		Utils.log("FileStorage: Events preloaded");
		
		for(Entry<Integer, CalendarEvent> ev : cachedEvents.entrySet()) {
			if(ev.getValue().getId() > highestEventId) highestEventId = ev.getValue().getId();
		}
	}
	
	public int getHighestUserId() {
		return highestUserId;
	}
	
	public int getHighestEventId() {
		return highestEventId;
	}
	
	public int getHighestEventGroupId() {
		return highestEventGroupId;
	}
	
	
	
	public void preloadUsers() {
		Type t = new TypeToken<HashMap<String, User>>(){}.getType();
		String json = "{}";
		if(usersStorageFile.exists()) json = Utils.readFile(usersStorageFile);
		cachedUsers = app.getGson().fromJson(json, t);
		Utils.log("FileStorage: Users preloaded");
		
		for(Entry<String, User> user : cachedUsers.entrySet()) {
			if(user.getValue().getId() > highestUserId) highestUserId = user.getValue().getId();
		}
	}
	
	
	@Override
	public User loadUser(String username) {
		return loadUser(username, false);
	}
	
	public User loadUser(String username, boolean noCache) {
		if(noCache) preloadUsers();	
		return cachedUsers.containsKey(username) ? cachedUsers.get(username) : null;
	}

	@Override
	public void storeUser(User user) {
		cachedUsers.put(user.getName(), user);
	}
	
	public void saveUsers() {
		Type t = new TypeToken<HashMap<String, User>>(){}.getType();
		String json = app.getGson().toJson(cachedUsers, t);
		
		Utils.writeToFile(usersStorageFile, json);		
		Utils.log("FileStorage: Users saved");
	}
	
	@Override
	public CalendarEvent loadEvent(int eventId) {
		return loadEvent(eventId, false);
	}
	
	public CalendarEvent loadEvent(int eventId, boolean noCache) {
		if(noCache) preloadEvents();	
		return cachedEvents.containsKey(eventId) ? cachedEvents.get(eventId) : null;
	}

	@Override
	public void saveEvent(CalendarEvent event) {
		cachedEvents.put(event.getId(), event);
	}
	
	public void saveEvents() {
		Type t = new TypeToken<HashMap<Integer, CalendarEvent>>(){}.getType();
		String json = app.getGson().toJson(cachedEvents, t);
		
		Utils.writeToFile(eventsStorageFile, json);		
		Utils.log("FileStorage: Events saved");
	}
	

	@Override
	public List<CalendarEvent> searchEvents(String query, String searchFieldName) {
		List<CalendarEvent> result = new ArrayList<CalendarEvent>();
		if(query == null) return result;
		
		try {
			Field field = CalendarEvent.class.getDeclaredField(searchFieldName);
			field.setAccessible(true);
			for(Entry<Integer, CalendarEvent> eg : cachedEvents.entrySet()) {
				CalendarEvent group = eg.getValue();
				Object fieldValue = field.get(group);				
				if(query.equals(String.valueOf(fieldValue))) {
					result.add(group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public EventGroup loadEventGroup(int eventGroupId) {
		return loadEventGroup(eventGroupId, false);
	}
	
	public EventGroup loadEventGroup(int eventGroupId, boolean noCache) {
		if(noCache) preloadEventGroups();	
		return cachedEventGroups.containsKey(eventGroupId) ? cachedEventGroups.get(eventGroupId) : null;
	}

	public  void preloadEventGroups() {
		Type t = new TypeToken<HashMap<Integer, EventGroup>>(){}.getType();
		String json = "{}";
		if(eventsGroupsStorageFile.exists()) json = Utils.readFile(eventsGroupsStorageFile);
		cachedEventGroups = app.getGson().fromJson(json, t);
		Utils.log("FileStorage: Event groups preloaded");
		
		for(Entry<Integer, EventGroup> eg : cachedEventGroups.entrySet()) {
			if(eg.getValue().getId() > highestEventGroupId) highestEventGroupId = eg.getValue().getId();
		}
	}

	@Override
	public void saveEventGroup(EventGroup eventGroup) {
		cachedEventGroups.put(eventGroup.getId(), eventGroup);	
	}

	@Override
	public List<EventGroup> searchEventsGroups(String query, String searchFieldName) {
		List<EventGroup> result = new ArrayList<EventGroup>();
		if(query == null) return result;
		
		try {
			Field field = EventGroup.class.getDeclaredField(searchFieldName);
			field.setAccessible(true);
			for(Entry<Integer, EventGroup> eg : cachedEventGroups.entrySet()) {
				EventGroup group = eg.getValue();
				Object fieldValue = field.get(group);				
				if(query.equals(String.valueOf(fieldValue))) {
					result.add(group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void saveEventGroups() {
		Type t = new TypeToken<HashMap<Integer, EventGroup>>(){}.getType();
		String json = app.getGson().toJson(cachedEventGroups, t);
		
		Utils.writeToFile(eventsGroupsStorageFile, json);		
		Utils.log("FileStorage: Event groups saved");
	}
	
	@Override
	public List<CalendarEvent> searchEventsBetween(long timestamp_start, long timestamp_end, int user_id) {
		List<CalendarEvent> result = new ArrayList<CalendarEvent>();
		if(timestamp_start == 0 || timestamp_end == 0) return result;
		
		for(Entry<Integer, CalendarEvent> eg : cachedEvents.entrySet()) {
			CalendarEvent evt = eg.getValue();
			if(evt.getOwnerId() == user_id) {
				if(evt.getStampStart() >= timestamp_start && evt.getStampStart() <= timestamp_end) {
					result.add(evt);
				}
			}
		}	
		
		return result;
	}

}
