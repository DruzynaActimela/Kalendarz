package com.actimel.models;

/**
 * Klasa reprezentuj¹ca grupê zdarzeñ.
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
	 * W³aœciciel grupy.
	 */
	private int ownerId; // user
	
	/**
	 * Zmienna okreœlaj¹ca, czy grupa jest publiczna.
	 */
	private boolean isPublic;
	
	/**
	 * Konstrutor grupy zdarzeñ.
	 * @param id ID grupy zdarzeñ
	 * @param name Nazwa
	 * @param color Kolor
	 * @param owner_id ID w³aœciciela
	 * @param is_public Zmienna okreœlaj¹ca, czy grupa zdarzeñ jest publiczna
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
	 * Getter nazwy grupy zdarzeñ.
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
	 * Metoda umo¿liwiaj¹ca ustawienie w³aœciciela.
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
	 * Getter w³aœciwoœci: isPublic.
	 * @return Czy grupa zdarzeñ jest publiczna
	 */
	public final boolean isPublic() {
		return isPublic;
	}
	/**
	 * Setter w³aœciwoœci: isPublic.
	 * @param p Czy grupa zdarzeñ ma byc publiczna
	 */
	public final void setPublic(final boolean p) {
		this.isPublic = p;
	}

	
}
