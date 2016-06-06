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
	 * Tytuł zdarzenia.
	 */
	private String title;
	
	/**
	 * Timestamp rozpoczęcia zdarzenia.
	 */
	private long start;
	
	/**
	 * Timestamp zakończenia zdarzenia.
	 */
	private long end;
	
	/**
	 * Wartośc określająca, czy zdarzenie jest całodniowe.
	 */
	private boolean allday;
	
	/**
	 * Wartosc określająca, czy zdarzenie jest publiczne.
	 */
	private boolean isPublic;
	
	/**
	 * Zmienna przechowująca ID właściciela zdarzenia.
	 */
	private int ownerId;
	
	/**
	 * Zmienna przechowująca ID grupy, w której znajduje się zdarzenie.
	 */
	private int parentGroupId;
	
	/**
	 * Konstruktor zdarzenia w kalendarzu.
	 * @param id ID zdarzenia
	 * @param name Nazwa zdarzenia
	 * @param stamp_start Timestamp rozpoczęcia zdarzenia 
	 * wyrażony w milisekundach
	 * @param stamp_end Timestamp zakończenia zdarzenia wyrażony w milisekundach
	 * @param allday Zmienna określająca, czy zdarzenie jest całodniowe.
	 * @param is_public Zmienna określająca, czy zdarzenie jest publiczne.
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
	 * Getter timestampa początku zdarzenia.
	 * @return Timestamp początku zdarzenia w milisekundach
	 */
	public final long getStampStart() {
		return start;
	}
	
	/**
	 * Setter timestampa początku zdarzenia.
	 * @param stampStart Nowy timestamp w milisekundach
	 */
	public final void setStampStart(final long stampStart) {
		this.start = stampStart;
	}
	/**
	 * Getter timestampa końca zdarzenia.
	 * @return Timestamp końca zdarzenia w milisekundach
	 */
	public final long getStampEnd() {
		return end;
	}
	/**
	 * Setter timestampa końca zdarzenia.
	 * @param stampEnd Nowy timestamp w milisekundach
	 */
	public final void setStampEnd(final long stampEnd) {
		this.end = stampEnd;
	}
	
	/**
	 * Getter właściwości: allDay.
	 * @return Czy zdarzenie jest całodniowe.
	 */
	
	public final boolean isAllDay() {
		return allday;
	}

	/**
	 * Setter właściwości: allDay.
	 * @param newAllday Czy zdarzenie ma byc całodniowe
	 */
	public final void setAllDay(final boolean newAllday) {
		this.allday = newAllday;
	}
	
	/**
	 * Getter właściwości: isPublic.
	 * @return Czy zdarzenie jest publiczne
	 */
	public final boolean isPublic() {
		return isPublic;
	}
	/**
	 * Setter właściwości: isPublic.
	 * @param p Czy zdarzenie ma byc publiczne
	 */
	public final void setPublic(final boolean p) {
		this.isPublic = p;
	}
	
	/**
	 * Metoda umożliwiająca ustawienie właściciela zdarzenia.
	 * @param nid ID nowego właściciela
	 */
	public final void setOwnerId(final int nid) {
		ownerId = nid;
	}
	
	/**
	 * Metoda umożliwiająca pobranie ID właściciela.
	 * @return ID właściciela
	 */
	public final int getOwnerId() {
		return ownerId;
	}
	
	/**
	 * Metoda umożliwiająca ustawienie grupy zdarzenia.
	 * @param nid ID nowej grupy
	 */
	public final void setParentGroupId(final int nid) {
		parentGroupId = nid;
	}
	
	/**
	 * Metoda umożliwiająca pobranie ID grupy zdarzenia.
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
