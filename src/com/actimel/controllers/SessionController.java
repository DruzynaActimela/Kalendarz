package com.actimel.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.actimel.models.Session;
import com.actimel.models.User;
import com.actimel.utils.Utils;

/**
 * Kontroler sesji u¿ytkowników aplikacji.
 * @author ActimelTeam
 *
 */
public class SessionController {
	
	/**
	 * Sól sesji, u¿ywana do tworzenia kluczy sesji.
	 */
	private final String SESSION_SALT = 
			"a8b8c9d9ce0a9c8f8c9088ff9a9d908f9c7a6a9c08f79ca909c8f7f6a8fb9abc9088a7";
	/**
	 * Lista aktywnych sesji.
	 */
	private HashMap<String, Session> sessions = new HashMap<String, Session>();
	
	/**
	 * Konstruktor kontrolera sesji.
	 */
	public SessionController() {
		
	}
	
	
	/**
	 * Metoda odpowiedzialna za tworzenie nowych sesji.
	 * @param user Obiekt u¿ytkownika, dla którego sesja zostanie utworzona.
	 * @param ip IP u¿ytkownika
	 * @return obiekt nowo utworzonej sesji
	 */
	public final Session createSession(final User user, final String ip) {
		String key = createSessionKey(user.getName(), ip, SESSION_SALT);
		Session session = new Session(user, key);
		sessions.put(key, session);	
		return session;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca pobranie sesji po kluczu.
	 * @param key Klucz sesji
	 * @return Obiekt sesji, gdy znaleziono, null jeœli nie.
	 */
	public final Session getSessionByKey(final String key) {
		for (Entry<String, Session> entry : sessions.entrySet()) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Metoda odpowiedzialna za uniewa¿nienie (usuniêcie) sesji z aplikacji.
	 * @param key Klucz sesji
	 * @return true, jeœli usuniêto, false jeœli nie.
	 */
	public final boolean destroySession(final String key) {
		if (sessions.containsKey(key)) {
			sessions.remove(key);
		}
		return false;
	}
	/**
	 * Metoda odpowiedzialna za okreœlenie, 
	 * czy podany klucz jest prawid³owy dla podanego u¿ytkownika o podanym IP.
	 * @param key Klucz sesji, który ma zostac sprawdzony
	 * @param user Obiekt u¿ytkownika
	 * @param ip Adres IP
	 * @return true, jeœli klucz jest poprawny, false, jeœli nie.
	 */
	public final boolean isSessionKeyValidForUser(final String key, final User user, final String ip) {
		if (key == null || user == null || ip == null) {
			return false;
		}
		String serverKey = 
				createSessionKey(user.getName(), ip, SESSION_SALT);		
		return key.equals(serverKey) && sessions.containsKey(key);
	}
	
	/**
	 * Metoda odpowiedzialna za utworzenie nowego klucza sesji 
	 * dla podanych parametrów.
	 * @param username Nazwa u¿ytkownika
	 * @param ip adres IP
	 * @param salt sól sesji
	 * @return Klucz sesji jako String.
	 */
	public static final String createSessionKey(final String username, final String ip, final String salt) {
		try {
			return Utils.sha256(salt + ip + username);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
