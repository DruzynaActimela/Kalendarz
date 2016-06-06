package com.actimel.models;

/**
 * Klasa reprezentuj�ca grup� zdarze�.
 * @author ActimelTeam
 *
 */
public class EventGroup {
	
	/**
	 * ID grupy.
	 */
	private int id;
	
	/**
	 * Nazwa grupy.
	 */
	private String name;
	
	/**
	 * Kolor grupy.
	 */
	private String color;
	
	/**
	 * W�a�ciciel grupy.
	 */
	private int ownerId; // user
	
	/**
	 * Zmienna okre�laj�ca, czy grupa jest publiczna.
	 */
	private boolean isPublic;
	
	/**
	 * Konstrutor grupy zdarze�.
	 * @param id ID grupy zdarze�
	 * @param name Nazwa
	 * @param color Kolor
	 * @param owner_id ID w�a�ciciela
	 * @param is_public Zmienna okre�laj�ca, czy grupa zdarze� jest publiczna
	 */
	public EventGroup(final int id, final String name, final String color, final int owner_id, final boolean is_public) {
		super();
		this.id = id;
		this.name = name;
		this.color = color;
		this.ownerId = owner_id;
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
	 * Getter nazwy grupy zdarze�.
	 * @return Nazwa grupy
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Setter nazwy.
	 * @param nname Nowa nazwa
	 */
	public final void setName(final String nname) {
		this.name = nname;
	}
	
	/**
	 * Getter koloru grupy.
	 * @return reprezentacja HEX koloru
	 */
	public final String getColor() {
		return color;
	}

	/**
	 * Setter koloru grupy.
	 * @param ccolor Nowy kolor jako wartosc HEX, np. 000000 lub FFFFFF
	 */
	public final void setColor(final String ccolor) {
		this.color = ccolor;
	}

	/**
	 * Metoda umo�liwiaj�ca ustawienie w�a�ciciela.
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
	 * Getter w�a�ciwo�ci: isPublic.
	 * @return Czy grupa zdarze� jest publiczna
	 */
	public final boolean isPublic() {
		return isPublic;
	}
	/**
	 * Setter w�a�ciwo�ci: isPublic.
	 * @param p Czy grupa zdarze� ma byc publiczna
	 */
	public final void setPublic(final boolean p) {
		this.isPublic = p;
	}

	
}
