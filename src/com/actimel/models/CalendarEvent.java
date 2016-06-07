package com.actimel.models;

import com.actimel.calendar.CalendarApp;
import com.google.gson.annotations.SerializedName;

/**
 * Klasa zdarzenia w kalendarzu.
 * @author ActimelTeam
 *
 */
public class CalendarEvent {
	
	/**
	 * ID zdarzenia.
	 */
	@SerializedName("_id")
	private int id;
	
	/**
	 * Tytu³ zdarzenia.
	 */
	private String title;
	
	/**
	 * Timestamp rozpoczêcia zdarzenia.
	 */
	private long start;
	
	/**
	 * Timestamp zakoñczenia zdarzenia.
	 */
	private long end;
	
	/**
	 * Wartoœc okreœlaj¹ca, czy zdarzenie jest ca³odniowe.
	 */
	private boolean allday;
	
	/**
	 * Wartosc okreœlaj¹ca, czy zdarzenie jest publiczne.
	 */
	private boolean isPublic;
	
	/**
	 * Zmienna przechowuj¹ca ID w³aœciciela zdarzenia.
	 */
	private int ownerId;
	
	/**
	 * Zmienna przechowuj¹ca ID grupy, w której znajduje siê zdarzenie.
	 */
	private int parentGroupId;
	
	/**
	 * Konstruktor zdarzenia w kalendarzu.
	 * @param eid ID zdarzenia
	 * @param name Nazwa zdarzenia
	 * @param stampStart Timestamp rozpoczêcia zdarzenia 
	 * wyra¿ony w milisekundach
	 * @param stampEnd Timestamp zakoñczenia zdarzenia wyra¿ony w milisekundach
	 * @param alldayRef Zmienna okreœlaj¹ca, czy zdarzenie jest ca³odniowe.
	 * @param eIsPublic Zmienna okreœlaj¹ca, czy zdarzenie jest publiczne.
	 */
	public CalendarEvent(final int eid, final String name, final long stampStart, final long stampEnd, final boolean alldayRef, final boolean eIsPublic) {
		this.id = eid;
		this.title = name;
		this.start = stampStart;
		this.end = stampEnd;
		this.allday = alldayRef;
		this.isPublic = eIsPublic;
	}

	/**
	 * Getter ID zdarzenia.
	 * @return ID zdarzenia
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Setter ID zdarzenia.
	 * @param newId Nowe ID zdarzenia
	 */
	public final void setId(final int newId) {
		this.id = newId;
	}

	/**
	 * Getter nazwy zdarzenia.
	 * @return Nazwa zdarzenia
	 */
	public final String getName() {
		return title;
	}

	/**
	 * Setter nazwy zdarzenia.
	 * @param name Nowa nazwa zdarzenia.
	 */
	public final void setName(final String name) {
		this.title = name;
	}

	/**
	 * Getter timestampa pocz¹tku zdarzenia.
	 * @return Timestamp pocz¹tku zdarzenia w milisekundach
	 */
	public final long getStampStart() {
		return start;
	}
	
	/**
	 * Setter timestampa pocz¹tku zdarzenia.
	 * @param stampStart Nowy timestamp w milisekundach
	 */
	public final void setStampStart(final long stampStart) {
		this.start = stampStart;
	}
	/**
	 * Getter timestampa koñca zdarzenia.
	 * @return Timestamp koñca zdarzenia w milisekundach
	 */
	public final long getStampEnd() {
		return end;
	}
	/**
	 * Setter timestampa koñca zdarzenia.
	 * @param stampEnd Nowy timestamp w milisekundach
	 */
	public final void setStampEnd(final long stampEnd) {
		this.end = stampEnd;
	}
	
	/**
	 * Getter w³aœciwoœci: allDay.
	 * @return Czy zdarzenie jest ca³odniowe.
	 */
	
	public final boolean isAllDay() {
		return allday;
	}

	/**
	 * Setter w³aœciwoœci: allDay.
	 * @param newAllday Czy zdarzenie ma byc ca³odniowe
	 */
	public final void setAllDay(final boolean newAllday) {
		this.allday = newAllday;
	}
	
	/**
	 * Getter w³aœciwoœci: isPublic.
	 * @return Czy zdarzenie jest publiczne
	 */
	public final boolean isPublic() {
		return isPublic;
	}
	/**
	 * Setter w³aœciwoœci: isPublic.
	 * @param p Czy zdarzenie ma byc publiczne
	 */
	public final void setPublic(final boolean p) {
		this.isPublic = p;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca ustawienie w³aœciciela zdarzenia.
	 * @param nid ID nowego w³aœciciela
	 */
	public final void setOwnerId(final int nid) {
		ownerId = nid;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca pobranie ID w³aœciciela.
	 * @return ID w³aœciciela
	 */
	public final int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca ustawienie grupy zdarzenia.
	 * @param nid ID nowej grupy
	 */
	public final void setParentGroupId(final int nid) {
		parentGroupId = nid;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca pobranie ID grupy zdarzenia.
	 * @return ID grupy
	 */
	public final int getParentGroupId() {
		return parentGroupId;
	}
	
	/**
	 * Metoda sprawdzaj¹ca, czy zdarzenie posiada grupê.
	 * @return Wartosc true/false odpowiadaj¹ca stanowi posiadania grupy
	 */
	public final boolean hasEventGroup() {
		return parentGroupId > 0;
	}
	
	/**
	 * Funkcja umo¿liwiaj¹ca pobranie instancji grupy zdarzenia.
	 * @return Instancja grupy zdarzenia
	 */
	public final EventGroup getEventGroup() {
		if (hasEventGroup()) {
			return 
			CalendarApp.getInstance()
			.getStorage()
			.loadEventGroup(parentGroupId);
		}
		return null;
	}
	
	
	
}

