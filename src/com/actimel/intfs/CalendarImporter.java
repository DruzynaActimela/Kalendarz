package com.actimel.intfs;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.actimel.calendar.Const;
import com.actimel.models.CalendarEvent;

public abstract class CalendarImporter {
	/**
	 * Lista zimportowanych zdarzeñ
	 * 
	 * @uml.property name="events"
	 */
	protected List<CalendarEvent> events;

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
	protected String dateFormat = Const.DATE_FORMAT_DAY_TIME;
	
	/**
	 * Konstruktor inicjalizuj¹cy listê zdarzeñ
	 */
	public CalendarImporter() {
		events = new ArrayList<CalendarEvent>();
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
