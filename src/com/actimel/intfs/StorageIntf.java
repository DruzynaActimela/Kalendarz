package com.actimel.intfs;


import java.util.List;

import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.User;

/**
 * Interfejs definiuj¹cy metody obiektu przechowuj¹cego dane.
 */
public interface StorageIntf {
	
	/**
	 * Funkcja odpowiadaj¹ca za ³adowanie zdarzenia po ID.
	 * @param eventId ID zdarzenia
	 * @return Obiekt zdarzenia, jeœli instnieje.
	 */
	CalendarEvent loadEvent(int eventId);
	
	/**
	 * Funkcja odpowiadaj¹ca za zapisanie zdarzenia.
	 * @param event Obiekt zdarzenia
	 */
	void saveEvent(CalendarEvent event);
	
	/**
	 * Funkcja odpowiedzialna za szukanie zdarzeñ po okreœlonym polu.
	 * @param query Kryterium wyszukiwania
	 * @param searchFieldName Pole po którym skrypt wyszuka zdarzenia
	 * @return Lista zdarzeñ pasuj¹cych do kryteriów wyszukiwania
	 */	
	List<CalendarEvent> searchEvents(String query, String searchFieldName);
	
	/**
	 * Funkcja odpowiedzialna za szukanie zdarzeñ 
	 * dla okreslonego u¿ytkownika w okreœlonym przedziale czasu.
	 * @param timestampStart Wartoœæ pocz¹tkowa, 
	 * jako timestamp z milisekundami 
	 * @param timestampEnd Wartoœæ koñcowa, jako timestamp z milisekundami
	 * @param userId ID u¿ytkownika, którego eventy zostan¹ przeszukane
	 * @return Lista zdarzeñ pasuj¹cych do kryterium wyszukiwania
	 */
	List<CalendarEvent> searchEventsBetween(long timestampStart, long timestampEnd, int userId);
	

	/**
	 * Funkcja odpowiadaj¹ca za usuwanie zdarzeñ.
	 * @param eventId ID zdarzenia, które ma zostac usuniête.
	 * @return Status usuniêcia, true jeœli zdarzenie zosta³o usuniête, 
	 * false jeœli nie.
	 */	
	boolean deleteEvent(int eventId);
	
	/**
	 * Funkcja odpowiadaj¹ca za pobrane obiektu grupy zdarzeñ.
	 * @param eventGroupId ID grupy do pobrania
	 * @return Obiekt grupy, jeœli istnieje, null jeœli nie istnieje
	 */
	EventGroup loadEventGroup(int eventGroupId);
	
	/**
	 * Funkcja odpowiadaj¹ca za zapisane grupy zdarzeñ.
	 * @param eventGroup Obiekt grupy zdarzeñ do zapisania
	 */
	void saveEventGroup(EventGroup eventGroup);
	
	/**
	 * Funkcja odpowiadaj¹ca za szukanie grup zdarzeñ 
	 * bazuj¹c na danym polu i kryterium.
	 * @param query Kryterium
	 * @param searchFieldName Pole po którym odbêdzie siê wyszukiwanie
	 * @return Lista grup zdarzeñ pasuj¹cych do kryterium
	 */
	List<EventGroup> searchEventsGroups(String query, String searchFieldName);
	
	/**
	 * Funkcja odpowiadaj¹ca za pobranie u¿ytkownika o okreœlonej nazwie.
	 * @param username Nazwa u¿ytkownika
	 * @return Obiekt u¿ytkownika, jeœli istnieje
	 */
	User loadUser(String username);
	
	/**
	 * Funkcja odpowiadaj¹ca za zapisane u¿ytkownika.
	 * @param user Obiekt u¿ytkownika
	 */
	void storeUser(User user);
}
