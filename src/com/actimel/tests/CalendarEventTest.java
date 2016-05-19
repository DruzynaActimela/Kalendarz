package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.CalendarEvent;

public class CalendarEventTest {

	private static CalendarEvent evt;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		evt = new CalendarEvent(1, "Testowy event", 13, 14, true);
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
		assertEquals(evt.getName(), "Testowy event");
		
	}
	

}
