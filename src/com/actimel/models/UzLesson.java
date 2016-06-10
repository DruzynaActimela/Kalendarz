package com.actimel.models;

import com.actimel.utils.Utils;
import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentuj¹ca lekcjê.
 * @author ActimelTeam
 *
 */
public class UzLesson {
	
	/**
	 * Dzien tygodnia.
	 */
	private String dzien;
	
	/**
	 * Grupa A, B, etc.
	 */
	private String grupa;
	
	
	/**
	 * Godzina rozpoczêcia zajêc.
	 */
	@SerializedName("hour_start")
	private String hourStart;
	
	/**
	 * Godzina zakonczenia zajêc.
	 */
	@SerializedName("hour_end")
	private String hourEnd;
	
	/**
	 * Nazwa przedmiotu.
	 */
	private String przedmiot;
	
	/**
	 * Typ zajêc.
	 */
	@SerializedName("typ_zajec")
	private String typZajec;
	
	/**
	 * Nauczyciel prowadz¹cy zajêcia.
	 */
	private String nauczyciel;
	
	/**
	 * Link do planu nauczyciela.
	 */
	@SerializedName("nauczyciel_url")
	private String nauczycielUrl;
	
	/**
	 * Sale, w których mog¹ odbywac siê zajêcia.
	 */
	private String sale;
	
	/**
	 * Uwagi dot. zajêc.
	 */
	private String uwagi;
	
	/**
	 * HTML uwag.
	 */
	@SerializedName("uwagi_html")
	private String uwagiHtml;
	
	/**
	 * Funkcja zamieniajaca godzine na long.
	 * @return Godzina jako long
	 */
	public final long startToLong() {
		return Utils.strTimeToLong(hourStart);
	}
	
	/**
	 * Funkcja zamieniajaca godzine na long.
	 * @return Godzina jako long
	 */
	public final long endToLong() {
		return Utils.strTimeToLong(hourEnd);
	}

	/**
	 * Getter nazwy.
	 * @return Nazwa
	 */
	public final String getName() {
		return przedmiot;
	}
	
}
