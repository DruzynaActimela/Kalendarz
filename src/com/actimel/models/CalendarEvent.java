package com.actimel.models;

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
	 * Tytu� zdarzenia.
	 */
	private String title;
	
	/**
	 * Timestamp rozpocz�cia zdarzenia.
	 */
	private long start;
	
	/**
	 * Timestamp zako�czenia zdarzenia.
	 */
	private long end;
	
	/**
	 * Warto�c okre�laj�ca, czy zdarzenie jest ca�odniowe.
	 */
	private boolean allday;
	
	/**
	 * Wartosc okre�laj�ca, czy zdarzenie jest publiczne.
	 */
	private boolean isPublic;
	
	/**
	 * Zmienna przechowuj�ca ID w�a�ciciela zdarzenia.
	 */
	private int ownerId;
	
	/**
	 * Zmienna przechowuj�ca ID grupy, w kt�rej znajduje si� zdarzenie.
	 */
	private int parentGroupId;
	
	/**
	 * Konstruktor zdarzenia w kalendarzu.
	 * @param id ID zdarzenia
	 * @param name Nazwa zdarzenia
	 * @param stamp_start Timestamp rozpocz�cia zdarzenia 
	 * wyra�ony w milisekundach
	 * @param stamp_end Timestamp zako�czenia zdarzenia wyra�ony w milisekundach
	 * @param allday Zmienna okre�laj�ca, czy zdarzenie jest ca�odniowe.
	 * @param is_public Zmienna okre�laj�ca, czy zdarzenie jest publiczne.
	 */
	public CalendarEvent(final int id, final String name, final long stamp_start, final long stamp_end, final boolean allday, final boolean is_public) {
		this.id = id;
		this.title = name;
		this.start = stamp_start;
		this.end = stamp_end;
		this.allday = allday;
		this.isPublic = is_public;
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
	 * @return Czy zdarzenie jest publiczne
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
	 * @param nid ID nowego w�a�ciciela
	 */
	public final void setOwnerId(final int nid) {
		ownerId = nid;
	}
	
	/**
	 * Metoda umo�liwiaj�ca pobranie ID w�a�ciciela.
	 * @return ID w�a�ciciela
	 */
	public final int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Metoda umo�liwiaj�ca ustawienie grupy zdarzenia.
	 * @param nid ID nowej grupy
	 */
	public final void setParentGroupId(final int nid) {
		parentGroupId = nid;
	}
	
	/**
	 * Metoda umo�liwiaj�ca pobranie ID grupy zdarzenia.
	 * @return ID grupy
	 */
	public final int getParentGroupId() {
		return parentGroupId;
	}
	
	
	
}

/*
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
		VEvent e = new VEvent();
		
 * 
 * 
 * 
 * java.util.Calendar cal = java.util.Calendar.getInstance();
cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
cal.set(java.util.Calendar.DAY_OF_MONTH, 25);

VEvent christmas = new VEvent(new Date(cal.getTime()), "Christmas Day");
// initialise as an all-day event..
christmas.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
            
Output:

BEGIN:VEVENT
DTSTAMP:20050222T044240Z
DTSTART;VALUE=DATE:20051225
SUMMARY:Christmas Day
END:VEVENT
 */
