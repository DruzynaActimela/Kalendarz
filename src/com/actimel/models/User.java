package com.actimel.models;

/**
 * Klasa reprezentuj�ca u�ytkownika.
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
	 * Has�o.
	 */
	private String pass;
	
	/**
	 * E-mail.
	 */
	private String email;
	
	/**
	 * Poziom uprawnie�.
	 */
	private int permissionLevel;

	/**
	 * Konstruktor obiektu u�ytkownika.
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
	 * Getter E-Maila u�ykownika.
	 * @return E-Mail u�ytkownika
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Setter E-Maila u�ytkownika.
	 * @param eemail Nowy adres E-Mail
	 */
	public final void setEmail(final String eemail) {
		this.email = eemail;
	}
	
	/**
	 * Getter ID u�ytkownika.
	 * @return ID
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Getter nazwy u�ytkownika.
	 * @return Nazwa u�ytkownika
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sprawdzenie, czy podane has�o u�ytkownika pasuje 
	 * do has�a przechowywanego przez ten obiekt.
	 * @param ppass Has�o, kt�re ma zostac sprawdzone.
	 * @return Wartosc true/false odpowiadaj�ca poprawno�ci has�a.
	 */
	public final boolean isPasswordCorrect(final String ppass) {
		if (ppass == null) {
			return false;
		}
		return ppass.trim().equals(this.pass.trim());
	}

	/**
	 * Getter dla poziomu uprawnie�.
	 * @return Poziom uprawnie� u�ytkownika jako int.
	 */
	public final int getPermissionLevel() {
		return permissionLevel;
	}
	
	
	
}
