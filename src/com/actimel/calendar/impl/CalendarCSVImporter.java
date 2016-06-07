package com.actimel.calendar.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.actimel.calendar.Const;
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

public class CalendarCSVImporter {
	/**
	 * Lista zimportowanych zdarzeñ
	 * 
	 * @uml.property name="events"
	 */
	private List<CalendarEvent> events;

	/**
	 * Domyœlna strefa czasowa.
	 * 
	 * @uml.property name="defaultTimezone"
	 */
	private final TimeZone defaultTimezone = TimeZone.getTimeZone("Europe/Warsaw");

	/**
	 * Format daty w importowanym pliku
	 * 
	 * @uml.property name="dateFormat"
	 */
	private String dateFormat = Const.DATE_FORMAT_DAY_TIME;

	/**
	 * Konstruktor inicjalizuj¹cy listê zdarzeñ
	 */
	public CalendarCSVImporter() {
		events = new ArrayList<CalendarEvent>();
	}

	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z którego nale¿y wczytaæ zdarzenia
	 * @throws FileNotFoundException
	 */
	public void importCSV(File csvFile) throws FileNotFoundException {
		importCSV(new FileReader(csvFile));
	}

	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z którego nale¿y wczytaæ zdarzenia
	 * @throws FileNotFoundException
	 */
	public void importCSV(Reader reader) {
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

	/**
	 * Funkcja umo¿liwiaj¹ca pobranie listy zdarzeñ, które uda³o siê
	 * zimportowaæ.
	 * 
	 * @return Lista zdarzeñ.
	 */
	public final List<CalendarEvent> getEvents() {
		return events;
	}

	/**
	 * Funkcja umo¿liwiaj¹ca pobranie domyœlnej strefy czasowej dla zdarzeñ.
	 * 
	 * @return Obiekt domyœlnej strefy czasowej
	 * @uml.property name="defaultTimezone"
	 */
	public final TimeZone getDefaultTimezone() {
		return defaultTimezone;
	}

	/**
	 * Funkcja umo¿liwiaj¹ca pobranie aktualnego formatu daty
	 * 
	 * @return Format importowanej daty jako String
	 * @uml.property name="dateFormat"
	 */
	public final String getDateFromat() {
		return dateFormat;
	}

	/**
	 * Funkcja umo¿liwiaj¹ca ustawienie aktualnego formatu daty
	 * 
	 * @param dateFormat
	 *            Format importowanej daty jako String
	 * @uml.property name="dateFormat"
	 */
	public final void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
