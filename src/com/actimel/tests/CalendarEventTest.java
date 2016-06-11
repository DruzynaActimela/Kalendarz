package com.actimel.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.calendar.CalendarApp;
import com.actimel.models.CalendarEvent;

public class CalendarEventTest {

	/**
	 * Event.
	 */
	private static CalendarEvent evt;
	
	/**
	 * Kontekst aplikacji.
	 */
	private static CalendarApp appContext;
	
	//dane testowe
	/**
	 * ID testowego eventu.
	 */
	private static final int TEST_EVENT_ID = 123;
	
	/**
	 * Data rozpoczêcia testowego eventu.
	 */
	private static final long TEST_EVENT_START = 1465655001492L;
	
	/**
	 * Data zakonczenia testowego eventu.
	 */
	private static final long TEST_EVENT_END = 1465655001492L + 3600000;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		appContext = new CalendarApp();

		
		evt = new CalendarEvent(TEST_EVENT_ID, "Testowy event", TEST_EVENT_START, TEST_EVENT_END, true, true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testCalendarEvent() {
		assertNotNull(evt);
		
	}
	
	@Test
	public void testCalendarEventGetters() {
		assertEquals(evt.getName(), "Testowy event");		
		assertEquals(evt.getId(), TEST_EVENT_ID);
		assertEquals(evt.getStampStart(), TEST_EVENT_START);
		assertEquals(evt.getStampEnd(), TEST_EVENT_END);
	}
	

}
