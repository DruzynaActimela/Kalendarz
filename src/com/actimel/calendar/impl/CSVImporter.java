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
	 * @param csvFile Obiekt pliku, z którego nale¿y wczytaæ zdarzenia
	 * @throws FileNotFoundException B³¹d, gdy plik nie istnieje
	 */
	public final void importEvents(final File csvFile) throws FileNotFoundException {
		importEvents(new FileReader(csvFile));
	}

	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param reader Obiekt readera, z którego bêd¹ czytane dane CSV
	 */
	public final void importEvents(final Reader reader) {
		CSVReader csvReader = new CSVReader(reader);
		String[] nextLine;
		final int six = 6;
		final int three = 3;
		try {
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine.length != six) {
					continue;
				}
				
				try {
					
					int id = Integer.parseInt(nextLine[0]);
					long start = Utils.dateToTimestamp(nextLine[2], getDateFromat());
					long end = Utils.dateToTimestamp(nextLine[three], getDateFromat());

					CalendarEvent event = new CalendarEvent(id, nextLine[1], start, end, false, true);
					super.addEvent(event);
				} catch (NumberFormatException e) {
					continue;
				}
			}
		} catch (IOException e) {
		}
		
		if (reader != null) {
			
		
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
