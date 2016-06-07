package com.actimel.models;

/**
 * Klasa reprezentuj�ca u�ytkownika.
 */
public class User {
	
	/**
	 * ID.
	 * @uml.property  name="id"
	 */
	private int id;
	
	/**
	 * Nazwa.
	 * @uml.property  name="name"
	 */
	private String name;
	
	/**
	 * Has�o.
	 * @uml.property  name="pass"
	 */
	private String pass;
	
	/**
	 * E-mail.
	 * @uml.property  name="email"
	 */
	private String email;
	
	/**
	 * Poziom uprawnie�.
	 * @uml.property  name="permissionLevel"
	 */
	private int permissionLevel;

	/**
	 * Konstruktor obiektu u�ytkownika.
	 * @param uid ID
	 * @param uName Nazwa
	 * @param uPass Hash has�a
	 * @param uEmail E-Mail
	 * @param uPermissionLevel Poziom uprawnie�s
	 */
	public User(final int uid, final String uName, final String uPass, final String uEmail, final int uPermissionLevel) {
		super();
		this.id = uid;
		this.name = uName;
		this.pass = uPass;
		this.email = uEmail;
		this.permissionLevel = uPermissionLevel;
	}
	
	/**
	 * Getter E-Maila u�ykownika.
	 * @return  E-Mail u�ytkownika
	 * @uml.property  name="email"
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Setter E-Maila u�ytkownika.
	 * @param eemail  Nowy adres E-Mail
	 * @uml.property  name="email"
	 */
	public final void setEmail(final String eemail) {
		this.email = eemail;
	}
	
	/**
	 * Getter ID u�ytkownika.
	 * @return  ID
	 * @uml.property  name="id"
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Getter nazwy u�ytkownika.
	 * @return  Nazwa u�ytkownika
	 * @uml.property  name="name"
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
	 * @return  Poziom uprawnie� u�ytkownika jako int.
	 * @uml.property  name="permissionLevel"
	 */
	public final int getPermissionLevel() {
		return permissionLevel;
	}
	
	
	
}
