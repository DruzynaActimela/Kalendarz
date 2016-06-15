package com.actimel.models;

/**
 * Dni tygodnia.
 * @author ActimelTeam
 *
 */
public enum DayOfWeek {
	
	/**
	 * Niedziela.
	 */
	SUNDAY(0),
	
	/**
	 * Poniedzia³ek.
	 */
	MONDAY(1),
	
	/**
	 * Wtorek.
	 */
	TUESDAY(2),
	
	/**
	 * Œroda.
	 */
	WEDNESDAY(3),
	
	/**
	 * Czwartek.
	 */
	THURSDAY(4),
	
	/**
	 * Pi¹tek.
	 */
	FRIDAY(5),
	
	/**
	 * Sobota.
	 */
	SATURDAY(6);
	
	/**
	 * Aktualna wartosc.
	 */	
	private int value;
	
	/**
	 * Konstruktor.
	 * @param id ID dnia tygodnia
	 */
	DayOfWeek(final int id) {
		value = id;
	}
	
	/**
	 * Getter ID dnia.
	 * @return ID dnia tygodnia
	 */
	public int getId() {
		return value;
	}
	
}
