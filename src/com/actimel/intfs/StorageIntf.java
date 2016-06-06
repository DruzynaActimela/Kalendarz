package com.actimel.intfs;


import java.util.List;

import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.User;

/**
 * Interfejs definiuj�cy metody obiektu przechowuj�cego dane.
 */
public interface StorageIntf {
	
	/**
	 * Funkcja odpowiadaj�ca za �adowanie zdarzenia po ID.
	 * @param eventId ID zdarzenia
	 * @return Obiekt zdarzenia, je�li instnieje.
	 */
	CalendarEvent loadEvent(int eventId);
	
	/**
	 * Funkcja odpowiadaj�ca za zapisanie zdarzenia.
	 * @param event Obiekt zdarzenia
	 */
	void saveEvent(CalendarEvent event);
	
	/**
	 * Funkcja odpowiedzialna za szukanie zdarze� po okre�lonym polu.
	 * @param query Kryterium wyszukiwania
	 * @param searchFieldName Pole po kt�rym skrypt wyszuka zdarzenia
	 * @return Lista zdarze� pasuj�cych do kryteri�w wyszukiwania
	 */	
	List<CalendarEvent> searchEvents(String query, String searchFieldName);
	
	/**
	 * Funkcja odpowiedzialna za szukanie zdarze� 
	 * dla okreslonego u�ytkownika w okre�lonym przedziale czasu.
	 * @param timestampStart Warto�� pocz�tkowa, 
	 * jako timestamp z milisekundami 
	 * @param timestampEnd Warto�� ko�cowa, jako timestamp z milisekundami
	 * @param userId ID u�ytkownika, kt�rego eventy zostan� przeszukane
	 * @return Lista zdarze� pasuj�cych do kryterium wyszukiwania
	 */
	List<CalendarEvent> searchEventsBetween(long timestampStart, long timestampEnd, int userId);
	

	/**
	 * Funkcja odpowiadaj�ca za usuwanie zdarze�.
	 * @param eventId ID zdarzenia, kt�re ma zostac usuni�te.
	 * @return Status usuni�cia, true je�li zdarzenie zosta�o usuni�te, 
	 * false je�li nie.
	 */	
	boolean deleteEvent(int eventId);
	
	/**
	 * Funkcja odpowiadaj�ca za pobrane obiektu grupy zdarze�.
	 * @param eventGroupId ID grupy do pobrania
	 * @return Obiekt grupy, je�li istnieje, null je�li nie istnieje
	 */
	EventGroup loadEventGroup(int eventGroupId);
	
	/**
	 * Funkcja odpowiadaj�ca za zapisane grupy zdarze�.
	 * @param eventGroup Obiekt grupy zdarze� do zapisania
	 */
	void saveEventGroup(EventGroup eventGroup);
	
	/**
	 * Funkcja odpowiadaj�ca za szukanie grup zdarze� 
	 * bazuj�c na danym polu i kryterium.
	 * @param query Kryterium
	 * @param searchFieldName Pole po kt�rym odb�dzie si� wyszukiwanie
	 * @return Lista grup zdarze� pasuj�cych do kryterium
	 */
	List<EventGroup> searchEventsGroups(String query, String searchFieldName);
	
	/**
	 * Funkcja odpowiadaj�ca za pobranie u�ytkownika o okre�lonej nazwie.
	 * @param username Nazwa u�ytkownika
	 * @return Obiekt u�ytkownika, je�li istnieje
	 */
	User loadUser(String username);
	
	/**
	 * Funkcja odpowiadaj�ca za zapisane u�ytkownika.
	 * @param user Obiekt u�ytkownika
	 */
	void storeUser(User user);
}
