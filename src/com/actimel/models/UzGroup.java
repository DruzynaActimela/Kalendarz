package com.actimel.models;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentuj¹ca grupê kierunku.
 * @author ActimelTeam
 *
 */
public class UzGroup {
	
	/**
	 * ID grupy.
	 */
	private String id;
	
	/**
	 * Nazwa grupy.
	 */
	private String nazwa;
	
	/**
	 * Czesc linku do grupy.
	 */
	private String link;
	
	/**
	 * Semestr.
	 */
	private String semestr;
	
	/**
	 * Skrót semestru.
	 */
	@SerializedName("semestr_short")
	private String semestrShort;
	
	
}
