package com.actimel.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.actimel.calendar.CalendarApp;
import com.actimel.calendar.impl.CSVExporter;
import com.actimel.calendar.impl.CSVImporter;
import com.actimel.models.CalendarEvent;
import com.actimel.utils.Utils;

/**
 * Test eksportera CSV.
 * @author ActimelTeam
 *
 */
public class CSVExporterTest {

	
	/**
	 * Test eksportera eventów.
	 */
	@Test
	public final void test() {
		CalendarApp app = new CalendarApp(); // musi byc, bo exporter korzysta z kontekstu
		
		CSVExporter exporter = new CSVExporter();
		
		List<CalendarEvent> eventsToExport = Arrays.asList(
				new CalendarEvent(1, "Testowy Event", 1L, 2L, false, true),
				new CalendarEvent(2, "Testowy Event 2", 1L, 2L, false, true),
				new CalendarEvent(2 + 1, "Testowy Event 3", 1L, 2L, false, true)
				
		);
		String csv = exporter.export(eventsToExport);
		
		File tmpCsvFile = new File("./___tmpcsvfile.csv");
		Utils.writeToFile(tmpCsvFile, csv);
		
		CSVImporter importer = new CSVImporter();
		try {
			importer.importEvents(tmpCsvFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<CalendarEvent> importedEvents = importer.getEvents();
		tmpCsvFile.delete();
		
		assertEquals(importedEvents.size(), eventsToExport.size());
		assertTrue(importedEvents.size() == 2 + 1);
		
	}

}
