package com.actimel.intfs;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.actimel.calendar.Const;
import com.actimel.models.CalendarEvent;

public abstract class CalendarImporter {
	/**
	 * Lista zimportowanych zdarze�
	 * 
	 * @uml.property name="events"
	 */
	protected List<CalendarEvent> events;

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
	protected String dateFormat = Const.DATE_FORMAT_DAY_TIME;
	
	/**
	 * Konstruktor inicjalizuj�cy list� zdarze�
	 */
	public CalendarImporter() {
		events = new ArrayList<CalendarEvent>();
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
