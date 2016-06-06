package com.actimel.models;

/**
 * Klasa reprezentująca użytkownika.
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
	 * Hasło.
	 */
	private String pass;
	
	/**
	 * E-mail.
	 */
	private String email;
	
	/**
	 * Poziom uprawnień.
	 */
	private int permissionLevel;

	/**
	 * Konstruktor obiektu użytkownika.
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
	 * Getter E-Maila użykownika.
	 * @return E-Mail użytkownika
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Setter E-Maila użytkownika.
	 * @param eemail Nowy adres E-Mail
	 */
	public final void setEmail(final String eemail) {
		this.email = eemail;
	}
	
	/**
	 * Getter ID użytkownika.
	 * @return ID
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Getter nazwy użytkownika.
	 * @return Nazwa użytkownika
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sprawdzenie, czy podane hasło użytkownika pasuje 
	 * do hasła przechowywanego przez ten obiekt.
	 * @param ppass Hasło, które ma zostac sprawdzone.
	 * @return Wartosc true/false odpowiadająca poprawności hasła.
	 */
	public final boolean isPasswordCorrect(final String ppass) {
		if (ppass == null) {
			return false;
		}
		return ppass.trim().equals(this.pass.trim());
	}

	/**
	 * Getter dla poziomu uprawnień.
	 * @return Poziom uprawnień użytkownika jako int.
	 */
	public final int getPermissionLevel() {
		return permissionLevel;
	}
	
	
	
}
