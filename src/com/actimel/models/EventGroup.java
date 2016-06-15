package com.actimel.models;

/**
 * Klasa reprezentuj¹ca grupê zdarzeñ.
 * @author ActimelTeam
 *
 */
public class EventGroup {
	
	/**
	 * ID grupy.
	 * @uml.property  name="id"
	 */
	private int id;
	
	/**
	 * Nazwa grupy.
	 * @uml.property  name="name"
	 */
	private String name;
	
	/**
	 * Kolor grupy.
	 * @uml.property  name="color"
	 */
	private String color;
	
	/**
	 * W³aœciciel grupy.
	 * @uml.property  name="ownerId"
	 */
	private int ownerId; // user
	
	/**
	 * Zmienna okreœlaj¹ca, czy grupa jest publiczna.
	 * @uml.property  name="isPublic"
	 */
	private boolean isPublic;
	
	/**
	 * Konstrutor grupy zdarzeñ.
	 * @param gid ID grupy zdarzeñ
	 * @param gname Nazwa
	 * @param gcolor Kolor
	 * @param gOwnerId ID w³aœciciela
	 * @param gIsPublic Zmienna okreœlaj¹ca, czy grupa zdarzeñ jest publiczna
	 */
	public EventGroup(final int gid, final String gname, final String gcolor, final int gOwnerId, final boolean gIsPublic) {
		super();
		this.id = gid;
		this.name = gname;
		this.color = gcolor;
		this.ownerId = gOwnerId;
		this.isPublic = gIsPublic;
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
	 * Getter nazwy grupy zdarzeñ.
	 * @return  Nazwa grupy
	 * @uml.property  name="name"
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Setter nazwy.
	 * @param nname  Nowa nazwa
	 * @uml.property  name="name"
	 */
	public final void setName(final String nname) {
		this.name = nname;
	}
	
	/**
	 * Getter koloru grupy.
	 * @return  reprezentacja HEX koloru
	 * @uml.property  name="color"
	 */
	public final String getColor() {
		return color;
	}

	/**
	 * Setter koloru grupy.
	 * @param ccolor  Nowy kolor jako wartosc HEX, np. 000000 lub FFFFFF
	 * @uml.property  name="color"
	 */
	public final void setColor(final String ccolor) {
		this.color = ccolor;
	}

	/**
	 * Metoda umo¿liwiaj¹ca ustawienie w³aœciciela.
	 * @param nid  ID nowego w³aœciciela
	 * @uml.property  name="ownerId"
	 */
	public final void setOwnerId(final int nid) {
		ownerId = nid;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca pobranie ID w³aœciciela.
	 * @return  ID w³aœciciela
	 * @uml.property  name="ownerId"
	 */
	public final int getOwnerId() {
		return ownerId;
	}

	/**
	 * Getter w³aœciwoœci: isPublic.
	 * @return  Czy grupa zdarzeñ jest publiczna
	 * @uml.property  name="isPublic"
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
