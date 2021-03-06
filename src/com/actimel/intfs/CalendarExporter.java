package com.actimel.intfs;

import java.util.ArrayList;
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
	 * @uml.property  name="events"
	 */
	private final List<CalendarEvent> events;
	
	/**
	 * Domy�lna strefa czasowa.
	 * @uml.property  name="defaultTimezone"
	 */
	private final 
	TimeZone defaultTimezone = TimeZone.getTimeZone("Europe/Warsaw");
	
	
	/**
	 * Konstruktor klasy.
	 */
	protected CalendarExporter() {
		events = new ArrayList<CalendarEvent>();
	}
	
	/**
	 * Funkcja eksportuj�ca zdarzenia.
	 * @param eventsRef Lista zdarze�
	 * @return Reprezentacja zdarze� w formacie, 
	 * kt�ry implementuje dana klasa eksportuj�ca, jako String.
	 */
	public abstract String export(final List<CalendarEvent> eventsRef);
	
	/**
	 * Funkcja umo�liwiaj�ca pobranie listy zdarze�.
	 * @return Lista zdarze�.
	 */
	public final List<CalendarEvent> getEvents() {
		return events;
	}
	
	/**
	 * Funkcja umo�liwiaj�ca pobranie domy�lnej strefy czasowej dla zdarze�.
	 * @return  Obiekt domy�lnej strefy czasowej
	 * @uml.property  name="defaultTimezone"
	 */
	public final TimeZone getDefaultTimezone() {
		return defaultTimezone;
	}
}
