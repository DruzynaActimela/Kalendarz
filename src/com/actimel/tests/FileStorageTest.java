package com.actimel.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.calendar.CalendarApp;
import com.actimel.calendar.FileStorage;
import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.User;

/**
 * Test klasy przechowuj¹cej dane w plikach.
 * @author ActimelTeam
 *
 */
public class FileStorageTest {

	/**
	 * Sciezka do pliku z eventami.
	 */
	private static final String EVENTS_PATH = "./test_files/test-events-path.json";
	
	/**
	 * Sciezka do pliku z grupami.
	 */
	private static final String GROUPS_PATH = "./test_files/test-evt-groups-path.json";
	
	/**
	 * Sceizka do plika z userami.
	 */
	private static final String USERS_PATH = "./test_files/test-users-path.json";
	
	/**
	 * Obiekt ktory bedziemy testowac.
	 */
	private static FileStorage storage;
	
	
	/**
	 * Setup.
	 * @throws Exception Wyjatek.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CalendarApp app = new CalendarApp();
		
		storage = new FileStorage(app, EVENTS_PATH, GROUPS_PATH, USERS_PATH);
	}
	
	
	/**
	 * Przetestowanie FileStorage.
	 */
	@Test
	public final void test() {
		storage.preloadUsers();
		storage.preloadEventGroups();
		storage.preloadEvents();
		
		// Przetestowanie, czy zostal poprawnie zaladowany plik z uzytkownikami
		User user = storage.loadUser(1);
		assertNotNull(user);
		assertEquals(user.getName(), "TestoweKonto");
		assertEquals(user.getId(), 1);
		
		// Przetestowanie, czy zostal poprawnie zaladowany plik z eventami
		
		CalendarEvent event = storage.loadEvent(1);
		assertNotNull(event);
		assertEquals(event.getName(), "Testowe Zdarzenie");
		assertEquals(event.getId(), 1);
		assertEquals(event.getParentGroupId(), 1);
		
		//Proba zaladowania nieistniejacego zdarzenia
		final int randomId = 123434;
		assertNull(storage.loadEvent(randomId));
		
		// Przetestowanie czy zostal zaladowany plik z grupami
		
		EventGroup group = storage.loadEventGroup(1);
		assertNotNull(group);
		assertEquals(group.getName(), "Testowa Grupa");
		assertEquals(group.getId(), 1);
		assertEquals(group.getOwnerId(), 1);
		
		assertEquals(storage.getHighestEventGroupId(), 1);
		assertEquals(storage.getHighestEventId(), 1);
		assertEquals(storage.getHighestUserId(), 1);
		
		//Test szukania uzytkownika po nicku
		User user2 = storage.loadUser("TestoweKonto");
		assertNotNull(user2);
		assertEquals(user2.getName(), "TestoweKonto");
		
		
		//Test szukania zdarzeñ po polu
		List<CalendarEvent> foundEvents = storage.searchEvents("1", "ownerId");
		assertTrue(foundEvents.size() == 1); // jest tylko jeden event z ownerId 1
		
		//Test szukania zdarzeñ w zakresie dat
		final long start = 1460806100000L;
		final long end = 1460809900000L;
		List<CalendarEvent> foundEvents2 = storage.searchEventsBetween(start, end, 1);
		assertTrue(foundEvents2.size() == 1); // jest tylko jeden event w tym zakresie
		
		//Test szukania w zmyœlonym zakresie
		List<CalendarEvent> foundEvents3 = storage.searchEventsBetween(0, 0, 1);
		assertTrue(foundEvents3.size() == 0); // nie ma zdarzen w tym zakresie
		
		
		
	}

}
