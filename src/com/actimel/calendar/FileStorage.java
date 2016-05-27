package com.actimel.calendar;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.actimel.intfs.StorageIntf;
import com.actimel.models.CalendarEvent;
import com.actimel.models.User;
import com.actimel.utils.Utils;
import com.google.gson.reflect.TypeToken;

public class FileStorage implements StorageIntf {
	
	private File eventsStorageFile;
	private File usersStorageFile;
	
	private int highestUserId = 0;
	
	private CalendarApp app;
	private HashMap<String, User> cachedUsers = new HashMap<String, User>();
	
	public FileStorage(CalendarApp appInstance, String eventsPath, String usersPath) {
		this.app = appInstance;
		
		eventsStorageFile = new File(eventsPath);
		if(!eventsStorageFile.exists()) {
			Utils.log("Plik " + eventsStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		usersStorageFile = new File(usersPath);
		if(!usersStorageFile.exists()) {
			Utils.log("Plik " + usersStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		
		preloadUsers();		
	}
	
	@Override
	public List<CalendarEvent> loadEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEvents(List<CalendarEvent> events) {
		// TODO Auto-generated method stub
	}
	
	public int getHighestUserId() {
		return highestUserId;
	}
	
	private void preloadUsers() {
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

}
