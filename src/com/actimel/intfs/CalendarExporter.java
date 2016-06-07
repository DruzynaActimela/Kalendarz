package com.actimel.intfs;

import java.util.List;
import java.util.TimeZone;

import com.actimel.models.CalendarEvent;

/**
 * Abstrakcyjna klasa, definiuj�ca 
 * jak maj� wygl�dac klasy eksportuj�ce eventy z aplikacji.
 * @author ActimelTeam
 *
 */
public abstract class CalendarExporter {
	
	/**
	 * Lista zdarze� do wyeksportowania.
	 */
	private final List<CalendarEvent> events;
	
	/**
	 * Domy�lna strefa czasowa.
	 */
	private final 
	TimeZone defaultTimezone = TimeZone.getTimeZone("Europe/Warsaw");
	
	
	/**
	 * Konstruktor klasy.
	 * @param eventsList Lista zdarze�, kt�re maj� zostac wyeksportowane.
	 */
	protected CalendarExporter(final List<CalendarEvent> eventsList) {
		this.events = eventsList;
	}
	
	/**
	 * Funkcja eksportuj�ca zdarzenia.
	 * @return Reprezentacja zdarze� w formacie, 
	 * kt�ry implementuje dana klasa eksportuj�ca, jako String.
	 */
	public abstract String export();
	
	/**
	 * Funkcja umo�liwiaj�ca pobranie listy zdarze�.
	 * @return Lista zdarze�.
	 */
	public final List<CalendarEvent> getEvents() {
		return events;
	}
	
	/**
	 * Funkcja umo�liwiaj�ca pobranie domy�lnej strefy czasowej dla zdarze�.
	 * @return Obiekt domy�lnej strefy czasowej
	 */
	public final TimeZone getDefaultTimezone() {
		return defaultTimezone;
	}
}
