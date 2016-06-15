package com.actimel.models;

/**
 * Klasa reprezentuj�ca grup� zdarze�.
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
	 * W�a�ciciel grupy.
	 * @uml.property  name="ownerId"
	 */
	private int ownerId; // user
	
	/**
	 * Zmienna okre�laj�ca, czy grupa jest publiczna.
	 * @uml.property  name="isPublic"
	 */
	private boolean isPublic;
	
	/**
	 * Konstrutor grupy zdarze�.
	 * @param gid ID grupy zdarze�
	 * @param gname Nazwa
	 * @param gcolor Kolor
	 * @param gOwnerId ID w�a�ciciela
	 * @param gIsPublic Zmienna okre�laj�ca, czy grupa zdarze� jest publiczna
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
	 * Getter nazwy grupy zdarze�.
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
	 * Metoda umo�liwiaj�ca ustawienie w�a�ciciela.
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
	 * Getter w�a�ciwo�ci: isPublic.
	 * @return  Czy grupa zdarze� jest publiczna
	 * @uml.property  name="isPublic"
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
