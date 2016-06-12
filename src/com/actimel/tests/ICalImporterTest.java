package com.actimel.tests;

import static org.junit.Assert.*;

import java.io.File;

import java.util.List;

import org.junit.Test;

import com.actimel.calendar.impl.ICalImporter;
import com.actimel.models.CalendarEvent;

/**
 * Test importera iCal.
 * @author ActimelTeam
 *
 */
public class ICalImporterTest {

	
	/**
	 * Plik z którego zostan¹ pobrane eventy.
	 */
	private static final String ICAL_FILE = "./test_files/calendar.ics";
	

	/**
	 * Test importowania z pliku iCal.
	 */
	@Test
	public final void testImport() {
		File file = new File(ICAL_FILE);
		
		ICalImporter importer = new ICalImporter();	
		importer.importEvents(file);
	
		List<CalendarEvent> importedEvents = importer.getEvents();		
		assertTrue(importedEvents.size() == 1);
		
		CalendarEvent first = importedEvents.get(0);
		assertEquals(first.getName(), "Moje zdarzenie");
		
	}


}
