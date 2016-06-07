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
	 * @uml.property  name="id"
	 */
	@SerializedName("_id")
	private int id;
	
	/**
	 * Tytu� zdarzenia.
	 * @uml.property  name="title"
	 */
	private String title;
	
	/**
	 * Timestamp rozpocz�cia zdarzenia.
	 * @uml.property  name="start"
	 */
	private long start;
	
	/**
	 * Timestamp zako�czenia zdarzenia.
	 * @uml.property  name="end"
	 */
	private long end;
	
	/**
	 * Warto�c okre�laj�ca, czy zdarzenie jest ca�odniowe.
	 * @uml.property  name="allday"
	 */
	private boolean allday;
	
	/**
	 * Wartosc okre�laj�ca, czy zdarzenie jest publiczne.
	 * @uml.property  name="isPublic"
	 */
	private boolean isPublic;
	
	/**
	 * Zmienna przechowuj�ca ID w�a�ciciela zdarzenia.
	 * @uml.property  name="ownerId"
	 */
	private int ownerId;
	
	/**
	 * Zmienna przechowuj�ca ID grupy, w kt�rej znajduje si� zdarzenie.
	 * @uml.property  name="parentGroupId"
	 */
	private int parentGroupId;
	
	/**
	 * Konstruktor zdarzenia w kalendarzu.
	 * @param eid ID zdarzenia
	 * @param name Nazwa zdarzenia
	 * @param stampStart Timestamp rozpocz�cia zdarzenia 
	 * wyra�ony w milisekundach
	 * @param stampEnd Timestamp zako�czenia zdarzenia wyra�ony w milisekundach
	 * @param alldayRef Zmienna okre�laj�ca, czy zdarzenie jest ca�odniowe.
	 * @param eIsPublic Zmienna okre�laj�ca, czy zdarzenie jest publiczne.
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
	 * @return  ID zdarzenia
	 * @uml.property  name="id"
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Setter ID zdarzenia.
	 * @param newId  Nowe ID zdarzenia
	 * @uml.property  name="id"
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
	 * Getter timestampa pocz�tku zdarzenia.
	 * @return Timestamp pocz�tku zdarzenia w milisekundach
	 */
	public final long getStampStart() {
		return start;
	}
	
	/**
	 * Setter timestampa pocz�tku zdarzenia.
	 * @param stampStart Nowy timestamp w milisekundach
	 */
	public final void setStampStart(final long stampStart) {
		this.start = stampStart;
	}
	/**
	 * Getter timestampa ko�ca zdarzenia.
	 * @return Timestamp ko�ca zdarzenia w milisekundach
	 */
	public final long getStampEnd() {
		return end;
	}
	/**
	 * Setter timestampa ko�ca zdarzenia.
	 * @param stampEnd Nowy timestamp w milisekundach
	 */
	public final void setStampEnd(final long stampEnd) {
		this.end = stampEnd;
	}
	
	/**
	 * Getter w�a�ciwo�ci: allDay.
	 * @return Czy zdarzenie jest ca�odniowe.
	 */
	
	public final boolean isAllDay() {
		return allday;
	}

	/**
	 * Setter w�a�ciwo�ci: allDay.
	 * @param newAllday Czy zdarzenie ma byc ca�odniowe
	 */
	public final void setAllDay(final boolean newAllday) {
		this.allday = newAllday;
	}
	
	/**
	 * Getter w�a�ciwo�ci: isPublic.
	 * @return  Czy zdarzenie jest publiczne
	 * @uml.property  name="isPublic"
	 */
	public final boolean isPublic() {
		return isPublic;
	}
	/**
	 * Setter w�a�ciwo�ci: isPublic.
	 * @param p Czy zdarzenie ma byc publiczne
	 */
	public final void setPublic(final boolean p) {
		this.isPublic = p;
	}
	
	/**
	 * Metoda umo�liwiaj�ca ustawienie w�a�ciciela zdarzenia.
	 * @param nid  ID nowego w�a�ciciela
	 * @uml.property  name="ownerId"
	 */
	public final void setOwnerId(final int nid) {
		ownerId = nid;
	}
	
	/**
	 * Metoda umo�liwiaj�ca pobranie ID w�a�ciciela.
	 * @return  ID w�a�ciciela
	 * @uml.property  name="ownerId"
	 */
	public final int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Metoda umo�liwiaj�ca ustawienie grupy zdarzenia.
	 * @param nid  ID nowej grupy
	 * @uml.property  name="parentGroupId"
	 */
	public final void setParentGroupId(final int nid) {
		parentGroupId = nid;
	}
	
	/**
	 * Metoda umo�liwiaj�ca pobranie ID grupy zdarzenia.
	 * @return  ID grupy
	 * @uml.property  name="parentGroupId"
	 */
	public final int getParentGroupId() {
		return parentGroupId;
	}
	
	/**
	 * Metoda sprawdzaj�ca, czy zdarzenie posiada grup�.
	 * @return Wartosc true/false odpowiadaj�ca stanowi posiadania grupy
	 */
	public final boolean hasEventGroup() {
		return parentGroupId > 0;
	}
	
	/**
	 * Funkcja umo�liwiaj�ca pobranie instancji grupy zdarzenia.
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

