package com.actimel.calendar.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.actimel.intfs.CalendarImporter;
import com.actimel.models.CalendarEvent;
import com.actimel.utils.Utils;
import com.opencsv.CSVReader;

/**
 * Abstrakcyjna klasa, definiuj¹ca jak maj¹ wygl¹dac klasy importuj¹ce eventy z
 * plików CSV.
 * 
 * @author ActimelTeam
 *
 */

public class CSVImporter extends CalendarImporter {
	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z którego nale¿y wczytaæ zdarzenia
	 * @throws FileNotFoundException
	 */
	public void importEvents(File csvFile) throws FileNotFoundException {
		importEvents(new FileReader(csvFile));
	}

	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z którego nale¿y wczytaæ zdarzenia
	 * @throws FileNotFoundException
	 */
	public void importEvents(Reader reader) {
		CSVReader csvReader = new CSVReader(reader);
		String[] nextLine;
		try {
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine.length != 6) {
					continue;
				}
				
				try {
					int id = Integer.parseInt(nextLine[0]);
					long start = Utils.dateToTimestamp(nextLine[2], dateFormat);
					long end = Utils.dateToTimestamp(nextLine[3], dateFormat);

					CalendarEvent event = new CalendarEvent(id, nextLine[1], start, end, false, true);
					events.add(event);
				} catch (NumberFormatException e) {
					continue;
				}
			}
		} catch (IOException e) {
		}

	}

	
}
