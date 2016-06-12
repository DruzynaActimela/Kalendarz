package com.actimel.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import com.actimel.calendar.impl.CSVImporter;
import com.actimel.models.CalendarEvent;

/**
 * Test importera CSV.
 * @author ActimelTeam
 *
 */
public class CSVImporterTest {

	
	/**
	 * Plik z którego zostan¹ pobrane eventy.
	 */
	private static final String CSV_FILE = "./test_files/test-events-csv.csv";
	

	/**
	 * Test importowania z pliku CSV.
	 */
	@Test
	public final void testImport() {
		File file = new File(CSV_FILE);
		
		CSVImporter importer = new CSVImporter();
		try {
			importer.importEvents(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<CalendarEvent> importedEvents = importer.getEvents();
		assertTrue(importedEvents.size() == 1);
		
		CalendarEvent first = importedEvents.get(0);
		assertEquals(first.getName(), "Moje zdarzenie");
		assertEquals(first.getId(), 1);
		
	}
	
	/**
	 * Test z nieistniej¹cym plikiem.
	 * @throws FileNotFoundException 
	 */
	@Test(expected = FileNotFoundException.class)
	public final void testNonExistingFile() throws FileNotFoundException {
		File nonExistingFile = new File("./asdqwe.csv");
		CSVImporter importer = new CSVImporter();	
		importer.importEvents(nonExistingFile);
	}
	

}
