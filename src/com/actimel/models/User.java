package com.actimel.models;

/**
 * Klasa reprezentuj¹ca u¿ytkownika.
 */
public class User {
	
	/**
	 * ID.
	 */
	private int id;
	
	/**
	 * Nazwa.
	 */
	private String name;
	
	/**
	 * Has³o.
	 */
	private String pass;
	
	/**
	 * E-mail.
	 */
	private String email;
	
	/**
	 * Poziom uprawnieñ.
	 */
	private int permissionLevel;

	/**
	 * Konstruktor obiektu u¿ytkownika.
	 */
	public User(final int id, final String name, final String pass, final String email, final int permission_level) {
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.email = email;
		this.permissionLevel = permission_level;
	}
	
	/**
	 * Getter E-Maila u¿ykownika.
	 * @return E-Mail u¿ytkownika
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Setter E-Maila u¿ytkownika.
	 * @param eemail Nowy adres E-Mail
	 */
	public final void setEmail(final String eemail) {
		this.email = eemail;
	}
	
	/**
	 * Getter ID u¿ytkownika.
	 * @return ID
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Getter nazwy u¿ytkownika.
	 * @return Nazwa u¿ytkownika
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sprawdzenie, czy podane has³o u¿ytkownika pasuje 
	 * do has³a przechowywanego przez ten obiekt.
	 * @param ppass Has³o, które ma zostac sprawdzone.
	 * @return Wartosc true/false odpowiadaj¹ca poprawnoœci has³a.
	 */
	public final boolean isPasswordCorrect(final String ppass) {
		if (ppass == null) {
			return false;
		}
		return ppass.trim().equals(this.pass.trim());
	}

	/**
	 * Getter dla poziomu uprawnieñ.
	 * @return Poziom uprawnieñ u¿ytkownika jako int.
	 */
	public final int getPermissionLevel() {
		return permissionLevel;
	}
	
	
	
}
