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

/**
 * Implementacja przechowywania danych w plikach.
 * @author ActimelTeam
 *
 */
public class FileStorage implements StorageIntf {
	
	/**
	 * Referencja do pliku ze zdarzeniami.
	 * @uml.property  name="eventsStorageFile"
	 */
	private File eventsStorageFile;
	
	/**
	 * Referencja do pliku z uzytkownikami.
	 * @uml.property  name="usersStorageFile"
	 */
	private File usersStorageFile;
	
	/**
	 * Referencja do pliku z grupami zdarzeñ.
	 * @uml.property  name="eventsGroupsStorageFile"
	 */
	private File eventsGroupsStorageFile;
	
	/**
	 * Najwy¿sze ID u¿ytkownika.
	 * @uml.property  name="highestUserId"
	 */
	private int highestUserId = 0;
	
	/**
	 * Najwyzsze ID zdarzenia.
	 * @uml.property  name="highestEventId"
	 */
	private int highestEventId = 0;
	
	/**
	 * Najwy¿sze ID grupy zdarzeñ.
	 * @uml.property  name="highestEventGroupId"
	 */
	private int highestEventGroupId = 0;
	
	/**
	 * Referencja do instancji aplikacji kalendarza.
	 * @uml.property  name="app"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private CalendarApp app;
	
	/**
	 * Cache u¿ytkowników w systemie.
	 * @uml.property  name="cachedUsers"
	 * @uml.associationEnd  qualifier="getName:java.lang.String com.actimel.models.User"
	 */
	private HashMap<String, User> cachedUsers = new HashMap<String, User>();
	
	/**
	 * Cache zdarzeñ w systemie.
	 * @uml.property  name="cachedEvents"
	 * @uml.associationEnd  qualifier="valueOf:java.lang.Integer com.actimel.models.CalendarEvent"
	 */
	private HashMap<Integer, CalendarEvent> 
	cachedEvents = new HashMap<Integer, CalendarEvent>();
	
	/**
	 * Cache grup zdarzeñ w systemie.
	 * @uml.property  name="cachedEventGroups"
	 * @uml.associationEnd  qualifier="valueOf:java.lang.Integer com.actimel.models.EventGroup"
	 */
	private HashMap<Integer, EventGroup> 
	cachedEventGroups = new HashMap<Integer, EventGroup>();
	
	
	/**
	 * Konstruktor implementacji przechowywania danych aplikacji w plikach.
	 * @param appInstance Instancja aplikacji
	 * @param eventsPath Œcie¿ka do pliku ze zdarzeniami
	 * @param eventsGroupsPath Œcie¿ka do pliku z grupami zdarzeñ
	 * @param usersPath Œcie¿ka do pliku z u¿ytkownikami
	 */
	public FileStorage(final CalendarApp appInstance, final String eventsPath, final String eventsGroupsPath, final String usersPath) {
		this.app = appInstance;
		
		eventsStorageFile = new File(eventsPath);
		if (!eventsStorageFile.exists()) {
			Utils.log("Plik " + eventsStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		eventsGroupsStorageFile = new File(eventsGroupsPath);
		if (!eventsGroupsStorageFile.exists()) {
			Utils.log("Plik " + eventsGroupsStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		usersStorageFile = new File(usersPath);
		if (!usersStorageFile.exists()) {
			Utils.log("Plik " + usersStorageFile.getAbsolutePath() + " nie istnieje!");
		}
		
		
		
		
		preloadUsers();		
	}
	
	/**
	 * Metoda odpowiedzialna za za³adowanie zdarzeñ do cache.
	 */
	public final void preloadEvents() {
		Type t = new TypeToken<HashMap<Integer, CalendarEvent>>() { }.getType();
		String json = "{}";
		if (eventsStorageFile.exists()) {
			json = Utils.readFile(eventsStorageFile);
		}
		cachedEvents = app.getGson().fromJson(json, t);
		Utils.log("FileStorage: Events preloaded");
		
		for (Entry<Integer, CalendarEvent> ev : cachedEvents.entrySet()) {
			if (ev.getValue().getId() > highestEventId) {
				highestEventId = ev.getValue().getId();
			}
		}
	}
	
	/**
	 * Getter najwy¿szego ID u¿ytkownika.
	 * @return  Najwy¿sze ID u¿ytkownika
	 * @uml.property  name="highestUserId"
	 */
	public final int getHighestUserId() {
		return highestUserId;
	}
	
	/**
	 * Getter najwy¿szego ID zdarzenia.
	 * @return  Najwy¿sze ID zdarzenia
	 * @uml.property  name="highestEventId"
	 */
	public final int getHighestEventId() {
		return highestEventId;
	}
	
	/**
	 * Getter najwy¿szego ID grupy zdarzeñ.
	 * @return  Najwy¿sze ID grupy zdarzeñ
	 * @uml.property  name="highestEventGroupId"
	 */
	public final int getHighestEventGroupId() {
		return highestEventGroupId;
	}
	
	
	/**
	 * Metoda odpowiedzialna za za³adowanie u¿ytkowników do cache.
	 */
	public final void preloadUsers() {
		Type t = new TypeToken<HashMap<String, User>>() { }.getType();
		String json = "{}";
		if (usersStorageFile.exists()) {
			json = Utils.readFile(usersStorageFile);
		}
		cachedUsers = app.getGson().fromJson(json, t);
		Utils.log("FileStorage: Users preloaded");
		
		for (Entry<String, User> user : cachedUsers.entrySet()) {
			if (user.getValue().getId() > highestUserId) {
				highestUserId = user.getValue().getId();
			}
		}
	}
	
	
	@Override
	public final User loadUser(final String username) {
		return loadUser(username, false);
	}
	
	@Override
	public final User loadUser(final int userId) {
		for (Entry<String, User> user : cachedUsers.entrySet()) {
			if (user.getValue().getId() == userId) {
				return user.getValue();
			}
		}
		return null;
	}
	
	
	/**
	 * Metoda odpowiedzialna za za³adowanie u¿ytkownika, 
	 * z mo¿liwoœci¹ pominiÄ™cia cache.
	 * @param username Nazwa u¿ytkownika
	 * @param noCache Czy pomin¹c cache?
	 * @return Obiekt u¿ytkownika
	 */
	public final User loadUser(final String username, final boolean noCache) {
		if (noCache) {
			preloadUsers();
		}	
		if (cachedUsers.containsKey(username)) {
			return cachedUsers.get(username);
		}
		return null;
	}

	@Override
	public final void storeUser(final User user) {
		if (user.getId() > highestUserId) {
			highestUserId = user.getId();
		}
		cachedUsers.put(user.getName(), user);
	}
	
	/**
	 * Metoda odpowiedzialna za zapisanie wszystkich u¿ytkowników.
	 */
	public final void saveUsers() {
		Type t = new TypeToken<HashMap<String, User>>() { }.getType();
		String json = app.getGson().toJson(cachedUsers, t);
		
		Utils.writeToFile(usersStorageFile, json);		
		Utils.log("FileStorage: Users saved");
	}
	
	
	@Override
	public final CalendarEvent loadEvent(final int eventId) {
		return loadEvent(eventId, false);
	}
	
	/**
	 * Metoda odpowiedzialna za za³adowanie zdarzenia, 
	 * z mo¿liwoœci¹ pominiÄ™cia cache.
	 * @param eventId ID zdarzenia
	 * @param noCache Czy pominac cache?
	 * @return Obiekt zdarzenia
	 */
	public final CalendarEvent 
	loadEvent(final int eventId, final boolean noCache) {
		if (noCache) {
			preloadEvents();
		}
		if (cachedEvents.containsKey(eventId)) {
			return cachedEvents.get(eventId);
		}
		return null;
	}

	@Override
	public final void saveEvent(final CalendarEvent event) {
		if (event.getId() > highestEventId) {
			highestEventId = event.getId();
		}
		cachedEvents.put(event.getId(), event);
	}
	@Override
	public final boolean deleteEvent(final int eventId) {
		if (cachedEvents.containsKey(eventId)) {
			cachedEvents.remove(eventId);
			saveEvents();
			return true;
		}
		return false;
	}
	
	/**
	 * Metoda odpowiedzialna za zapisanie zdarzeñ.
	 */
	public final void saveEvents() {
		Type t = new TypeToken<HashMap<Integer, CalendarEvent>>() { }.getType();
		String json = app.getGson().toJson(cachedEvents, t);
		
		Utils.writeToFile(eventsStorageFile, json);		
		Utils.log("FileStorage: Events saved");
	}
	

	@Override
	public final List<CalendarEvent> 
	searchEvents(final String query, final String searchFieldName) {
		List<CalendarEvent> result = new ArrayList<CalendarEvent>();
		if (query == null) {
			return result;
		}
		
		try {
			Field field = CalendarEvent.class.getDeclaredField(searchFieldName);
			field.setAccessible(true);
			for (Entry<Integer, CalendarEvent> eg : cachedEvents.entrySet()) {
				CalendarEvent group = eg.getValue();
				Object fieldValue = field.get(group);				
				if (query.equals(String.valueOf(fieldValue))) {
					result.add(group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public final EventGroup loadEventGroup(final int eventGroupId) {
		return loadEventGroup(eventGroupId, false);
	}
	
	/**
	 * Metoda odpowiedzialna za za³adowanie grupy zdarzeñ, 
	 * z mo¿liwoœci¹ pominiÄ™cia cache.
	 * @param eventGroupId ID grupy zdarzeñ
	 * @param noCache Czy pominac cache?
	 * @return Obiekt grupy zdarzeñ
	 */
	public final EventGroup 
	loadEventGroup(final int eventGroupId, final boolean noCache) {
		if (noCache) {
			preloadEventGroups();
		}	
		
		if (cachedEventGroups.containsKey(eventGroupId)) {
			return  cachedEventGroups.get(eventGroupId);
		}
		return null;
	}
	
	/**
	 * Metoda odpowiedzialna za za³adowanie grup eventów do cache.
	 */
	public final  void preloadEventGroups() {
		Type t = new TypeToken<HashMap<Integer, EventGroup>>() { }.getType();
		String json = "{}";
		if (eventsGroupsStorageFile.exists()) {
			json = Utils.readFile(eventsGroupsStorageFile);
		}
		cachedEventGroups = app.getGson().fromJson(json, t);
		Utils.log("FileStorage: Event groups preloaded");
		
		for (Entry<Integer, EventGroup> eg : cachedEventGroups.entrySet()) {
			if (eg.getValue().getId() > highestEventGroupId) {
				highestEventGroupId = eg.getValue().getId();
			}
		}
	}

	@Override
	public final void saveEventGroup(final EventGroup eventGroup) {
		if (eventGroup.getId() > highestEventGroupId) {
			highestEventGroupId = eventGroup.getId();
		}
		cachedEventGroups.put(eventGroup.getId(), eventGroup);	
	}

	@Override
	public final List<EventGroup> 
	searchEventsGroups(final String query, final String searchFieldName) {
		List<EventGroup> result = new ArrayList<EventGroup>();
		if (query == null) {
			return result;
		}
		
		try {
			Field field = EventGroup.class.getDeclaredField(searchFieldName);
			field.setAccessible(true);
			for (Entry<Integer, EventGroup> eg : cachedEventGroups.entrySet()) {
				EventGroup group = eg.getValue();
				Object fieldValue = field.get(group);				
				if (query.equals(String.valueOf(fieldValue))) {
					result.add(group);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Metoda odpowiedzialna za zapisanie grup zdarzeñ.
	 */
	public final void saveEventGroups() {
		Type t = new TypeToken<HashMap<Integer, EventGroup>>() { }.getType();
		String json = app.getGson().toJson(cachedEventGroups, t);
		
		Utils.writeToFile(eventsGroupsStorageFile, json);		
		Utils.log("FileStorage: Event groups saved");
	}
	
	@Override
	public final List<CalendarEvent> 
	searchEventsBetween(final long timestampStart, final long timestampEnd, final int userId) {
		List<CalendarEvent> result = new ArrayList<CalendarEvent>();
		if (timestampStart == 0 || timestampEnd == 0) {
			return result;
		}
		
		Utils.log("Searching...");
		
		for (Entry<Integer, CalendarEvent> eg : cachedEvents.entrySet()) {
			CalendarEvent evt = eg.getValue();
			if (evt.getOwnerId() == userId) {
				if (evt.getStampStart() >= timestampStart 
						&& evt.getStampStart() <= timestampEnd) {
					result.add(evt);
				}
			}
		}	
		
		return result;
	}

}
