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
 * Abstrakcyjna klasa, definiuj�ca jak maj� wygl�dac klasy importuj�ce eventy z
 * plik�w CSV.
 * 
 * @author ActimelTeam
 *
 */

public class CalendarCSVImporter {
	/**
	 * Lista zimportowanych zdarze�
	 * 
	 * @uml.property name="events"
	 */
	private List<CalendarEvent> events;

	/**
	 * Domy�lna strefa czasowa.
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
	 * Konstruktor inicjalizuj�cy list� zdarze�
	 */
	public CalendarCSVImporter() {
		events = new ArrayList<CalendarEvent>();
	}

	/**
	 * Funkcja importuj�ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z kt�rego nale�y wczyta� zdarzenia
	 * @throws FileNotFoundException
	 */
	public void importCSV(File csvFile) throws FileNotFoundException {
		importCSV(new FileReader(csvFile));
	}

	/**
	 * Funkcja importuj�ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z kt�rego nale�y wczyta� zdarzenia
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
	 * Funkcja umo�liwiaj�ca pobranie listy zdarze�, kt�re uda�o si�
	 * zimportowa�.
	 * 
	 * @return Lista zdarze�.
	 */
	public final List<CalendarEvent> getEvents() {
		return events;
	}

	/**
	 * Funkcja umo�liwiaj�ca pobranie domy�lnej strefy czasowej dla zdarze�.
	 * 
	 * @return Obiekt domy�lnej strefy czasowej
	 * @uml.property name="defaultTimezone"
	 */
	public final TimeZone getDefaultTimezone() {
		return defaultTimezone;
	}

	/**
	 * Funkcja umo�liwiaj�ca pobranie aktualnego formatu daty
	 * 
	 * @return Format importowanej daty jako String
	 * @uml.property name="dateFormat"
	 */
	public final String getDateFromat() {
		return dateFormat;
	}

	/**
	 * Funkcja umo�liwiaj�ca ustawienie aktualnego formatu daty
	 * 
	 * @param dateFormat
	 *            Format importowanej daty jako String
	 * @uml.property name="dateFormat"
	 */
	public final void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
