package com.actimel.intfs;

import java.util.List;
import java.util.TimeZone;

import com.actimel.models.CalendarEvent;

/**
 * Abstrakcyjna klasa, definiuj¹ca 
 * jak maj¹ wygl¹dac klasy eksportuj¹ce eventy z aplikacji.
 * @author ActimelTeam
 *
 */
public abstract class CalendarExporter {
	
	/**
	 * Lista zdarzeñ do wyeksportowania.
	 */
	private final List<CalendarEvent> events;
	
	/**
	 * Domyœlna strefa czasowa.
	 */
	private final 
	TimeZone defaultTimezone = TimeZone.getTimeZone("Europe/Warsaw");
	
	
	/**
	 * Konstruktor klasy.
	 * @param eventsList Lista zdarzeñ, które maj¹ zostac wyeksportowane.
	 */
	protected CalendarExporter(final List<CalendarEvent> eventsList) {
		this.events = eventsList;
	}
	
	/**
	 * Funkcja eksportuj¹ca zdarzenia.
	 * @return Reprezentacja zdarzeñ w formacie, 
	 * który implementuje dana klasa eksportuj¹ca, jako String.
	 */
	public abstract String export();
	
	/**
	 * Funkcja umo¿liwiaj¹ca pobranie listy zdarzeñ.
	 * @return Lista zdarzeñ.
	 */
	public final List<CalendarEvent> getEvents() {
		return events;
	}
	
	/**
	 * Funkcja umo¿liwiaj¹ca pobranie domyœlnej strefy czasowej dla zdarzeñ.
	 * @return Obiekt domyœlnej strefy czasowej
	 */
	public final TimeZone getDefaultTimezone() {
		return defaultTimezone;
	}
}
