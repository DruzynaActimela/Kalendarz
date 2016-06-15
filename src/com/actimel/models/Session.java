package com.actimel.models;

/**
 * Klasa reprezentuj¹ca sesjê u¿ytkownika.
 */
public class Session {
	/**
	 * Obiekt u¿ytkownika sesji.
	 * @uml.property  name="user"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private User user;
	
	/**
	 * Klucz sesji.
	 * @uml.property  name="key"
	 */
	private String key;	
	
	/**
	 * Konstruktor sesji.
	 * @param uuser Obiekt u¿ytkownika
	 * @param kkey Klucz sesji
	 */
	public Session(final User uuser, final String kkey) {
		this.user = uuser;
		this.key = kkey;
		
	}
	
	/**
	 * Getter u¿ytkownika danej sesji.
	 * @return  Obiekt u¿ytkownika
	 * @uml.property  name="user"
	 */
	public final User getUser() {
		return user;
	}

	/**
	 * Getter klucza sesji.
	 * @return  Klucz sesji
	 * @uml.property  name="key"
	 */
	public final String getKey() {
		return key;
	}
	
	/**
	 * Getter ID u¿ytkownika przypisanego do sesji.
	 * @return ID u¿ytkownika przypisanego do sesji.
	 */
	public final int getUserId() {
		if (user == null) {
			return 0;
		}
		
		return user.getId();
	}
	
	
}
