package com.actimel.models;

import com.actimel.utils.Utils;
import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentuj�ca lekcj�.
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
	 * Godzina rozpocz�cia zaj�c.
	 */
	@SerializedName("hour_start")
	private String hourStart;
	
	/**
	 * Godzina zakonczenia zaj�c.
	 */
	@SerializedName("hour_end")
	private String hourEnd;
	
	/**
	 * Nazwa przedmiotu.
	 */
	private String przedmiot;
	
	/**
	 * Typ zaj�c.
	 */
	@SerializedName("typ_zajec")
	private String typZajec;
	
	/**
	 * Nauczyciel prowadz�cy zaj�cia.
	 */
	private String nauczyciel;
	
	/**
	 * Link do planu nauczyciela.
	 */
	@SerializedName("nauczyciel_url")
	private String nauczycielUrl;
	
	/**
	 * Sale, w kt�rych mog� odbywac si� zaj�cia.
	 */
	private String sale;
	
	/**
	 * Uwagi dot. zaj�c.
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
