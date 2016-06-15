package com.actimel.models;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentuj¹ca kierunek.
 * @author 4i60r
 *
 */
public class Division {

	/**
	 * ID kierunku.
	 */
	@SerializedName("id")
	private String id;
	
	/**
	 * Nazwa kierunku.
	 */
	@SerializedName("nazwa")
	private String name;
	
	/**
	 * Czêsc linku do kierunku.
	 */
	@SerializedName("link")
	private String link;

	/**
	 * Konstruktor klasy kierunku.
	 * @param idRef ID kierunku
	 * @param nameRef Nazwa
	 * @param linkRef Link
	 */
	public Division(final String idRef, final String nameRef, final String linkRef) {
		super();
		this.id = idRef;
		this.name = nameRef;
		this.link = linkRef;
	}

	/**
	 * Getter ID kierunku.
	 * @return ID kierunku
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Setter ID.
	 * @param idd Nowe ID
	 */
	public final void setId(final String idd) {
		this.id = idd;
	}
	
	/**
	 * Getter nazwy kierunku.
	 * @return Nazwa kierunku
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Setter nazwy.
	 * @param nameRef Nowa nazwa
	 */
	public final void setName(final String nameRef) {
		this.name = nameRef;
	}
	/**
	 * Getter linku kierunku.
	 * @return Link kierunku
	 */
	public final String getLink() {
		return link;
	}

	/**
	 * Setter linku.
	 * @param linkRef Nowy link
	 */
	public final void setLink(final String linkRef) {
		this.link = linkRef;
	}
	
	
}
