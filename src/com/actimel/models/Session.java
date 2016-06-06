package com.actimel.models;

/**
 * Klasa reprezentująca sesję użytkownika.
 */
public class Session {
	/**
	 * Obiekt użytkownika sesji.
	 */
	private User user;
	
	/**
	 * Klucz sesji.
	 */
	private String key;	
	
	/**
	 * Konstruktor sesji.
	 * @param uuser Obiekt użytkownika
	 * @param kkey Klucz sesji
	 */
	public Session(final User uuser, final String kkey) {
		this.user = uuser;
		this.key = kkey;
		
	}
	
	/**
	 * Getter użytkownika danej sesji.
	 * @return Obiekt użytkownika
	 */
	public final User getUser() {
		return user;
	}

	/**
	 * Getter klucza sesji.
	 * @return Klucz sesji
	 */
	public final String getKey() {
		return key;
	}
	
	/**
	 * Getter ID użytkownika przypisanego do sesji.
	 * @return ID użytkownika przypisanego do sesji.
	 */
	public final int getUserId() {
		if (user == null) {
			return 0;
		}
		
		return user.getId();
	}
	
	
}
